package com.enpm.rasa.dto;

import com.enpm.rasa.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
    private int orderId;
    private int userId;
    private String fullName;
    private String orderDate;
    private Long totalAmount;
    private PaymentMethod paymentMethod;
    private String orderStatus;

    // Getters and setters
}
