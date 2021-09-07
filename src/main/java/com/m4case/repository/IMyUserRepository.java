package com.m4case.repository;

import com.m4case.model.MyUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMyUserRepository extends CrudRepository<MyUser, Long> {
    MyUser findByEmail(String email);
}
