package com.biharigraphic.jilamart.profile.controller;


import com.biharigraphic.jilamart.exception.enums.ErrorCode;
import com.biharigraphic.jilamart.user.dto.request.UpdateProfileRequestDto;
import com.biharigraphic.jilamart.user.dto.response.UserResponse;
import com.biharigraphic.jilamart.user.entity.User;
import com.biharigraphic.jilamart.user.exception.UserException;
import com.biharigraphic.jilamart.user.mapper.impl.UserMapperImpl;
import com.biharigraphic.jilamart.user.repository.DeleteAccountRequestRepository;
import com.biharigraphic.jilamart.profile.service.ProfileService;
import com.biharigraphic.jilamart.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProfileController {

    private final ProfileService userService;
    private final UserMapperImpl userMapper;
    private final UserUtil userUtil;


    private final DeleteAccountRequestRepository deleteAccountRequestRepository;

    @GetMapping("/profile")
    public ResponseEntity<UserResponse>
    userProfile(@RequestHeader("Authorization") String jwt) {

        String token = jwt.replace("Bearer ", "");

        return ResponseEntity.ok(userService.getProfile(token));
    }


    /// this will be upgrade later to otp match then only update confirms
    @PostMapping("/update-profile")
    public ResponseEntity<UserResponse> updateProfile(
            @RequestHeader("Authorization") String jwt,
            @RequestBody UpdateProfileRequestDto request) {

        // ✅ Get user from JWT
        User user = userUtil.getUserFromTheJwt(jwt);

        if (user == null) {
           throw new UserException("User not found", ErrorCode.USER_NOT_FOUND.name());
        }
        return ResponseEntity.ok(userService.updateUserProfile(user, request));
    }

}
