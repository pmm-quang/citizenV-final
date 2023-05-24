package com.citizenv.app.secirity;

import com.citizenv.app.entity.AdministrativeDivision;
import net.bytebuddy.implementation.bytecode.Division;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.stream.Stream;

public final class SecurityUtils {
    private SecurityUtils() {};

    public static Optional<String> getCurrentUserLogin(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetail springSecurityUser = (CustomUserDetail) authentication.getPrincipal();
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            return (String) authentication.getPrincipal();
        }
        return null;
    }

    public static Optional<Long> getIdCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Long userId = 0L;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetail springSecurityUser = (CustomUserDetail) authentication.getPrincipal();
            userId = springSecurityUser.getUser().getId();
        }
        return Optional.ofNullable(userId);
    }
    public static String getUsernameCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String username = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetail springSecurityUser = (CustomUserDetail) authentication.getPrincipal();
            username = springSecurityUser.getUsername();
        }
        return username;
    }

    public static String getDivisionCodeCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String divisionCode = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetail springSecurityUser = (CustomUserDetail) authentication.getPrincipal();
            AdministrativeDivision division = springSecurityUser.getUser().getDivision();
            if (division != null) {
                divisionCode = division.getCode();
            }
        }
        return divisionCode;
    }

    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional
                .ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getCredentials() instanceof String)
                .map(authentication -> (String) authentication.getCredentials());
    }

    private static Stream<String> getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority);
    }
}
