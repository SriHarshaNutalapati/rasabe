package com.enpm.rasa.dto;

import com.enpm.rasa.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardDetailsRequestDTO {
    private PaymentMethod paymentMethod;
    private String name;
    private String paymentId;
}
