package com.m4case.service;

import com.m4case.model.WeeklySalary;
import com.m4case.repository.IWeeklySalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
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
    public Object saveObj(WeeklySalary weeklySalary) {
        return weeklySalaryRepository.save(weeklySalary);
    }

    @Override
    public void save(WeeklySalary weeklySalary) {
        weeklySalaryRepository.save(weeklySalary);
    }
    public WeeklySalary saveSalary(WeeklySalary weeklySalary) {
        return weeklySalaryRepository.save(weeklySalary);
    }

    @Override
    public WeeklySalary findByCoach_IdAndDate(Long id, Date date) {
        return weeklySalaryRepository.findByCoach_IdAndAndDate(id,date);
    }

    @Override
    public WeeklySalary findByPlayer_IdAndDate(Long id, Date date) {
        return weeklySalaryRepository.findByPlayer_IdAndAndDate(id,date);
    }

    @Override
    public void deleteAllByCoach_Id(long id) {
        weeklySalaryRepository.deleteAllByCoach_Id(id);
    }

    @Override
    public void deleteAllByPlayer_Id(long id) {
        weeklySalaryRepository.deleteAllByPlayer_Id(id);
    }

    @Override
    public void delete(Long id) {
        weeklySalaryRepository.deleteById(id);
    }

    @Override
    public List<WeeklySalary> findByCoach_Id(Long id) {
        return weeklySalaryRepository.findByCoach_Id(id);
    }

    @Override
    public List<WeeklySalary> findByPlayer_Id(Long id) {
        return weeklySalaryRepository.findByPlayer_Id(id);
    }
}
