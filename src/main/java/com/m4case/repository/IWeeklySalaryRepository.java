package com.m4case.repository;

import com.m4case.model.WeeklySalary;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;

public interface IWeeklySalaryRepository extends CrudRepository<WeeklySalary, Long> {
    List<WeeklySalary> findByCoach_Id (Long id);
    List<WeeklySalary> findByPlayer_Id (Long id);
    WeeklySalary findByCoach_IdAndAndDate(long id, Date date);
    WeeklySalary findByPlayer_IdAndAndDate(long id, Date date);
    void deleteAllByCoach_Id(long id);
    void deleteAllByPlayer_Id(long id);
}
