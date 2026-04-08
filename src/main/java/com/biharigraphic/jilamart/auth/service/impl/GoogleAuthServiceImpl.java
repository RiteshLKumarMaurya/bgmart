package com.biharigraphic.jilamart.auth.service.impl;

import com.biharigraphic.jilamart.auth.dto.request.GoogleLoginRequest;
import com.biharigraphic.jilamart.auth.dto.response.TokenResponse;
import com.biharigraphic.jilamart.auth.service.GoogleAuthService;
import com.biharigraphic.jilamart.exception.role.RoleNotFoundException;
import com.biharigraphic.jilamart.exception.token.InvalidGoogleIdTokenException;
import com.biharigraphic.jilamart.role.entity.Role;
import com.biharigraphic.jilamart.role.enums.RoleName;
import com.biharigraphic.jilamart.role.repository.RoleRepository;
import com.biharigraphic.jilamart.security.JwtUtil;
import com.biharigraphic.jilamart.user.entity.User;
import com.biharigraphic.jilamart.user.repository.UserRepository;
import com.biharigraphic.jilamart.utils.MyCodeGenerator;
import com.biharigraphic.jilamart.wallet.service.WalletService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleAuthServiceImpl implements GoogleAuthService {

    private final WalletService walletService;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtProvider; // your existing JWT generator

    private final MyCodeGenerator myCodeGenerator;

    private static final int PASSWORD_LENGTH=12;

    private static final String GOOGLE_CLIENT_ID = "995517987520-7eeqf0au1s38o0a969pmel7k0grunadg.apps.googleusercontent.com";

    public TokenResponse googleLogin(GoogleLoginRequest request) {
        String idTokenString = request.getIdToken();

        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    new GsonFactory()
            ).setAudience(Collections.singletonList(GOOGLE_CLIENT_ID)).build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken == null) {
                throw new InvalidGoogleIdTokenException();
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String googleId = payload.getSubject();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String picture = (String) payload.get("picture");

            User user = userRepository.findByGoogleId(googleId).orElse(null);

            if (user == null) {
                user = new User();

                user.setFullName(name);
                user.setEmailId(email);
                user.setGoogleId(googleId);
                user.setProfilePictureUrl(picture);
                user.setCreatedAt(Instant.now());
                user.setAddresses(new ArrayList<>());

                String username = myCodeGenerator.generateUniqueUsername(name);
                String password = myCodeGenerator.generateStrongPassword(PASSWORD_LENGTH);

                user.setUsername(username);
                user.setPassword(password);

                // default role
                Role role = roleRepository.findByName(RoleName.ROLE_USER)
                        .orElseThrow(() -> new RoleNotFoundException("ROLE_USER"));

                user.setRole(role);

                walletService.addCoins(user, 0L, "Wallet Created");
            }

            String accessToken = jwtProvider.generateAccessToken(user);
            String refreshToken = jwtProvider.generateRefreshToken(user);

            user.setAccessToken(accessToken);
            user.setRefreshToken(refreshToken);

            userRepository.save(user);

            TokenResponse response = new TokenResponse();
            response.setAccessToken(accessToken);
            response.setRefreshToken(refreshToken);

            return response;

        } catch (InvalidGoogleIdTokenException ex) {
            throw ex; // already custom → direct throw
        } catch (Exception e) {
            // wrap unknown error
            throw new InvalidGoogleIdTokenException();
        }
    }
}
