package com.biharigraphic.jilamart.user.repository;

import com.biharigraphic.jilamart.role.entity.Role;
import com.biharigraphic.jilamart.role.enums.RoleName;
import com.biharigraphic.jilamart.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByPhoneNumber(String phoneNumber);
    Boolean existsByPhoneNumber(String phoneNumber);



    boolean existsByUsername(String username);
    boolean existsByGoogleId(String googleId);
    Optional<User> findByGoogleId(String googleId);

    // ✅ Paginated users ordered by creation date (newest first)
    Page<User> findAllByOrderByCreatedAtDesc(Pageable pageable);


    Page<User> findAllByRole_Name(RoleName roleName, Pageable pageable);


    List<User> findByRole_Name(RoleName roleName);



}
