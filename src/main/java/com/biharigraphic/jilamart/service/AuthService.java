package com.biharigraphic.jilamart.service;

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
import com.biharigraphic.jilamart.exception.InvalidOrExpiredTokenException;
import com.biharigraphic.jilamart.exception.password.WrongPasswordEnteredException;
import com.biharigraphic.jilamart.exception.role.RoleNotFoundException;
import com.biharigraphic.jilamart.exception.user.*;
import com.biharigraphic.jilamart.role.entity.Role;
import com.biharigraphic.jilamart.role.enums.RoleName;
import com.biharigraphic.jilamart.role.repository.RoleRepository;
import com.biharigraphic.jilamart.security.JwtUtil;
import com.biharigraphic.jilamart.user.entity.User;
import com.biharigraphic.jilamart.user.exception.UserException;
import com.biharigraphic.jilamart.user.repository.UserRepository;
import com.biharigraphic.jilamart.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final WalletService walletService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RoleRepository roleRepository;


    // Register a new user
    public void register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException(request.getUsername());
        } else {
            log.info("username is new so we accept for register as new user !");
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber()))
            throw (new UserPhoneAlreadyRegistered());

        // Assign role
        //default as admin
        Role role = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RoleNotFoundException("ROLE_USER"));
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(role)
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
                .orElseThrow(() -> new UsernameNotFoundException(request.getUsername()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new WrongPasswordEnteredException("Invalid password");
        }


        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        user.setAccessToken(accessToken);
        user.setRefreshToken(refreshToken);

        userRepository.save(user);

        TokenResponse response = new TokenResponse();
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
            throw new InvalidOrExpiredTokenException();
        }

        String username = jwtUtil.extractUsername(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundForUsernameException(username));

        String newAccessToken = jwtUtil.generateAccessToken(user);

        return new RefreshTokenResponse(newAccessToken);
    }

    // Change username
    public ChangeUsernameResponse changeUsername(ChangeUsernameRequest request) {
        User user = userRepository.findByUsername(request.getCurrentUsername()).orElse(null);
        if (user == null) throw new UserNotFoundForUsernameException(request.getCurrentUsername());
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new WrongPasswordEnteredException(request.getPassword());

        //now check already avail the any user with this username or not
        if(userRepository.existsByUsername(request.getNewUsername())){
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

}
