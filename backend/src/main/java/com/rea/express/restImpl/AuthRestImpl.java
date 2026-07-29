package com.rea.express.restImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.rea.express.rest.AuthRest;
import com.rea.express.service.AuthService;
import com.rea.express.wrapper.LoginResponse;
import com.rea.express.wrapper.UserWrapper;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthRestImpl implements AuthRest {

    private final AuthService authService;

    @Override
    public ResponseEntity<LoginResponse> login(Map<String, String> requestMap) {
        return authService.login(requestMap);
    }

    @Override
    public ResponseEntity<UserWrapper> currentUser() {
        try {
            return authService.currentUser();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
