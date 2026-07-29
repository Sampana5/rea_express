package com.rea.express.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.rea.express.wrapper.UserWrapper;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/users")
public interface UserRest {

    @GetMapping
    ResponseEntity<List<UserWrapper>> getAllUsers();

    @GetMapping(path = "/email")
    ResponseEntity<UserWrapper> getUserByEmail(@RequestParam String email);

    @GetMapping(path = "/role")
    ResponseEntity<List<UserWrapper>> getUsersByRole(@RequestParam String role);

    @GetMapping(path = "/{identifier}")
    ResponseEntity<UserWrapper> getUserByIdentifier(@PathVariable String identifier);

    @PostMapping(path = "/signup")
    ResponseEntity<Map<String, String>> signUp(@RequestBody Map<String, String> requestMap);

    @PutMapping(path = "/{id}")
    ResponseEntity<Map<String, String>> updateUser(@PathVariable Integer id, @RequestBody Map<String, String> requestMap);

    @DeleteMapping(path = "/{id}")
    ResponseEntity<Map<String, String>> deleteUser(@PathVariable Integer id);
}
