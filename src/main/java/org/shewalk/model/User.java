package org.shewalk.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"})
})
public class User {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;


    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private String role;

    @NotBlank(message = "Trusted email is required")
    @Email(message = "Trusted email should be valid")
    @Column(nullable = false)
    private String trustedEmail;

    //no arg condtructor
    public User(){}

    //Constructor
    public User(Long id, String name, String email, String password,  String role, String trustedEmail) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.trustedEmail = trustedEmail;
    }

    //Getters and Setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getRole() {return role;}
    public void setRole(String role) {this.role = role;}

    public String getTrustedEmail() { return trustedEmail; }
    public void setTrustedEmail(String trustedEmail) { this.trustedEmail = trustedEmail; }
}
