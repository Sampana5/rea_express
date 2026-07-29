package com.rea.express.wrapper;

import com.rea.express.POJO.User;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserWrapper {

    private Integer id;
    private String name;
    private String contactNumber;
    private String email;
    private String status;
    private Set<String> roles;

    public static UserWrapper fromUser(User user) {
        UserWrapper wrapper = new UserWrapper();
        wrapper.setId(user.getId());
        wrapper.setName(user.getName());
        wrapper.setContactNumber(user.getContactNumber());
        wrapper.setEmail(user.getEmail());
        wrapper.setStatus(user.getStatus());
        wrapper.setRoles(
                user.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toSet())
        );
        return wrapper;
    }
}
