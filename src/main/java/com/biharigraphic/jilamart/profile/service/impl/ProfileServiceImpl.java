package com.biharigraphic.jilamart.profile.service.impl;

import com.biharigraphic.jilamart.exception.enums.ErrorCode;
import com.biharigraphic.jilamart.user.dto.request.UpdateProfileRequestDto;
import com.biharigraphic.jilamart.user.dto.response.UserResponse;
import com.biharigraphic.jilamart.user.entity.User;
import com.biharigraphic.jilamart.user.exception.UserException;
import com.biharigraphic.jilamart.user.mapper.impl.UserMapperImpl;
import com.biharigraphic.jilamart.user.repository.UserRepository;
import com.biharigraphic.jilamart.profile.service.ProfileService;
import com.biharigraphic.jilamart.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final UserMapperImpl userMapper;

    private final UserUtil userUtil;


    @Override
    public UserResponse updateUserProfile(User user, UpdateProfileRequestDto request) {
        // ✅ Update only provided fields
        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getEmailId() != null) user.setEmailId(request.getEmailId());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());

        // ✅ Save updated user
        userRepository.save(user);

        return userMapper.mapToUserResponse(user);
    }

    @Override
    public UserResponse getProfile(String jwtToken) {
        User user = userUtil.getUserFromTheJwt(jwtToken);

        if (user == null) {
            throw new UserException("User not found ", ErrorCode.USER_NOT_FOUND.name());
        }

        UserResponse userResponse = userMapper.mapToUserResponse(user);
        return userResponse;
    }
}