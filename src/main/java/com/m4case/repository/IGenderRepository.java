package com.m4case.repository;

import com.m4case.model.Gender;
import org.springframework.data.repository.CrudRepository;

public interface IGenderRepository extends CrudRepository<Gender, Long> {
}
