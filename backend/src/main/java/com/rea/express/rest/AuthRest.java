package com.rea.express.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.rea.express.wrapper.LoginResponse;
import com.rea.express.wrapper.UserWrapper;

import java.util.Map;

@RequestMapping(path = "/auth")
public interface AuthRest {

    @PostMapping(path = "/login")
    ResponseEntity<LoginResponse> login(@RequestBody Map<String, String> requestMap);

    @GetMapping(path = "/me")
    ResponseEntity<UserWrapper> currentUser();
}
