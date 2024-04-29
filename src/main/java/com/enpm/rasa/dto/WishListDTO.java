package com.enpm.rasa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WishListDTO {
    private String productName;
    private int productId;
    private String productImage;
    private int wishlistId;
}
