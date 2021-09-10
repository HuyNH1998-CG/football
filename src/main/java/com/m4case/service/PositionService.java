package com.m4case.service;

import com.m4case.model.Position;
import com.m4case.repository.IPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PositionService implements IPositionService {
    @Autowired
    private IPositionRepository positionRepository;

    @Override
    public Iterable<Position> findAll() {
        return positionRepository.findAll();
    }

    @Override
    public Optional<Position> findById(Long id) {
        return positionRepository.findById(id);
    }

    @Override
    public Object saveObj(Position position) {
        return positionRepository.save(position);
    }

    @Override
    public void save(Position position) {
        positionRepository.save(position);
    }

    @Override
    public void delete(Long id) {
        positionRepository.deleteById(id);
    }
}
