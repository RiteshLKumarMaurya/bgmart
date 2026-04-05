package com.biharigraphic.jilamart.service;

import com.biharigraphic.jilamart.UsernameAlreadyExistsException;
import com.biharigraphic.jilamart.auth.dto.request.ChangePasswordRequest;
import com.biharigraphic.jilamart.auth.dto.request.ChangeUsernameRequest;
import com.biharigraphic.jilamart.auth.dto.request.LoginRequest;
import com.biharigraphic.jilamart.auth.dto.request.RegisterRequest;
import com.biharigraphic.jilamart.auth.dto.response.ChangePasswordResponse;
import com.biharigraphic.jilamart.auth.dto.response.ChangeUsernameResponse;
import com.biharigraphic.jilamart.auth.dto.response.RefreshTokenResponse;
import com.biharigraphic.jilamart.auth.dto.response.TokenResponse;
import com.biharigraphic.jilamart.auth.enums.SignInProvider;
import com.biharigraphic.jilamart.auth.exception_handler.AuthExceptionHandler;
import com.biharigraphic.jilamart.address.entity.Address;
import com.biharigraphic.jilamart.role.entity.Role;
import com.biharigraphic.jilamart.role.enums.RoleName;
import com.biharigraphic.jilamart.role.repository.RoleRepository;
import com.biharigraphic.jilamart.security.JwtUtil;
import com.biharigraphic.jilamart.user.entity.User;
import com.biharigraphic.jilamart.user.exception.UserException;
import com.biharigraphic.jilamart.user.repository.UserRepository;
import com.biharigraphic.jilamart.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final WalletService walletService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RoleRepository roleRepository;

    private final AuthExceptionHandler authExceptionHandler;


    // Register a new user
    public void register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            authExceptionHandler.handleUsernameExists(new UsernameAlreadyExistsException("Username already exists"));
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber()))
            authExceptionHandler.handleUserException(new UserException("user already registered with given phone number!"));


        // Assign role
        Role userRole;
        if (request.getRoleName() == null || request.getRoleName().isEmpty()) {
            userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseGet(() -> {
                        authExceptionHandler.handleRuntime(new RuntimeException("Default role not found"));
                        return null; // or throw after handling
                    });
        } else {
            userRole = roleRepository.findByName(RoleName.valueOf(request.getRoleName()))
                    .orElseGet(() -> {
                        authExceptionHandler.handleRuntime(new RuntimeException("Role not found: " + request.getRoleName()));
                        return null;
                    });
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(userRole)
                .build();

        List<Address> addresses = new ArrayList<>();
        user.setAddresses(addresses);

// ✅ FIRST SAVE USER
        User savedUser = userRepository.save(user);

// ✅ THEN USE IT
        walletService.addCoins(savedUser, 0L, "Wallet Created");


        //set the provider
        user.setProvider(SignInProvider.USERNAME_PASSWORD);

    }

    // Login user
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username found!!!"));

        if ( !passwordEncoder.matches(request.getPassword() , user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // Check role
        Role requestRole = roleRepository.findByName(RoleName.valueOf(request.getRole()))
                .orElseThrow(() -> new RuntimeException("Role not found: " + request.getRole()));

        if (!user.getRole().equals(requestRole)) {
            throw new UserException("User role not matched");
        }

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        user.setAccessToken(accessToken);
        user.setRefreshToken(refreshToken);

        userRepository.save(user);

        TokenResponse response=new TokenResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        return response;
    }

    // Validate token
    public boolean validate(String accessToken) {
        return jwtUtil.isTokenValid(accessToken);
    }

    // Refresh token
    public RefreshTokenResponse refresh(String refreshToken) {
        if (!jwtUtil.isTokenValid(refreshToken)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        String username = jwtUtil.extractUsername(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String role = jwtUtil.extractRole(refreshToken); // single role
        String newAccessToken = jwtUtil.generateAccessToken(user);

        return new RefreshTokenResponse(newAccessToken);
    }

    // Change username
    public ChangeUsernameResponse changeUsername(ChangeUsernameRequest request) {
        User user = userRepository.findByUsername(request.getCurrentUsername()).orElse(null);
        if (user == null) return new ChangeUsernameResponse(false, "Username not found.");
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            return new ChangeUsernameResponse(false, "Incorrect password.");

        user.setUsername(request.getNewUsername());
        userRepository.save(user);

        return new ChangeUsernameResponse(true, "Username changed successfully.");
    }

    // Change password
    public ChangePasswordResponse changePassword(ChangePasswordRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);
        if (user == null) return new ChangePasswordResponse(false, "Username not found.");
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword()))
            return new ChangePasswordResponse(false, "Incorrect current password.");

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return new ChangePasswordResponse(true, "Password changed successfully.");
    }

}
