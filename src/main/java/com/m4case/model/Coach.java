package com.m4case.model;

import javax.persistence.*;

@Entity
public class Coach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String age;
    private String hometown;
    private String avatar;
    private long salary;
    @OneToOne(cascade = {CascadeType.ALL})
    private WeeklySalary weeklySalary;

    @Column(unique = true)
    private String email;

    public Coach() {
    }

    public Coach(long id, String name, String age, String hometown, long salary, WeeklySalary weeklySalary, String email) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.hometown = hometown;
        this.salary = salary;
        this.weeklySalary = weeklySalary;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    public WeeklySalary getWeeklySalary() {
        return weeklySalary;
    }

    public void setWeeklySalary(WeeklySalary weeklySalary) {
        this.weeklySalary = weeklySalary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
