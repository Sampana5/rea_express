package com.rea.express.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.rea.express.POJO.ERole;
import com.rea.express.POJO.Role;
import com.rea.express.POJO.User;
import com.rea.express.constents.ReaConstants;
import com.rea.express.dao.RoleDao;
import com.rea.express.dao.UserDao;
import com.rea.express.exceptions.InvalidRoleException;
import com.rea.express.service.UserService;
import com.rea.express.utils.ReaUtils;
import com.rea.express.wrapper.UserWrapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUsers() {
        log.info("Fetching all users");
        try {
            List<UserWrapper> users = userDao.findAll().stream()
                    .map(UserWrapper::fromUser)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(users);
        } catch (Exception ex) {
            log.error("Failed to fetch users", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<UserWrapper> getUserByIdentifier(String identifier) {
        log.info("Fetching user by identifier {}", identifier);
        if (identifier.matches("\\d+")) {
            return getUserById(Integer.parseInt(identifier));
        }
        return getUserByName(identifier);
    }

    private ResponseEntity<UserWrapper> getUserById(Integer id) {
        log.info("Fetching user by id {}", id);
        try {
            return userDao.findById(id)
                    .map(UserWrapper::fromUser)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception ex) {
            log.error("Failed to fetch user by id {}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private ResponseEntity<UserWrapper> getUserByName(String name) {
        log.info("Fetching user by name {}", name);
        try {
            User user = userDao.findByName(name);
            if (Objects.isNull(user)) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(UserWrapper.fromUser(user));
        } catch (Exception ex) {
            log.error("Failed to fetch user by name {}", name, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<UserWrapper> getUserByEmail(String email) {
        log.info("Fetching user by email {}", email);
        try {
            User user = userDao.findByEmailId(email);
            if (Objects.isNull(user)) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(UserWrapper.fromUser(user));
        } catch (Exception ex) {
            log.error("Failed to fetch user by email {}", email, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getUsersByRole(String role) {
        log.info("Fetching users by role {}", role);
        try {
            ERole roleName = parseRole(role);
            List<UserWrapper> users = userDao.findByRoleName(roleName).stream()
                    .map(UserWrapper::fromUser)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(users);
        } catch (InvalidRoleException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Failed to fetch users by role {}", role, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Map<String, String>> signUp(Map<String, String> requestMap) {
        log.info("Inside signup {}", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                User existingUser = userDao.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(existingUser)) {
                    userDao.save(getUserFromMap(requestMap));
                    return ReaUtils.getResponseEntity("Inscription réussie.", HttpStatus.OK);
                }
                return ReaUtils.getResponseEntity("Cet email est déjà utilisé.", HttpStatus.BAD_REQUEST);
            }
            return ReaUtils.getResponseEntity(ReaConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        } catch (InvalidRoleException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Signup failed", ex);
        }
        return ReaUtils.getResponseEntity(ReaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Map<String, String>> updateUser(Integer id, Map<String, String> requestMap) {
        log.info("Updating user {} with {}", id, requestMap);
        try {
            User user = userDao.findById(id).orElse(null);
            if (Objects.isNull(user)) {
                return ReaUtils.getResponseEntity("Utilisateur introuvable.", HttpStatus.NOT_FOUND);
            }

            if (requestMap.containsKey("name")) {
                user.setName(requestMap.get("name"));
            }
            if (requestMap.containsKey("contactNumber")) {
                user.setContactNumber(requestMap.get("contactNumber"));
            }
            if (requestMap.containsKey("email")) {
                String newEmail = requestMap.get("email");
                User userWithSameEmail = userDao.findByEmailId(newEmail);
                if (userWithSameEmail != null && !userWithSameEmail.getId().equals(id)) {
                    return ReaUtils.getResponseEntity("Cet email est déjà utilisé.", HttpStatus.BAD_REQUEST);
                }
                user.setEmail(newEmail);
            }
            if (requestMap.containsKey("password")) {
                user.setPassword(passwordEncoder.encode(requestMap.get("password")));
            }
            if (requestMap.containsKey("status")) {
                user.setStatus(requestMap.get("status"));
            }
            if (requestMap.containsKey("role")) {
                user.setRoles(Set.of(resolveRole(requestMap.get("role"))));
            }

            userDao.save(user);
            return ReaUtils.getResponseEntity("Utilisateur mis à jour.", HttpStatus.OK);
        } catch (InvalidRoleException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Update failed for user {}", id, ex);
        }
        return ReaUtils.getResponseEntity(ReaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteUser(Integer id) {
        log.info("Deleting user {}", id);
        try {
            if (!userDao.existsById(id)) {
                return ReaUtils.getResponseEntity("Utilisateur introuvable.", HttpStatus.NOT_FOUND);
            }
            userDao.deleteById(id);
            return ReaUtils.getResponseEntity("Utilisateur supprimé.", HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Delete failed for user {}", id, ex);
        }
        return ReaUtils.getResponseEntity(ReaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpMap(Map<String, String> requestMap) {
        return requestMap.containsKey("name")
                && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email")
                && requestMap.containsKey("password");
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(passwordEncoder.encode(requestMap.get("password")));
        user.setStatus("true");
        user.setRoles(Set.of(resolveRole(requestMap.get("role"))));
        return user;
    }

    private Role resolveRole(String roleValue) {
        ERole roleName = parseRole(roleValue);
        return roleDao.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException("Role not found: " + roleName));
    }

    private ERole parseRole(String roleValue) {
        if (roleValue == null || roleValue.isBlank()) {
            return ERole.ROLE_USER;
        }
        if (roleValue.equalsIgnoreCase("user")) {
            return ERole.ROLE_USER;
        }
        if (roleValue.equalsIgnoreCase("admin")) {
            return ERole.ROLE_ADMIN;
        }
        throw new InvalidRoleException(ReaConstants.INVALID_ROLE);
    }
}

