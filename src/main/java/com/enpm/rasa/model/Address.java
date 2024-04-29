package com.enpm.rasa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int addressId;

    @OneToOne
    private User userId;

    @Column(length = 500)
    private String street;

    @Column(length = 500)
    private String city;

    @Column(length = 500)
    private String state;

    @Column(length = 500)
    private String country;

    @Column(length = 500)
    private Long pinCode;

}
