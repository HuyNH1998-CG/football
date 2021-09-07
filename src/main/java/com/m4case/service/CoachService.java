package com.m4case.service;

import com.m4case.model.Coach;
import com.m4case.repository.ICoachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CoachService implements ICoachService {
    @Autowired
    private ICoachRepository coachRepository;

    @Override
    public Iterable<Coach> findAll() {
        return coachRepository.findAll();
    }

    @Override
    public Optional<Coach> findById(Long id) {
        return coachRepository.findById(id);
    }

    @Override
    public void save(Coach coach) {
        coachRepository.save(coach);
    }

    @Override
    public void delete(Long id) {
        coachRepository.deleteById(id);
    }

    public Coach findByEmail(String email) {
        return coachRepository.findByEmail(email);
    }
}
