package com.m4case.model;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class WeeklySalary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Coach coach;

    @OneToOne
    private Player player;

    private long totalSalary;

    @CreationTimestamp
    private Date date;

    public WeeklySalary() {
    }

    public WeeklySalary(Long id, Coach coach, Player player, long totalSalary, Date date) {
        this.id = id;
        this.coach = coach;
        this.player = player;
        this.totalSalary = totalSalary;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public long getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(long totalSalary) {
        this.totalSalary = totalSalary;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
