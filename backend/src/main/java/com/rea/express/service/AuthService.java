package com.rea.express.service;

import org.springframework.http.ResponseEntity;
import com.rea.express.dto.ForgotPasswordRequest;
import com.rea.express.dto.GithubLoginRequest;
import com.rea.express.dto.GoogleLoginRequest;
import com.rea.express.dto.ResetPasswordRequest;
import com.rea.express.wrapper.LoginResponse;
import com.rea.express.wrapper.UserWrapper;

import java.util.Map;

public interface AuthService {

    ResponseEntity<LoginResponse> login(Map<String, String> requestMap);

    ResponseEntity<LoginResponse> googleLogin(GoogleLoginRequest request);

    ResponseEntity<LoginResponse> githubLogin(GithubLoginRequest request);

    ResponseEntity<Map<String, String>> forgotPassword(ForgotPasswordRequest request);

    ResponseEntity<Map<String, String>> resetPassword(ResetPasswordRequest request);

    ResponseEntity<UserWrapper> currentUser();
}
