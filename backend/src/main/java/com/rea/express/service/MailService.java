package com.rea.express.service;

public interface MailService {

    void sendResetCode(String to, String code, long expirationMinutes);
}
