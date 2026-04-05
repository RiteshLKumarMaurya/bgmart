package com.biharigraphic.jilamart.user.service;

import com.biharigraphic.jilamart.role.enums.RoleName;
import com.biharigraphic.jilamart.user.dto.request.UpdateProfileRequestDto;
import com.biharigraphic.jilamart.user.dto.response.UpdateProfileResponseDto;
import com.biharigraphic.jilamart.user.dto.response.UserResponseDto;
import com.biharigraphic.jilamart.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public UpdateProfileResponseDto updateUserProfile(User user, UpdateProfileRequestDto request);


}
