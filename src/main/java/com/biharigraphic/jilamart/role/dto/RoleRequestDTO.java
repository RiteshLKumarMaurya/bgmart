package com.biharigraphic.jilamart.role.dto;

import com.biharigraphic.jilamart.role.enums.RoleName;
import lombok.Data;

@Data
public class RoleRequestDTO {
    private RoleName name;
}