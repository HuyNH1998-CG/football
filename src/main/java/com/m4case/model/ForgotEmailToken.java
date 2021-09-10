package com.m4case.model;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class ForgotEmailToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String confirmToken;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @OneToOne(targetEntity = MyUser.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private MyUser user;

    public ForgotEmailToken() {
    }
    public ForgotEmailToken(MyUser myUser) {
        this.user = myUser;
        date = new Date();
        confirmToken = UUID.randomUUID().toString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getConfirmToken() {
        return confirmToken;
    }

    public void setConfirmToken(String confirmToken) {
        this.confirmToken = confirmToken;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }
}
