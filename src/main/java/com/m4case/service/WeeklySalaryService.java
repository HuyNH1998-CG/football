package com.m4case.service;

import com.m4case.model.WeeklySalary;
import com.m4case.repository.IWeeklySalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WeeklySalaryService implements IWeeklySalaryService {
    @Autowired
    private IWeeklySalaryRepository weeklySalaryRepository;

    @Override
    public Iterable<WeeklySalary> findAll() {
        return weeklySalaryRepository.findAll();
    }

    @Override
    public Optional<WeeklySalary> findById(Long id) {
        return weeklySalaryRepository.findById(id);
    }

    @Override
    public void save(WeeklySalary weeklySalary) {
        weeklySalaryRepository.save(weeklySalary);
    }

    @Override
    public void delete(Long id) {
        weeklySalaryRepository.deleteById(id);
    }

    @Override
    public WeeklySalary findByCoach_Id(Long id) {
        return weeklySalaryRepository.findByCoach_Id(id);
    }

    @Override
    public WeeklySalary findByPlayer_Id(Long id) {
        return weeklySalaryRepository.findByPlayer_Id(id);
    }
}
