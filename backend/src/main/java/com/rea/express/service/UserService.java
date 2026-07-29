package com.rea.express.service;

import org.springframework.http.ResponseEntity;
import com.rea.express.wrapper.UserWrapper;

import java.util.List;
import java.util.Map;

public interface UserService {

    ResponseEntity<List<UserWrapper>> getAllUsers();

    ResponseEntity<UserWrapper> getUserByIdentifier(String identifier);

    ResponseEntity<UserWrapper> getUserByEmail(String email);

    ResponseEntity<List<UserWrapper>> getUsersByRole(String role);

    ResponseEntity<Map<String, String>> signUp(Map<String, String> requestMap);

    ResponseEntity<Map<String, String>> updateUser(Integer id, Map<String, String> requestMap);

    ResponseEntity<Map<String, String>> deleteUser(Integer id);
}
