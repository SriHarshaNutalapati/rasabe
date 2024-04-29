package com.enpm.rasa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cardId;

    @ManyToOne
    private User userId;

    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String paymentId;
}
