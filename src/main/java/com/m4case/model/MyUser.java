package com.m4case.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Entity
public class MyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Email(message = "Must be correctly input email")
    private String email;

    @Size(min = 6, max = 8, message = "Password between 6-8 character")
    private String password;

    private String name;
    private String age;
    @ManyToOne
    private Gender gender;
    private String avatar;

    @ManyToOne
    private Role role;

    public MyUser() {
    }

    public MyUser(Long id, String email, String password, String name, String age, Gender gender, String avatar, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.avatar = avatar;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
