package com.m4case.service;

import com.m4case.model.Gender;
import com.m4case.repository.IGenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GenderService implements IGenderService {
    @Autowired
    private IGenderRepository genderRepository;

    @Override
    public Iterable<Gender> findAll() {
        return genderRepository.findAll();
    }

    @Override
    public Optional<Gender> findById(Long id) {
        return genderRepository.findById(id);
    }

    @Override
    public Object saveObj(Gender gender) {
        return genderRepository.save(gender);
    }

    @Override
    public void save(Gender gender) {
        genderRepository.save(gender);
    }

    @Override
    public void delete(Long id) {
        genderRepository.deleteById(id);
    }
}
