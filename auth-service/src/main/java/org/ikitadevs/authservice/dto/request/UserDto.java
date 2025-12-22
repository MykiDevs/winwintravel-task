package org.ikitadevs.authservice.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}
