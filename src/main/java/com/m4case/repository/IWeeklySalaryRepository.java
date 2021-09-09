package com.m4case.repository;

import com.m4case.model.WeeklySalary;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;

public interface IWeeklySalaryRepository extends CrudRepository<WeeklySalary, Long> {
    WeeklySalary findByCoach_Id (Long id);
    WeeklySalary findByPlayer_Id (Long id);
    WeeklySalary findByCoach_IdAndAndDate(long id, Date date);
    WeeklySalary findByPlayer_IdAndAndDate(long id, Date date);
}
