package com.enpm.rasa.modeltest;

import com.enpm.rasa.model.Address;
import com.enpm.rasa.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AddressTest {

    @Test
    void testAddressEntityCreation() {
        // Arrange
        User user = new User();
        user.setUserId(1);

        String street = "123 Main St";
        String city = "Anytown";
        String state = "CA";
        String country = "USA";
        Long pinCode = 12345L;

        // Act
        Address address = Address.builder()
                .userId(user)
                .street(street)
                .city(city)
                .state(state)
                .country(country)
                .pinCode(pinCode)
                .build();

        // Assert
        assertEquals(user, address.getUserId());
        assertEquals(street, address.getStreet());
        assertEquals(city, address.getCity());
        assertEquals(state, address.getState());
        assertEquals(country, address.getCountry());
        assertEquals(pinCode, address.getPinCode());
    }
}