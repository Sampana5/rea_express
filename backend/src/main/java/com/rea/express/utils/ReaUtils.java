package com.rea.express.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ReaUtils {

    private ReaUtils() {
    }

    public static ResponseEntity<Map<String, String>> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
        Map<String, String> body = new HashMap<>();
        body.put("message", responseMessage);
        return new ResponseEntity<>(body, httpStatus);
    }
}
