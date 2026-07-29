package com.rea.express.service;

import org.springframework.http.ResponseEntity;
import com.rea.express.wrapper.LoginResponse;
import com.rea.express.wrapper.UserWrapper;

import java.util.Map;

public interface AuthService {

    ResponseEntity<LoginResponse> login(Map<String, String> requestMap);

    ResponseEntity<UserWrapper> currentUser();
}
