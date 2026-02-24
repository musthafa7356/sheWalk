package org.shewalk.Dto;

public class UserProfileDto {

    private String name;
    private String email;
    private String role;
    public UserProfileDto(String name, String email, String role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }
    public String getName() {
        return name;
    }
    public String getRole() {
        return role;
    }
}
