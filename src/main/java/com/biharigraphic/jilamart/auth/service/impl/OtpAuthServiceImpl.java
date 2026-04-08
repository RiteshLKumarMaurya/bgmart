package com.biharigraphic.jilamart.auth.service.impl;

import com.biharigraphic.jilamart.auth.enums.SignInProvider;
import com.biharigraphic.jilamart.auth.service.OtpAuthService;
import com.biharigraphic.jilamart.role.enums.RoleName;
import com.biharigraphic.jilamart.auth.dto.response.TokenResponse;
import com.biharigraphic.jilamart.role.entity.Role;
import com.biharigraphic.jilamart.role.repository.RoleRepository;
import com.biharigraphic.jilamart.security.JwtUtil;
import com.biharigraphic.jilamart.user.entity.User;
import com.biharigraphic.jilamart.user.repository.UserRepository;
import com.biharigraphic.jilamart.wallet.service.WalletService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class OtpAuthServiceImpl implements OtpAuthService {

    private final WalletService walletService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RoleRepository roleRepository;

    public TokenResponse loginWithOtp(String firebaseToken) {

        try {
            FirebaseToken decoded =
                    FirebaseAuth.getInstance().verifyIdToken(firebaseToken);

            // ✅ IMPORTANT FIX
            String phone = (String) decoded.getClaims().get("phone_number");




            if (phone == null) {
                throw new RuntimeException("Phone not found");
            }

            User user = userRepository.findByPhoneNumber(phone).orElse(null);



            if (user == null) {
                // 🆕 New user
                user = new User();
                user.setPhoneNumber(phone);
                user.setUsername("user_" + System.currentTimeMillis());
                user.setCreatedAt(Instant.now());

                Role role = roleRepository.findByName(RoleName.ROLE_USER)
                        .orElseThrow();

                user.setRole(role);

                //TODO: i will be also save these info in the user entity -> then save
                String email=decoded.getEmail();
                String imgUrl=decoded.getPicture();
                boolean isEmailVerified=decoded.isEmailVerified();
                String name=decoded.getName();

                //NOW I WANT TO set the provider over here
                user.setProvider(SignInProvider.PHONE_OTP);

                //set wallet as well

                walletService.addCoins(user, 0L, "Wallet Created");


                userRepository.save(user);
            }




            // 🔐 JWT
            String accessToken = jwtUtil.generateAccessToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);

            user.setAccessToken(accessToken);
            user.setRefreshToken(refreshToken);

            userRepository.save(user);

            TokenResponse res = new TokenResponse();
            res.setAccessToken(accessToken);
            res.setRefreshToken(refreshToken);

            return res;

        } catch (Exception e) {
            throw new RuntimeException("OTP login failed: " + e.getMessage());
        }
    }
}