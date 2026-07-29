package com.rea.express.restImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.rea.express.constents.ReaConstants;
import com.rea.express.rest.UserRest;
import com.rea.express.service.UserService;
import com.rea.express.utils.ReaUtils;
import com.rea.express.wrapper.UserWrapper;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserRestImpl implements UserRest {

    private final UserService userService;

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUsers() {
        try {
            return userService.getAllUsers();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<UserWrapper> getUserByIdentifier(String identifier) {
        try {
            return userService.getUserByIdentifier(identifier);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<UserWrapper> getUserByEmail(String email) {
        try {
            return userService.getUserByEmail(email);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getUsersByRole(String role) {
        try {
            return userService.getUsersByRole(role);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Map<String, String>> signUp(Map<String, String> requestMap) {
        try {
            return userService.signUp(requestMap);
        } catch (Exception ex) {
            return ReaUtils.getResponseEntity(ReaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Map<String, String>> updateUser(Integer id, Map<String, String> requestMap) {
        try {
            return userService.updateUser(id, requestMap);
        } catch (Exception ex) {
            return ReaUtils.getResponseEntity(ReaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteUser(Integer id) {
        try {
            return userService.deleteUser(id);
        } catch (Exception ex) {
            return ReaUtils.getResponseEntity(ReaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
