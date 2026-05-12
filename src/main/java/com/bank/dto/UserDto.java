package com.bank.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long userId;
    
    @NotBlank(message = "Name is required")
    @Size(min = 3, message = "Minimum 3 characters required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Minimum 6 characters required")
    private String password;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Must be 10 digit number")
    private String phoneNumber;

    @NotBlank(message = "Aadhaar is required")
    @Pattern(regexp = "^[0-9]{12}$", message = "Aadhaar must be 12 digits")
    private String aadhaarNumber;

    @NotNull(message = "DOB is required")
    @Past(message = "DOB must be in past")
    private LocalDate dob;

    @Pattern(regexp="SAVINGS|CURRENT",message = "Invalid account type")
    private String accountType;

    @NotBlank(message = "Address is required")
    @Size(min = 5, message = "Address is too short")
    private String address;

    private boolean createAccount = true;
    
    private boolean enabled;

    private List<AccountDto> accounts;
}
