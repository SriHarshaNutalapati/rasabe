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
public class BuyNowDTO {
    private PaymentMethod paymentMethod;
}
