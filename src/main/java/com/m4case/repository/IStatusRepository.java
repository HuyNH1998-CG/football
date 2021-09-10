package com.m4case.repository;

import com.m4case.model.Status;
import org.springframework.data.repository.CrudRepository;

public interface IStatusRepository extends CrudRepository<Status, Long> {
}
