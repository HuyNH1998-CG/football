package com.m4case.service;

import com.m4case.model.Player;

import java.util.List;

public interface IPlayerService extends IGeneralService<Player> {
    Player findByEmail(String email);
    List<Player> findBySalaryBetween(Long min, Long max);
    List<Player> findAllBySalaryGreaterThanEqual (Long salary);
}
