package com.m4case.service;

import com.m4case.model.Player;
import com.m4case.repository.IPlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService implements IPlayerService {
    @Autowired
    private IPlayerRepository playerRepository;

    @Override
    public Iterable<Player> findAll() {
        return playerRepository.findAll();
    }

    @Override
    public Optional<Player> findById(Long id) {
        return playerRepository.findById(id);
    }

    @Override
    public void save(Player player) {
        playerRepository.save(player);
    }

    @Override
    public void delete(Long id) {
        playerRepository.deleteById(id);
    }

    public Player findByEmail(String email) {
        return playerRepository.findByEmail(email);
    }

    @Override
    public List<Player> findBySalaryBetween(Long min, Long max) {
        return playerRepository.findAllBySalaryBetween(min, max);
    }

    @Override
    public List<Player> findAllBySalaryGreaterThanEqual(Long salary) {
        return playerRepository.findAllBySalaryGreaterThanEqual(salary);
    }
}
