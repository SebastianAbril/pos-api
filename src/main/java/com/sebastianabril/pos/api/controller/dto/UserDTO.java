package com.sebastianabril.pos.api.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class UserDTO {
    @NotBlank(message = "Insert a name")
    private String name;

    @NotBlank(message = "Insert a last name")
    private String lastName;

    @NotBlank(message = "Insert an email")
    @Email(message = "Insert a valid address")
    private String email;

    @Size(min = 8, max = 20)
    @NotBlank(message = "Insert a password")
    private String password;

    @PositiveOrZero(message = "Insert a role id")
    private Integer roleId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
