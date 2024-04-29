package com.enpm.rasa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddProduct {
    private String name;
    private String description;
    private Long price;
    private String tagName;
    private Long quantity;
    private String imageUrl;
}
