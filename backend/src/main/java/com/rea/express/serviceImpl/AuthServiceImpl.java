package com.rea.express.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.rea.express.JWT.CustomerUserDetailsService;
import com.rea.express.JWT.JwtUtil;
import com.rea.express.POJO.User;
import com.rea.express.dao.UserDao;
import com.rea.express.service.AuthService;
import com.rea.express.wrapper.LoginResponse;
import com.rea.express.wrapper.UserWrapper;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomerUserDetailsService customerUserDetailsService;
    private final JwtUtil jwtUtil;
    private final UserDao userDao;

    @Override
    public ResponseEntity<LoginResponse> login(Map<String, String> requestMap) {
        log.info("Login attempt for {}", requestMap.get("email"));
        try {
            if (!validateLoginMap(requestMap)) {
                return ResponseEntity.badRequest().build();
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestMap.get("email"),
                            requestMap.get("password")
                    )
            );

            User user = customerUserDetailsService.getUserDetails();
            if (Objects.isNull(user)) {
                user = userDao.findByEmailId(requestMap.get("email"));
            }

            if (Objects.isNull(user)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            if ("false".equalsIgnoreCase(user.getStatus())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            String role = user.getRoles().stream()
                    .findFirst()
                    .map(r -> r.getName().name())
                    .orElse("ROLE_USER");

            String token = jwtUtil.generateToken(user.getEmail(), role);

            LoginResponse response = LoginResponse.builder()
                    .token(token)
                    .type("Bearer")
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .contactNumber(user.getContactNumber())
                    .status(user.getStatus())
                    .roles(user.getRoles().stream()
                            .map(r -> r.getName().name())
                            .collect(Collectors.toSet()))
                    .build();

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException ex) {
            log.warn("Invalid credentials for {}", requestMap.get("email"));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception ex) {
            log.error("Login failed", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<UserWrapper> currentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String email = authentication.getName();
            User user = userDao.findByEmailId(email);
            if (Objects.isNull(user)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            return ResponseEntity.ok(UserWrapper.fromUser(user));
        } catch (Exception ex) {
            log.error("Failed to fetch current user", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private boolean validateLoginMap(Map<String, String> requestMap) {
        return requestMap != null
                && requestMap.containsKey("email")
                && requestMap.containsKey("password")
                && requestMap.get("email") != null
                && !requestMap.get("email").isBlank()
                && requestMap.get("password") != null
                && !requestMap.get("password").isBlank();
    }
}
