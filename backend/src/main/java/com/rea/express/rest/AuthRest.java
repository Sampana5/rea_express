package com.rea.express.rest;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.rea.express.dto.ForgotPasswordRequest;
import com.rea.express.dto.GithubLoginRequest;
import com.rea.express.dto.GoogleLoginRequest;
import com.rea.express.dto.ResetPasswordRequest;
import com.rea.express.wrapper.LoginResponse;
import com.rea.express.wrapper.UserWrapper;

import java.util.Map;

@RequestMapping(path = "/auth")
public interface AuthRest {

    @PostMapping(path = "/login")
    ResponseEntity<LoginResponse> login(@RequestBody Map<String, String> requestMap);

    @PostMapping(path = "/google")
    ResponseEntity<LoginResponse> googleLogin(@Valid @RequestBody GoogleLoginRequest request);

    @PostMapping(path = "/github")
    ResponseEntity<LoginResponse> githubLogin(@Valid @RequestBody GithubLoginRequest request);

    @PostMapping(path = "/forgot-password")
    ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request);

    @PostMapping(path = "/reset-password")
    ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request);

    @GetMapping(path = "/me")
    ResponseEntity<UserWrapper> currentUser();
}
