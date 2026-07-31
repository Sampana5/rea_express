package com.rea.express.restImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.rea.express.rest.UserRest;
import com.rea.express.service.UserService;
import com.rea.express.wrapper.UserWrapper;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserRestImpl implements UserRest {

    private final UserService userService;

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    public ResponseEntity<UserWrapper> getUserByIdentifier(String identifier) {
        return userService.getUserByIdentifier(identifier);
    }

    @Override
    public ResponseEntity<UserWrapper> getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getUsersByRole(String role) {
        return userService.getUsersByRole(role);
    }

    @Override
    public ResponseEntity<Map<String, String>> signUp(Map<String, String> requestMap) {
        return userService.signUp(requestMap);
    }

    @Override
    public ResponseEntity<Map<String, String>> updateUser(Integer id, Map<String, String> requestMap) {
        return userService.updateUser(id, requestMap);
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteUser(Integer id) {
        return userService.deleteUser(id);
    }
}
