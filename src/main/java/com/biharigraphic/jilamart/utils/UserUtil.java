package com.biharigraphic.jilamart.utils;

import com.biharigraphic.jilamart.security.JwtUtil;
import com.biharigraphic.jilamart.user.entity.User;
import com.biharigraphic.jilamart.user.exception.UserException;
import com.biharigraphic.jilamart.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtil {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    /// util to common use case
    public User getUserFromTheJwt(String authHeader){

        User user = new User();

        // ✅ Clean the JWT
        String jwt = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7).trim(); // remove "Bearer " and trim spaces
        } else {
            throw new RuntimeException("Invalid Authorization header");
        }

        // ✅ Extract username
        String username = jwtUtil.extractUsername(jwt);

        if (username != null) {
            user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UserException("User not found with username: " + username));
        }
        return user;
    }
}
