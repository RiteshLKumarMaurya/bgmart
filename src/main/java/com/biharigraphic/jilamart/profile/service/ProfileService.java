package com.biharigraphic.jilamart.profile.service;

import com.biharigraphic.jilamart.user.dto.request.UpdateProfileRequestDto;
import com.biharigraphic.jilamart.user.dto.response.UserResponse;
import com.biharigraphic.jilamart.user.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface ProfileService {

    public UserResponse updateUserProfile(User user, UpdateProfileRequestDto request);

    public UserResponse getProfile(String jwtToken);

}
