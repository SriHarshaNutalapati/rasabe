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
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int productId;

    @Column(length = 500, unique = true, nullable = false)
    private String name;

    @Column(length = 5000)
    private String description;

    @Column(length = 5000, columnDefinition = "VARCHAR(5000) DEFAULT 'https://totalcomp.com/images/no-image.jpeg'")
    private String imageUrl;

    private Long price;

    @ManyToOne
    private ProductTag tag;

}
