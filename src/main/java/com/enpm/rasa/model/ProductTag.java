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
public class ProductTag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int tagId;

    @Column(length = 500, unique = true, nullable = false)
    private String tagName;

    @Column(length = 5000, nullable = false, columnDefinition = "VARCHAR(5000) DEFAULT 'https://totalcomp.com/images/no-image.jpeg'")
    private String imageUrl;
}
