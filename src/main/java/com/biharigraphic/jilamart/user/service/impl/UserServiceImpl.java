package com.biharigraphic.jilamart.user.service.impl;

import com.biharigraphic.jilamart.user.dto.request.UpdateProfileRequestDto;
import com.biharigraphic.jilamart.user.dto.response.UpdateProfileResponseDto;
import com.biharigraphic.jilamart.user.entity.User;
import com.biharigraphic.jilamart.user.mapper.impl.UserMapperImpl;
import com.biharigraphic.jilamart.user.repository.UserRepository;
import com.biharigraphic.jilamart.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapperImpl userMapper;



    @Override
    public UpdateProfileResponseDto updateUserProfile(User user, UpdateProfileRequestDto request) {
        // ✅ Update only provided fields
        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getEmailId() != null) user.setEmailId(request.getEmailId());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());

        // ✅ Save updated user
        userRepository.save(user);

        return new UpdateProfileResponseDto(true, "Profile updated successfully!");
    }
}