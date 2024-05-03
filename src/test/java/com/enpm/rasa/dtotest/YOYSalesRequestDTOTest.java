package com.enpm.rasa.dtotest;

import com.enpm.rasa.dto.YOYSalesRequestDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class YOYSalesRequestDTOTest {

    @Test
    public void testConstructorAndGetters() {
        // Arrange
        int year = 2023;
        String productName = "ExampleProduct";

        // Act
        YOYSalesRequestDTO requestDTO = new YOYSalesRequestDTO(year, productName);

        // Assert
        assertEquals(year, requestDTO.getYear());
        assertEquals(productName, requestDTO.getProductName());
    }

    @Test
    public void testSetters() {
        // Arrange
        int year = 2023;
        String productName = "ExampleProduct";
        YOYSalesRequestDTO requestDTO = new YOYSalesRequestDTO();

        // Act
        requestDTO.setYear(year);
        requestDTO.setProductName(productName);

        // Assert
        assertEquals(year, requestDTO.getYear());
        assertEquals(productName, requestDTO.getProductName());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Arrange
        YOYSalesRequestDTO requestDTO1 = new YOYSalesRequestDTO(2023, "ExampleProduct");
        YOYSalesRequestDTO requestDTO2 = new YOYSalesRequestDTO(2023, "ExampleProduct");
        YOYSalesRequestDTO requestDTO3 = new YOYSalesRequestDTO(2024, "ExampleProduct");
        YOYSalesRequestDTO requestDTO4 = new YOYSalesRequestDTO(2023, "AnotherProduct");

        // Assert
        assertEquals(requestDTO1, requestDTO2);
        assertEquals(requestDTO1.hashCode(), requestDTO2.hashCode());
        assertEquals(requestDTO1, requestDTO1); // Reflexive property
        assertEquals(requestDTO1.hashCode(), requestDTO1.hashCode()); // Consistent property
        assertEquals(requestDTO1.equals(requestDTO2), requestDTO2.equals(requestDTO1)); // Symmetric property
        assertEquals(requestDTO1.equals(requestDTO2) && requestDTO2.equals(requestDTO3), requestDTO1.equals(requestDTO3)); // Transitive property
        assertEquals(false, requestDTO1.equals(null)); // Non-nullity property
        assertEquals(false, requestDTO1.equals(requestDTO4)); // Equality check for different objects
    }

    @Test
    public void testToString() {
        // Arrange
        YOYSalesRequestDTO requestDTO = new YOYSalesRequestDTO(2023, "ExampleProduct");

        // Act & Assert
        assertEquals("YOYSalesRequestDTO(year=2023, productName=ExampleProduct)", requestDTO.toString());
    }
}

