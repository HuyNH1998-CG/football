package com.m4case.service;

import com.m4case.model.Hype;
import com.m4case.repository.IHypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HypeService implements IHypeService {
    @Autowired
    private IHypeRepository hypeRepository;

    @Override
    public Iterable<Hype> findAll() {
        return hypeRepository.findAll();
    }

    @Override
    public Optional<Hype> findById(Long id) {
        return hypeRepository.findById(id);
    }

    @Override
    public Object saveObj(Hype hype) {
        return hypeRepository.save(hype);
    }

    @Override
    public void save(Hype hype) {
        hypeRepository.save(hype);
    }

    @Override
    public void delete(Long id) {
        hypeRepository.deleteById(id);
    }
}
