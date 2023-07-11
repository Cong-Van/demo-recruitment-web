package com.funix.prj_321x.asm02.DTO;

import com.funix.prj_321x.asm02.entity.Role;
import jakarta.validation.constraints.Pattern;

public class UserDTO {

    private int id;

    private String address;

    private String description;

    private String email;

    private String fullName;

    private String image;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).+$",
            message = "Mật khẩu cần chứa in hoa in thường, ký tự đặc biệt và số")
    private String password;

    @Pattern(regexp = "^0[0-9]{9}$", message = "Số điện thoại không đúng")
    private String phoneNumber;

    private int status;

    private Role role;

    public UserDTO() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
