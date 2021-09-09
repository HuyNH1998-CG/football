package com.m4case.repository;

import com.m4case.model.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPlayerRepository extends CrudRepository<Player, Long> {
    Player findByEmail(String email);
    List<Player> findAllBySalaryBetween (Long salary1, Long salary2);
    List<Player> findAllBySalaryGreaterThanEqual (Long salary);
}
