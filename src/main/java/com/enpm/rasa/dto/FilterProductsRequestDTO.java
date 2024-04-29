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
public class FilterProductsRequestDTO {
    private List<String> tags;
    private Long minPrice;
    private Long maxPrice;
    private String searchString;
}
