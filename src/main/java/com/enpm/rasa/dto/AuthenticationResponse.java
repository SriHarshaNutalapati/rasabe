package com.enpm.rasa.dto;

import com.enpm.rasa.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String token;
    private int id;
    private String name;
    private Role role;
    private String email;
}
