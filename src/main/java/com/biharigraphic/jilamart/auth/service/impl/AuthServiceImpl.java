package com.biharigraphic.jilamart.auth.service.impl;

import com.biharigraphic.jilamart.auth.dto.*;
import com.biharigraphic.jilamart.auth.dto.request.ChangePasswordRequest;
import com.biharigraphic.jilamart.auth.dto.request.ChangeUsernameRequest;
import com.biharigraphic.jilamart.auth.dto.request.LoginRequest;
import com.biharigraphic.jilamart.auth.dto.request.RegisterRequest;
import com.biharigraphic.jilamart.auth.dto.response.ChangePasswordResponse;
import com.biharigraphic.jilamart.auth.dto.response.ChangeUsernameResponse;
import com.biharigraphic.jilamart.auth.dto.response.RefreshTokenResponse;
import com.biharigraphic.jilamart.auth.dto.response.TokenResponse;
import com.biharigraphic.jilamart.auth.enums.SignInProvider;
import com.biharigraphic.jilamart.address.entity.Address;
import com.biharigraphic.jilamart.auth.service.AuthService;
import com.biharigraphic.jilamart.auth.service.EmailService;
import com.biharigraphic.jilamart.cart.entity.Cart;
import com.biharigraphic.jilamart.cart.repo.CartRepository;
import com.biharigraphic.jilamart.cart.service.CartService;
import com.biharigraphic.jilamart.enums.TokenType;
import com.biharigraphic.jilamart.exception.InvalidOrExpiredTokenException;
import com.biharigraphic.jilamart.exception.PhoneNumberNotFoundException;
import com.biharigraphic.jilamart.exception.enums.ErrorCode;
import com.biharigraphic.jilamart.exception.otp.OtpException;
import com.biharigraphic.jilamart.exception.password.WrongPasswordEnteredException;
import com.biharigraphic.jilamart.exception.role.RoleNotFoundException;
import com.biharigraphic.jilamart.exception.user.*;
import com.biharigraphic.jilamart.notification.service.DeviceTokenService;
import com.biharigraphic.jilamart.notification.service.NotificationService;
import com.biharigraphic.jilamart.otp.entity.Otp;
import com.biharigraphic.jilamart.otp.repo.OtpRepository;
import com.biharigraphic.jilamart.role.entity.Role;
import com.biharigraphic.jilamart.role.enums.RoleName;
import com.biharigraphic.jilamart.role.repository.RoleRepository;
import com.biharigraphic.jilamart.security.JwtUtil;
import com.biharigraphic.jilamart.user.entity.User;
import com.biharigraphic.jilamart.user.exception.UserException;
import com.biharigraphic.jilamart.user.mapper.impl.UserMapperImpl;
import com.biharigraphic.jilamart.user.repository.UserRepository;
import com.biharigraphic.jilamart.utils.UserUtil;
import com.biharigraphic.jilamart.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final WalletService walletService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RoleRepository roleRepository;
    private final UserMapperImpl userMapper;
    private CartService cartService;
    private final CartRepository cartRepository;

    private final DeviceTokenService deviceTokenService;

    private final NotificationService notificationService;

    private final OtpRepository otpRepository;
    private final EmailService emailService;
    private final UserUtil userUtil;


    private final long accessTokenExpiry = 1000 * 60 * 30; // 30 minutes
    private final long refreshTokenExpiry = 1000 * 60 * 60 * 24 * 28; // 28 days


    // Register a new user
    public PhonePasswordRegisterResponse register(RegisterRequest request) {
        if (userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new UserPhoneAlreadyRegistered(request.getPhoneNumber());
        }

        if (userRepository.findByEmailId(request.getEmailId()).isPresent()) {
            throw new UserException("user already registered with this email: " + request.getEmailId(), ErrorCode.EMAIL_ALREADY_REGISTERED.name());
        }

        // Assign role
        //default as admin
        Role role = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new RoleNotFoundException("ROLE_USER"));
        User user = User.builder().phoneNumber(request.getPhoneNumber()).fullName(request.getFullName()).emailId(request.getEmailId()).password(passwordEncoder.encode(request.getPassword())).phoneNumber(request.getPhoneNumber()).role(role).build();


        List<Address> addresses = new ArrayList<>();
        user.setAddresses(addresses);

        user.setProvider(SignInProvider.PHONE_PASSWORD);


        // ✅ FIRST SAVE USER
        User savedUser = userRepository.save(user);

        // ✅ THEN USE IT
        walletService.addCoins(savedUser, 0L, "Wallet Created");

        //Cart bhi bana lo and user ko set kr do
        Cart cart = new Cart();
        cart.setCartItems(new ArrayList<>());
        cart.setUser(savedUser);

        //now save the cart
        cartRepository.save(cart);

        //set the provider

        //now do log-in
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword(request.getPassword());
        loginRequest.setPhoneNumber(request.getPhoneNumber());

        if (request.getFcmToken() != null) {
            loginRequest.setFcmToken(request.getFcmToken());
        }

        if (request.getDevice() != null) {
            loginRequest.setDevice(request.getDevice());
        }


        PhonePasswordLoginResponse response = login(loginRequest);
        return new PhonePasswordRegisterResponse(response.getUserResponse(), response.getTokenResponse());
    }

    // Login user
    public PhonePasswordLoginResponse login(LoginRequest request) {
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber()).orElseThrow(() -> new PhoneNumberNotFoundException("user not found with phone number: " + request.getPhoneNumber(), ErrorCode.PHONE_NUMBER_NOT_FOUND.name()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new WrongPasswordEnteredException("Wrong password");
        }


        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        user.setAccessToken(accessToken);
        user.setRefreshToken(refreshToken);

        userRepository.save(user);

        TokenResponse tokenResponse = TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).accessTokenExpiry(System.currentTimeMillis() + accessTokenExpiry).refreshTokenExpiry(System.currentTimeMillis() + refreshTokenExpiry).build();


        if (request.getFcmToken() != null) {

            //save or update device token
            deviceTokenService.saveOrUpdate(user, request.getFcmToken(), request.getDevice());

            notificationService.notifyLogin(user, request.getFcmToken());
        }


        return new PhonePasswordLoginResponse(userMapper.mapToUserResponse(user), tokenResponse);

    }

    // Validate token
    public boolean validate(String token, TokenType type) {
        return jwtUtil.isTokenValid(token,type);
    }

    // Refresh token
    public RefreshTokenResponse refresh(String refreshToken) {

        if (jwtUtil.isRefreshTokenValid(refreshToken,TokenType.REFRESH_TOKEN)) {
            String username = jwtUtil.extractUsername(refreshToken);

            User user = userRepository.findByPhoneNumber(username)
                    .orElseThrow(() -> new PhoneNumberNotFoundException(
                            "user not registered with phone number: " + username,
                            ErrorCode.PHONE_NUMBER_NOT_FOUND.name()
                    ));

            // 🔥 ADD THIS (VERY IMPORTANT)
            if (!refreshToken.equals(user.getRefreshToken())) {
                throw new InvalidOrExpiredTokenException("Invalid refresh token");
            }

            String newAccessToken = jwtUtil.generateAccessToken(user);

            return new RefreshTokenResponse(newAccessToken);

        }

        throw new InvalidOrExpiredTokenException("refresh token is invalid or expired~");

/**       log.info("refresh token in refresh(refreshToken): {}", refreshToken);
        String phoneNumber = jwtUtil.extractUsername(refreshToken);**/


    }

    // Change username
    public ChangeUsernameResponse changeUsername(ChangeUsernameRequest request) {
        User user = userRepository.findByUsername(request.getCurrentUsername()).orElse(null);
        if (user == null) throw new UserNotFoundForUsernameException(request.getCurrentUsername());
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new WrongPasswordEnteredException(request.getPassword());

        //now check already avail the any user with this username or not
        if (userRepository.existsByUsername(request.getNewUsername())) {
            throw new UsernameAlreadyExistsException(request.getNewUsername());
        }

        user.setUsername(request.getNewUsername());
        userRepository.save(user);

        return new ChangeUsernameResponse(true, "Username changed successfully.");
    }

    // Change password
    public ChangePasswordResponse changePassword(ChangePasswordRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);
        if (user == null) throw new WrongPasswordEnteredException(request.getCurrentPassword());
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword()))
            throw new WrongPasswordEnteredException(request.getCurrentPassword());

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return new ChangePasswordResponse(true, "Password changed successfully.");
    }

    public ChangePhoneNumberResponse changePhoneNumber(ChangePhoneNumberRequest request) {
        User user = userRepository.findByPhoneNumber(request.getCurrentPhoneNumber()).orElseThrow(() -> new PhoneNumberNotFoundException("user not found with phone: " + request.getCurrentPhoneNumber(), ErrorCode.PHONE_NUMBER_NOT_FOUND.name()));

        //now check that already that new phone number is in use or not
        if (userRepository.existsByPhoneNumber(request.getNewPhoneNumber())) {
            throw new UserPhoneAlreadyRegistered("phone number :" + request.getNewPhoneNumber() + " already in use.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new WrongPasswordEnteredException("Wrong password");
        }

        //now we need to set new phone number
        user.setPhoneNumber(request.getNewPhoneNumber());

        User newUser = userRepository.save(user);

        return new ChangePhoneNumberResponse(newUser.getPhoneNumber(), "user phone number updated successfully !");
    }

    public ChangePasswordWithPhoneResponse changePassword(ChangePasswordWithPhoneRequest request) {
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber()).orElseThrow(() -> new PhoneNumberNotFoundException("user not found with phone number: ", request.getPhoneNumber()));
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new WrongPasswordEnteredException("current password is not correct");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        return new ChangePasswordWithPhoneResponse("password updated successfully!");
    }

    @Override
    public ForgetPasswordResponse forgetPassword(ForgetPasswordRequest request) {

        //first check the coming phone number and the email are same user or not
        if (!userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new PhoneNumberNotFoundException("user does not exists with phone: " + request.getPhoneNumber(), ErrorCode.PHONE_NUMBER_NOT_FOUND.name());
        }
        if (!userRepository.existsByEmailId(request.getEmailId())) {
            throw new UserException("user not exists with email: " + request.getEmailId(), ErrorCode.EMAIL_NOT_REGISTERED.name());
        } //now find the user by phone and match the both thin
        
        User user = userRepository.findByPhoneNumberAndEmailId(request.getPhoneNumber(), request.getEmailId()).orElseThrow(() -> new UserException("User phone and email both are not of the same user!", ErrorCode.PHONE_EMAIL_NOT_OF_USER.name()));


        // generate OTP
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);


        Otp otpEntity = new Otp();
        otpEntity.setEmail(user.getEmailId());
        otpEntity.setOtp(otp);
        otpEntity.setExpiryTime(Instant.now().plusSeconds(300)); // 5 min

        otpRepository.save(otpEntity);

        // send email
        log.info("Before sending email");

        emailService.sendOtp(user.getEmailId(), otp);

        log.info("After sending email");

        return new ForgetPasswordResponse("OTP sent successfully");
    }

    @Override
    public VerifyOtpResponse verifyOtp(VerifyOtpRequest request) {

//        userRepository.findByPhoneNumberAndEmailId(request.getPhoneNumber(), request.getEmailId()).orElseThrow(() -> new UserException("Invalid phone or email"));

        //first check the coming phone number and the email are same user or not
        if (!userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new PhoneNumberNotFoundException("user does not exists with phone: " + request.getPhoneNumber(), ErrorCode.PHONE_NUMBER_NOT_FOUND.name());
        }
        if (!userRepository.existsByEmailId(request.getPhoneNumber())) {
            throw new UserException("user not exists with email: " + request.getEmailId(), ErrorCode.EMAIL_NOT_REGISTERED.name());
        } //now find the user by phone and match the both thin

        User user = userRepository.findByPhoneNumberAndEmailId(request.getPhoneNumber(), request.getEmailId()).orElseThrow(() -> new UserException("User phone and email both are not of the same user!", ErrorCode.PHONE_EMAIL_NOT_OF_USER.name()));



        //now we need to now check exists the otp and email or not in otp record

        Otp otp = otpRepository.findByEmailAndOtp(request.getEmailId(), request.getOtp()).orElseThrow(() -> new OtpException("Incorrect otp: " + request.getOtp(), ErrorCode.INCORRECT_OTP.name()));

        //now we just need lastly is read or not
        if (otp.isUsed()) {
            throw new OtpException("Otp already used", ErrorCode.USED_OTP.name());
        }


        if (otp.getExpiryTime().compareTo(Instant.now()) < 0) {
            throw new OtpException("OTP expired", ErrorCode.EXPIRED_OTP.name());
        }

        //now we can do  verify it
        otp.setUsed(true);

        return new VerifyOtpResponse("Otp verification success!");
    }


}
