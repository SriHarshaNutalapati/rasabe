package com.enpm.rasa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCartDTO {
    private String productName;
    private Long quantity;
    private Long amount;
    private int productId;
    private String productImage;
    private String tagName;
    private int cartId;
}
