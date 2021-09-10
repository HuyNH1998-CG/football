package com.m4case.repository;

import com.m4case.model.Position;
import org.springframework.data.repository.CrudRepository;

public interface IPositionRepository extends CrudRepository<Position, Long> {
}
