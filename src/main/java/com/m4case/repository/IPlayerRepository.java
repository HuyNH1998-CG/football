package com.m4case.repository;

import com.m4case.model.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPlayerRepository extends CrudRepository<Player, Long> {
    Player findByEmail(String email);
}
