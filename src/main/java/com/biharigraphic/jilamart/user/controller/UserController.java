package com.biharigraphic.jilamart.user.controller;


import com.biharigraphic.jilamart.user.dto.request.UpdateProfileRequestDto;
import com.biharigraphic.jilamart.user.dto.response.UpdateProfileResponseDto;
import com.biharigraphic.jilamart.user.dto.response.UserResponse;
import com.biharigraphic.jilamart.user.entity.User;
import com.biharigraphic.jilamart.user.exception.UserException;
import com.biharigraphic.jilamart.user.mapper.impl.UserMapperImpl;
import com.biharigraphic.jilamart.user.repository.DeleteAccountRequestRepository;
import com.biharigraphic.jilamart.user.service.UserService;
import com.biharigraphic.jilamart.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService userService;
    private final UserMapperImpl userMapper;
    private final UserUtil userUtil;
    

    private final DeleteAccountRequestRepository deleteAccountRequestRepository;
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> userProfile(@RequestHeader("Authorization") String jwt) {

        String token = jwt.replace("Bearer ", "");
        User user = userUtil.getUserFromTheJwt(token);

        if (user == null) {
            throw new UserException("User not found");
        }

        UserResponse userResponse = userMapper.mapToUserResponse(user);
        return ResponseEntity.ok(userResponse);
    }
    

    /// this will be upgarade later to otp match then only update confirms
    @PostMapping("/update-profile")
    public ResponseEntity<UpdateProfileResponseDto> updateProfile(
            @RequestHeader("Authorization") String jwt,
            @RequestBody UpdateProfileRequestDto request) {

        // ✅ Get user from JWT
        User user = userUtil.getUserFromTheJwt(jwt);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new UpdateProfileResponseDto(false, "User or profile not found."));
        }
        return ResponseEntity.ok(userService.updateUserProfile(user,request));
    }
    
}
