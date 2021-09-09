package com.m4case.service;

import com.m4case.model.WeeklySalary;
import com.m4case.repository.IWeeklySalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class WeeklySalaryService implements IWeeklySalaryService {
    @Autowired
    IWeeklySalaryRepository iWeeklySalaryRepository;

    @Override
    public Iterable<WeeklySalary> findAll() {
        return iWeeklySalaryRepository.findAll();
    }

    @Override
    public Optional<WeeklySalary> findById(Long id) {
        return iWeeklySalaryRepository.findById(id);
    }

    @Override
    public void save(WeeklySalary weeklySalary) {
        iWeeklySalaryRepository.save(weeklySalary);
    }

    @Override
    public void delete(Long id) {
        iWeeklySalaryRepository.deleteById(id);
    }
}
