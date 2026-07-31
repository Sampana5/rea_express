package com.rea.express.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.rea.express.JWT.CustomerUserDetailsService;
import com.rea.express.JWT.JwtUtil;
import com.rea.express.POJO.ERole;
import com.rea.express.POJO.PasswordResetCode;
import com.rea.express.POJO.User;
import com.rea.express.dao.PasswordResetCodeDao;
import com.rea.express.dao.RoleDao;
import com.rea.express.dao.UserDao;
import com.rea.express.dto.ForgotPasswordRequest;
import com.rea.express.dto.GithubLoginRequest;
import com.rea.express.dto.GoogleLoginRequest;
import com.rea.express.dto.ResetPasswordRequest;
import com.rea.express.oauth.GithubOAuthClient;
import com.rea.express.oauth.GoogleTokenVerifier;
import com.rea.express.service.AuthService;
import com.rea.express.service.MailService;
import com.rea.express.utils.ReaUtils;
import com.rea.express.wrapper.LoginResponse;
import com.rea.express.wrapper.UserWrapper;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final int MAX_CODE_ATTEMPTS = 5;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final AuthenticationManager authenticationManager;
    private final CustomerUserDetailsService customerUserDetailsService;
    private final JwtUtil jwtUtil;
    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;
    private final GoogleTokenVerifier googleTokenVerifier;
    private final GithubOAuthClient githubOAuthClient;
    private final PasswordResetCodeDao passwordResetCodeDao;
    private final MailService mailService;

    @Value("${app.reset-code.expiration-minutes:10}")
    private long resetCodeExpirationMinutes;

    @Override
    public ResponseEntity<LoginResponse> login(Map<String, String> requestMap) {
        log.info("Tentative de connexion pour {}", requestMap != null ? requestMap.get("email") : null);

        if (!validateLoginMap(requestMap)) {
            throw new IllegalArgumentException("Email et mot de passe sont requis.");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestMap.get("email"),
                            requestMap.get("password")
                    )
            );
        } catch (BadCredentialsException ex) {
            log.warn("Identifiants invalides pour {}", requestMap.get("email"));
            throw new BadCredentialsException("Email ou mot de passe incorrect.");
        }

        User user = customerUserDetailsService.getUserDetails();
        if (Objects.isNull(user)) {
            user = userDao.findByEmailId(requestMap.get("email"));
        }
        if (Objects.isNull(user)) {
            throw new BadCredentialsException("Email ou mot de passe incorrect.");
        }

        ensureActive(user);
        return ResponseEntity.ok(buildLoginResponse(user));
    }

    @Override
    @Transactional
    public ResponseEntity<LoginResponse> googleLogin(GoogleLoginRequest request) {
        GoogleTokenVerifier.GoogleProfile profile = googleTokenVerifier.verify(request.getIdToken());
        log.info("Connexion Google pour {}", profile.email());
        User user = findOrCreateSocialUser(profile.email(), profile.name(), "google");
        return ResponseEntity.ok(buildLoginResponse(user));
    }

    @Override
    @Transactional
    public ResponseEntity<LoginResponse> githubLogin(GithubLoginRequest request) {
        GithubOAuthClient.GithubProfile profile = githubOAuthClient.fetchProfile(request.getCode());
        log.info("Connexion GitHub pour {}", profile.email());
        User user = findOrCreateSocialUser(profile.email(), profile.name(), "github");
        return ResponseEntity.ok(buildLoginResponse(user));
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, String>> forgotPassword(ForgotPasswordRequest request) {
        if ("sms".equalsIgnoreCase(request.getChannel())) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED,
                    "L'envoi de code par SMS sera bientôt disponible. Utilisez votre adresse e-mail.");
        }

        String email = request.getEmail().trim();
        User user = userDao.findByEmailId(email);

        if (user != null) {
            passwordResetCodeDao.findTopByEmailAndUsedFalseOrderByIdDesc(email).ifPresent(previous -> {
                if (previous.getCreatedAt().isAfter(LocalDateTime.now().minusSeconds(60))) {
                    throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                            "Un code vient d'être envoyé. Patientez une minute avant d'en redemander un.");
                }
                previous.setUsed(true);
                passwordResetCodeDao.save(previous);
            });

            String code = String.format("%06d", RANDOM.nextInt(1_000_000));
            PasswordResetCode resetCode = new PasswordResetCode();
            resetCode.setEmail(email);
            resetCode.setCodeHash(passwordEncoder.encode(code));
            resetCode.setCreatedAt(LocalDateTime.now());
            resetCode.setExpiresAt(LocalDateTime.now().plusMinutes(resetCodeExpirationMinutes));
            resetCode.setUsed(false);
            resetCode.setAttempts(0);
            passwordResetCodeDao.save(resetCode);

            mailService.sendResetCode(email, code, resetCodeExpirationMinutes);
        } else {
            log.info("Demande de réinitialisation pour un e-mail inconnu : {}", email);
        }

        // Réponse identique que le compte existe ou non (pas de fuite d'information).
        return ReaUtils.getResponseEntity(
                "Si un compte existe avec cet e-mail, un code de vérification vient d'être envoyé.",
                HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, String>> resetPassword(ResetPasswordRequest request) {
        String email = request.getEmail().trim();
        PasswordResetCode resetCode = passwordResetCodeDao
                .findTopByEmailAndUsedFalseOrderByIdDesc(email)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Code invalide ou expiré. Demandez un nouveau code."));

        if (resetCode.getExpiresAt().isBefore(LocalDateTime.now())
                || resetCode.getAttempts() >= MAX_CODE_ATTEMPTS) {
            resetCode.setUsed(true);
            passwordResetCodeDao.save(resetCode);
            throw new IllegalArgumentException("Code invalide ou expiré. Demandez un nouveau code.");
        }

        if (!passwordEncoder.matches(request.getCode().trim(), resetCode.getCodeHash())) {
            resetCode.setAttempts(resetCode.getAttempts() + 1);
            passwordResetCodeDao.save(resetCode);
            int remaining = MAX_CODE_ATTEMPTS - resetCode.getAttempts();
            throw new IllegalArgumentException(
                    "Code incorrect. Il vous reste " + remaining + " essai(s).");
        }

        User user = userDao.findByEmailId(email);
        if (user == null) {
            throw new IllegalArgumentException("Code invalide ou expiré. Demandez un nouveau code.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userDao.save(user);
        resetCode.setUsed(true);
        passwordResetCodeDao.save(resetCode);
        log.info("Mot de passe réinitialisé pour {}", email);

        return ReaUtils.getResponseEntity(
                "Mot de passe réinitialisé. Vous pouvez maintenant vous connecter.",
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserWrapper> currentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String email = authentication.getName();
            User user = userDao.findByEmailId(email);
            if (Objects.isNull(user)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            return ResponseEntity.ok(UserWrapper.fromUser(user));
        } catch (Exception ex) {
            log.error("Impossible de récupérer l'utilisateur courant", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ------------------------------------------------------------- privés

    private User findOrCreateSocialUser(String email, String name, String provider) {
        User user = userDao.findByEmailId(email);
        if (user == null) {
            user = new User();
            user.setName(name != null && !name.isBlank() ? name : email);
            user.setEmail(email);
            user.setContactNumber("");
            // Mot de passe aléatoire : le compte social ne se connecte pas par mot de passe,
            // mais l'utilisateur peut en définir un via « mot de passe oublié ».
            user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            user.setStatus("true");
            user.setProvider(provider);
            user.setRoles(Set.of(roleDao.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new IllegalStateException("Rôle utilisateur introuvable"))));
            user = userDao.save(user);
            log.info("Compte {} créé via {}", email, provider);
        }
        ensureActive(user);
        return user;
    }

    private void ensureActive(User user) {
        if ("false".equalsIgnoreCase(user.getStatus())) {
            throw new DisabledException("Votre compte est désactivé. Contactez REA Express.");
        }
    }

    private LoginResponse buildLoginResponse(User user) {
        String role = user.getRoles().stream()
                .findFirst()
                .map(r -> r.getName().name())
                .orElse("ROLE_USER");

        String token = jwtUtil.generateToken(user.getEmail(), role);

        return LoginResponse.builder()
                .token(token)
                .type("Bearer")
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .contactNumber(user.getContactNumber())
                .status(user.getStatus())
                .roles(user.getRoles().stream()
                        .map(r -> r.getName().name())
                        .collect(Collectors.toSet()))
                .build();
    }

    private boolean validateLoginMap(Map<String, String> requestMap) {
        return requestMap != null
                && requestMap.containsKey("email")
                && requestMap.containsKey("password")
                && requestMap.get("email") != null
                && !requestMap.get("email").isBlank()
                && requestMap.get("password") != null
                && !requestMap.get("password").isBlank();
    }
}
