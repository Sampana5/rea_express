package com.rea.express.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.rea.express.POJO.ERole;
import com.rea.express.POJO.Role;
import com.rea.express.dao.RoleDao;

@Component
@RequiredArgsConstructor
public class RoleDataLoader implements CommandLineRunner {

    private final RoleDao roleDao;

    @Override
    public void run(String... args) {
        for (ERole roleName : ERole.values()) {
            roleDao.findByName(roleName).orElseGet(() -> roleDao.save(new Role(roleName)));
        }
    }
}
