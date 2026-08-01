package com.rea.express.POJO;

/**
 * Cycle de vie d'un devis.
 * Les statuts AWAITING_PAYMENT / PAID / FULFILLED sont prévus pour
 * l'intégration paiement (prochaine mise à jour) sans casser le modèle.
 */
public enum QuoteStatus {
    PENDING,
    IN_REVIEW,
    QUOTED,
    ACCEPTED,
    CANCELLED,
    AWAITING_PAYMENT,
    PAID,
    FULFILLED
}
