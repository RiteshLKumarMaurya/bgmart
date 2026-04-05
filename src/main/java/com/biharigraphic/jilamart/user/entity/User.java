package com.biharigraphic.jilamart.user.entity;

import com.biharigraphic.jilamart.address.entity.Address;
import com.biharigraphic.jilamart.auth.enums.SignInProvider;
import com.biharigraphic.jilamart.entity.BaseEntity;
import com.biharigraphic.jilamart.role.entity.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"password", "services", "profile"})
@Table(name = "app_user")
public class User extends BaseEntity implements UserDetails {

    @Column(unique = true)
    private String googleId;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    private String fullName;//profile info

    private String phoneNumber;//profile info
    private String profilePictureUrl;//profile info

    @Column(unique = true, nullable = true)
    private String emailId;//profile info

    @Column(length = 1024)
    private String accessToken;

    @Column(length = 1024)
    private String refreshToken;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;//RoleName-> //profile info

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private SignInProvider provider; // GOOGLE / OTP


    private String fcmToken;

    /* ---------- Spring Security UserDetails ---------- */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority(role.getName().name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
