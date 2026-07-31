package com.rea.express.restImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.rea.express.dto.ForgotPasswordRequest;
import com.rea.express.dto.GithubLoginRequest;
import com.rea.express.dto.GoogleLoginRequest;
import com.rea.express.dto.ResetPasswordRequest;
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
    public ResponseEntity<LoginResponse> googleLogin(GoogleLoginRequest request) {
        return authService.googleLogin(request);
    }

    @Override
    public ResponseEntity<LoginResponse> githubLogin(GithubLoginRequest request) {
        return authService.githubLogin(request);
    }

    @Override
    public ResponseEntity<Map<String, String>> forgotPassword(ForgotPasswordRequest request) {
        return authService.forgotPassword(request);
    }

    @Override
    public ResponseEntity<Map<String, String>> resetPassword(ResetPasswordRequest request) {
        return authService.resetPassword(request);
    }

    @Override
    public ResponseEntity<UserWrapper> currentUser() {
        return authService.currentUser();
    }
}
