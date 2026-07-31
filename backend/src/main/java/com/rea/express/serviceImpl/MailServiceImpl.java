package com.rea.express.serviceImpl;

import com.rea.express.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final ObjectProvider<JavaMailSender> mailSenderProvider;

    @Value("${spring.mail.username:}")
    private String fromAddress;

    @Override
    public void sendResetCode(String to, String code, long expirationMinutes) {
        JavaMailSender sender = mailSenderProvider.getIfAvailable();
        if (sender == null || fromAddress == null || fromAddress.isBlank()) {
            // SMTP non configuré (dev) : le code est loggé pour pouvoir tester le flux.
            log.warn("SMTP non configuré — code de réinitialisation pour {} : {}", to, code);
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(to);
            message.setSubject("REA Express — Votre code de vérification");
            message.setText("Bonjour,\n\n"
                    + "Votre code de vérification est : " + code + "\n"
                    + "Il expire dans " + expirationMinutes + " minutes.\n\n"
                    + "Si vous n'êtes pas à l'origine de cette demande, ignorez ce message : "
                    + "votre mot de passe reste inchangé.\n\n"
                    + "L'équipe REA Express");
            sender.send(message);
            log.info("Code de réinitialisation envoyé à {}", to);
        } catch (Exception ex) {
            log.error("Échec d'envoi du code de réinitialisation à {}", to, ex);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Impossible d'envoyer l'e-mail pour le moment. Réessayez plus tard.");
        }
    }
}
