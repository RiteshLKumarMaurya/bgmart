package com.biharigraphic.jilamart.role.dto;

import com.biharigraphic.jilamart.role.enums.RoleName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleResponseDTO {
    private Long id;
    private RoleName name;
}