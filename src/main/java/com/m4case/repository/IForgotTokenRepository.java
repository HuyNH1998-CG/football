package com.m4case.repository;

import com.m4case.model.ForgotEmailToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IForgotTokenRepository extends CrudRepository<ForgotEmailToken, Long> {
    ForgotEmailToken findByConfirmToken (String confirmToken);
}
