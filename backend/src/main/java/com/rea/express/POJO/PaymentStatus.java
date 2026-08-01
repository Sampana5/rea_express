package com.rea.express.POJO;

/** Statut paiement — NONE tant que le devis n'est pas converti en commande payante. */
public enum PaymentStatus {
    NONE,
    PENDING,
    PAID,
    FAILED,
    REFUNDED
}
