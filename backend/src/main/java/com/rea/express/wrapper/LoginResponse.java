package com.rea.express.wrapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String type;
    private Integer id;
    private String name;
    private String email;
    private String contactNumber;
    private String status;
    private Set<String> roles;
}
