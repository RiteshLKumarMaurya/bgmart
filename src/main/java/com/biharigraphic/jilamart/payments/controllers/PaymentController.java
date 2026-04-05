package com.biharigraphic.jilamart.payments.controllers;

import com.biharigraphic.jilamart.auth.controller.AuthController;
import com.biharigraphic.jilamart.payments.model.dtos.CreatePaymentRequest;
import com.biharigraphic.jilamart.payments.model.dtos.CreatePaymentResponse;
import com.biharigraphic.jilamart.payments.model.entities.Payment;
import com.biharigraphic.jilamart.payments.model.entities.WebhookEvent;
import com.biharigraphic.jilamart.payments.model.enums.*;
import com.biharigraphic.jilamart.payments.repo.PaymentRepository;
import com.biharigraphic.jilamart.payments.repo.RazorpayWebhookEventRepository;
import com.biharigraphic.jilamart.user.repository.UserRepository;
import com.biharigraphic.jilamart.utils.BigDecimalUtil;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.*;

//for the psymrnt inbuillded
@RestController
@CrossOrigin(origins = "*") // allow Android to call
@RequestMapping("/api/payments/v1")
@RequiredArgsConstructor
public class PaymentController {
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final AuthController authController;

    private final PaymentRepository paymentRepository;

    private final RazorpayWebhookEventRepository razorpayWebhookEventRepository;

    @Value("${razorpay.webhook.secret}")
    private String RAZORPAY_WEBHOOK_SECRET;

    @Value("${razorpay.key_id}")
    private String RAZORPAY_KEY_ID;

    @Value("${razorpay.key_secret}")
    private String RAZORPAY_KEY_SECRET;

    private RazorpayClient client;

    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final String TAG = "Payment Controller";

    //todo: for the security purpose make use of the not hard code

    @PostConstruct
    public void init() {
        try {
            client = new RazorpayClient(RAZORPAY_KEY_ID, RAZORPAY_KEY_SECRET);
            //log the webhook secret for the confirmation

        } catch (RazorpayException e) {
            logger.error(TAG + " : Exception occured while initializing the client: " + e.getMessage());
        }
    }


    @PostMapping("/create-payment")
    public ResponseEntity<?> createOrder(
            @RequestBody CreatePaymentRequest request) throws Exception {

        log.info("create payment called!!!");

        int amount = request.getAmount();
        String currency = request.getCurrency();

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount);
        orderRequest.put("currency", currency);

        //todo: in later use the notes to slot mentioning
        JSONObject notes = new JSONObject();
        notes.put("instruction", "Leave at the door - within next 24hr ");
        orderRequest.put("notes", notes);
        orderRequest.put("payment_capture", 1);//todo: why 1


        Order order = client.orders.create(orderRequest);

        Payment payment = new Payment();


        //default is false
        payment.setRefundRequired(false);
        payment.setRefunded(false);

        payment.setAmount(BigDecimalUtil.divide(BigDecimal.valueOf(amount), new BigDecimal(100)));
        payment.setCurrency(currency);

        payment.setStatus(PaymentStatus.PENDING);
        payment.setPaymentType(PaymentType.ONLINE);

        payment.setRazorpayOrderId(order.get("id").toString());


        //web hook me karna hai
//        payment.setStatus(PaymentStatus.COMPLETED);
        ///todo: method type and currency 100 % lelo from the ui and use
//        payment.setPaymentMethod(PaymentMethodType.valueOf(method.toUpperCase()));


        //donot save in here only in the web hook to this if captured the events


        //set the payment
        // todo: app user why not inerting auto
        paymentRepository.save(payment);


        CreatePaymentResponse createPaymentResponse = new CreatePaymentResponse(payment.getRazorpayOrderId(), RAZORPAY_KEY_ID);


        return ResponseEntity.ok(createPaymentResponse);

    }


    @PostMapping("/payment-webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestHeader("X-Razorpay-Signature") String signature,
            @RequestBody String payload) {

        //log the webhook called
        logger.info("webhook called!!!!!!!");

        try {
            // Step 1: Verify Razorpay Signature
            if (!verifySignature(payload, signature, RAZORPAY_WEBHOOK_SECRET)) {
                logger.warn("the payload and signature and rzp webhook signature failed!!!");

                //todo: can mark as refund due to processing issue in server side after payment confirmed

                JSONObject json = new JSONObject(payload);

                JSONObject paymentEntity = json.getJSONObject("payload")
                        .getJSONObject("payment")
                        .getJSONObject("entity");

                String razorpayOrderId = paymentEntity.getString("order_id");

                //find the payment and set the refundable as true
                Payment payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId).orElse(null);

                if (payment == null) {
                    //log the error
                    log.error("payment not found to set as refund after the process failed in webhook after completed a payment!!!");
                } else {
                    payment.setRefundRequired(true);
                    //now update
                    paymentRepository.save(payment);
                }

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid signature");
            }


            // Step 2: Parse Webhook Event
            JSONObject json = new JSONObject(payload);

            String event = json.getString("event");

            //log the event

            JSONObject paymentEntity = json.getJSONObject("payload")
                    .getJSONObject("payment")
                    .getJSONObject("entity");

// 1. Extract payment entity details
            String razorpayPaymentId = paymentEntity.getString("id");       // payment unique id
            String razorpayOrderId = paymentEntity.getString("order_id");
            boolean captured = paymentEntity.getBoolean("captured");
            String methodDetail = paymentEntity.getString("method");
            String email = paymentEntity.optString("email", null);          // may be optional
            String contact = paymentEntity.optString("contact", null);

            String paymentStatusPayload = paymentEntity.optString("status", null);  // optional, you can parse it if needed

            //todo: first check the status in if else to do these stuffs

// 2. Fetch Payment by receipt or orderId
            Optional<Payment> optionalPayment = paymentRepository.findByRazorpayOrderId(razorpayOrderId);
            Payment payment = null;

            if (optionalPayment.isPresent()) {
                log.info("optional payment is present!!!");
                payment = optionalPayment.get();
            } else if (!optionalPayment.isPresent()) {
                log.info("opt payment not present in db");
                return ResponseEntity.ok("No matching payment found");
            }

// 3. Update Payment entity
            payment.setPaymentId(razorpayPaymentId);
            payment.setRazorpayOrderId(razorpayOrderId);//todo: not needed see above controller method
            payment.setCaptured(captured);
            payment.setMethodDetail(methodDetail);
            payment.setPaymentStatusPayload(paymentStatusPayload);
            if (email != null)
                payment.setEmail(email);

            payment.setContact(contact);

// Check duplicate
            if (event.equals(payment.getLastProcessedEvent())) {
                log.info("⏭️ Skipping duplicate event '{}' for paymentId: {}", event, payment.getPaymentId());
                return ResponseEntity.ok("Duplicate event ignored");
            }

            long createdAtUnix = paymentEntity.optLong("created_at", 0);
            Instant paymentTime = createdAtUnix > 0 ? Instant.ofEpochSecond(createdAtUnix) : null;


// Switch-case handling
            switch (event) {
                case "payment.failed":
                    log.error("❌ payment.failed event received");
                    payment.setStatus(PaymentStatus.FAILED);
                    break;

                case "order.paid":
                    log.info("📥 order.paid event received");

                    if (payment.getStatus() != PaymentStatus.COMPLETED) {
                        payment.setStatus(PaymentStatus.COMPLETED);


                    }

                    //setting paymnet method UPI etc using the enum also
                    if (methodDetail != null && !methodDetail.isEmpty()) {
                        try {
                            payment.setPaymentMethod(PaymentMethodType.valueOf(methodDetail.toUpperCase()));
                        } catch (IllegalArgumentException ex) {
                            log.warn("⚠️ Unknown payment method received: {}", methodDetail);
                        }
                    }

                    if (paymentTime != null) payment.setPaymentTime(paymentTime);
                    break;

                case "order.refund":
                    log.info("order refund by webhook");
                    //can set the refunded
                    //1. first find the entity
                    Payment payment1=paymentRepository.findByRazorpayOrderId(razorpayOrderId).orElseThrow(()->new RuntimeException("payment record not found with rzp order id: "+razorpayOrderId));

                    //now update the payment1
                    payment1.setRefunded(true);
                    payment.setStatus(PaymentStatus.REFUNDED);

                default:
                    log.warn("⚠️ Unknown event received: {}", event);
                    break;
            }
            // Track event
            payment.setLastProcessedEvent(event);
            // Save
            paymentRepository.save(payment);


            // 4. Save webhook event log -> create webhook event first
            WebhookEvent webhookEvent = new WebhookEvent();
            PaymentEventType eventType;

            try {
                eventType = PaymentEventType.valueOf(event.toUpperCase().replace('.', '_'));
            } catch (IllegalArgumentException ex) {
                log.warn("⚠️ Unknown event type received: {}", event);
                eventType = PaymentEventType.UNKNOWN;
            }

            //set data in the webhook event
            webhookEvent.setEventType(eventType);
            webhookEvent.setProcessingStatus(WebhookProcessingStatus.PROCESSED);

            if (paymentEntity.has("id")) {
                webhookEvent.setRazorpayEventId(paymentEntity.getString("id"));
            } else {
                webhookEvent.setRazorpayEventId(UUID.randomUUID().toString()); // fallback ID if missing
                log.warn("⚠️ Razorpay webhook 'id' field missing, using fallback ID");
            }

            webhookEvent.setPayloadJson(payload);
            webhookEvent.setPayment(payment);

            //option but best: set the created_at as
            //set the received at
            webhookEvent.setReceivedAt(paymentTime != null ? paymentTime : Instant.now());
        //same as just above not needed in the payment for each due to there's event webhook have aolready this detail


            payment.getWebhookEvents().add(webhookEvent);
            paymentRepository.save(payment);

            return ResponseEntity.ok("✅ Webhook processed");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    private boolean verifySignature(String payload, String actualSignature, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);

        byte[] computedHash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        String expectedSignature = Hex.encodeHexString(computedHash); // HEX encoding (Razorpay uses this)

        // Use constant-time comparison for security
        return MessageDigest.isEqual(expectedSignature.getBytes(), actualSignature.getBytes());
    }

}