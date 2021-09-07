package com.m4case.repository;

import com.m4case.model.Coach;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICoachRepository extends CrudRepository<Coach, Long> {
    Coach findByEmail (String email);
}
