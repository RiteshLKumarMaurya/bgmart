package com.biharigraphic.jilamart.auth.service;

import com.biharigraphic.jilamart.auth.dto.request.GoogleLoginRequest;
import com.biharigraphic.jilamart.auth.dto.response.TokenResponse;
import com.biharigraphic.jilamart.address.entity.Address;
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
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {

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

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String googleId = payload.getSubject();
                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String picture = (String) payload.get("picture");

                User user = userRepository.findByGoogleId(googleId).orElse(null);

                if (user == null) {
                    // Create new user (first-time login)
                    user = new User();

                    user.setFullName(name);
                    user.setEmailId(email);
                    List<Address> addresses = new ArrayList<Address>();
                    user.setAddresses(addresses);

                    user.setGoogleId(googleId);
                    user.setEmailId(email);

                    String username = myCodeGenerator.generateUniqueUsername(name);
                    String password = myCodeGenerator.generateStrongPassword(PASSWORD_LENGTH);

                    user.setUsername(username);
                    user.setPassword(password);
                    user.setProfilePictureUrl(picture);
                    user.setCreatedAt(Instant.now());

                    Role role1 = roleRepository.findByName(RoleName.valueOf(request.getRole()))
                            .orElseThrow(() -> new RoleNotFoundException("role not found: " + request.getRole()));
                    user.setRole(role1);


                } else {
                    // Existing user
                    if (!user.getRole().getName().name().equals(request.getRole())) {
                        // Role mismatch: send clear error

                        TokenResponse response=new TokenResponse();
                        response.setErrorCode("ROLE_MISMATCH");
                        response.setErrorMessage("This Google account is already registered with a different role ("
                                + user.getRole().getName() + "). Use the correct app or use a different google account.");
                        return response;
                    }
                }

                String accessToken = jwtProvider.generateAccessToken(user);
                String refreshToken = jwtProvider.generateRefreshToken(user);
                user.setAccessToken(accessToken);
                user.setRefreshToken(refreshToken);

                //set wallet at first time
                walletService.addCoins(user, 0L, "Wallet Created");

                userRepository.save(user);

                TokenResponse response=new TokenResponse();
                response.setAccessToken(accessToken);
                response.setRefreshToken(refreshToken);

                return response;
            } else {
                TokenResponse response=new TokenResponse();
                response.setErrorCode("INVALID_TOKEN");
                response.setErrorMessage("Invalid Google ID token");
                return response;
            }

        } catch (Exception e) {
            TokenResponse response=new TokenResponse();
            response.setErrorCode("ERROR");
            response.setErrorMessage("Google login failed: " + e.getMessage());
            return response;
        }
    }

}
