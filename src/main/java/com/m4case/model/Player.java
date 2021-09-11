package com.m4case.model;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private Date birthday;
    private String hometown;
    private String position;
    private String hype;
    private int height;
    private int weight;
    private int bmi;
    private long salary;

    @OneToOne(cascade = {CascadeType.ALL})
    private WeeklySalary weeklySalary;

    private String avatar;
    private String status;

    @Column(unique = true)
    private String email;

    public Player() {
    }

    public Player(long id, String name, Date birthday, String hometown, String position, String hype, int height, int weight, int bmi, long salary, WeeklySalary weeklySalary, String avatar, String status, String email) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.hometown = hometown;
        this.position = position;
        this.hype = hype;
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
        this.salary = salary;
        this.weeklySalary = weeklySalary;
        this.avatar = avatar;
        this.status = status;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getHype() {
        return hype;
    }

    public void setHype(String hype) {
        this.hype = hype;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getBmi() {
        return bmi;
    }

    public void setBmi(int bmi) {
        this.bmi = bmi;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
