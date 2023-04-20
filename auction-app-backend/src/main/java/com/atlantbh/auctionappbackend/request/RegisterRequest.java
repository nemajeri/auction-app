package com.atlantbh.auctionappbackend.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "First name cannot be empty.")
    @Setter(AccessLevel.NONE)
    private String firstName;

    @NotBlank(message = "Last name cannot be empty.")
    @Setter(AccessLevel.NONE)
    private String lastName;

    @NotBlank(message = "Email cannot be empty.")
    @Email(message = "Invalid email format.")
    @Setter(AccessLevel.NONE)
    private String email;

    @NotBlank(message = "Password cannot be empty.")
    @Setter(AccessLevel.NONE)
    private String password;
}
