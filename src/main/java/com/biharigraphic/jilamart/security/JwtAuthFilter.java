package com.biharigraphic.jilamart.security;

import com.biharigraphic.jilamart.enums.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {


        final String authHeader = request.getHeader("Authorization");
        log.info("authorization{}", authHeader);

        // ❌ No token → just continue
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ Extract token properly
        String token = authHeader.substring(7).trim();


        // ❌ Invalid token → skip auth
        if (!jwtUtil.isTokenValid(token, TokenType.valueOf(jwtUtil.extractType(token)))) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ Extract data
        String phoneNumber = jwtUtil.extractUsername(token);
        String role = jwtUtil.extractRole(token);

        // ✅ Authority
        var authorities = Collections.singletonList(
                new SimpleGrantedAuthority(role)
        );

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        phoneNumber,
                        null,
                        authorities
                );

        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // ✅ Set authentication
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }
}
