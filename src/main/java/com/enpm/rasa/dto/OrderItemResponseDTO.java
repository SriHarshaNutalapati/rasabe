package com.enpm.rasa.dto;

import com.enpm.rasa.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDTO {
    private Product product;
    private Long unitPrice;
    private Long quantity;
    private int orderItemId;
    private int orderId;
}
