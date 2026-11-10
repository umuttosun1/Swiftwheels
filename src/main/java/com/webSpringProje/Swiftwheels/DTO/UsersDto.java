package com.webSpringProje.Swiftwheels.DTO;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.Date;

public class UsersDto {
    private Long id;
    @NotBlank(message = "Ad zorunlu")
    @Size(max = 40)
    private String firstName;
    @NotBlank(message = "Soyad zorunlu")
    @Size(max = 40)
    private String lastName;
    @NotBlank(message = "Şifre zorunlu")
    @Size(min = 8, max = 72, message = "Şifre 8–72 karakter olmalı")
    private String password;
    @NotBlank(message = "E-posta zorunlu")
    @Email(message = "Geçerli bir e-posta girin")
    @Size(max = 100)
    private String email;
    @NotNull(message = "Doğum tarihi zorunlu")
    @Past(message = "Doğum tarihi geçmişte olmalı")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;
    @NotBlank(message = "Telefon zorunlu")
    @Pattern(regexp = "^(\\+90|0)?5\\d{9}$", message = "05XXXXXXXXX biçiminde girin")
    private String phoneNumber;

    public UsersDto(Long id, String firstName, String lastName, String password, String email, LocalDate birthDate, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }

    public UsersDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
