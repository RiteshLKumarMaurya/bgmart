package com.biharigraphic.jilamart.role.service;

import com.biharigraphic.jilamart.role.dto.RoleRequestDTO;
import com.biharigraphic.jilamart.role.dto.RoleResponseDTO;

import java.util.List;

public interface RoleService {

    RoleResponseDTO createRole(RoleRequestDTO request);

    List<RoleResponseDTO> getAllRoles();

    RoleResponseDTO getRoleById(Long id);

    RoleResponseDTO updateRole(Long id, RoleRequestDTO request);

    void deleteRole(Long id);
}