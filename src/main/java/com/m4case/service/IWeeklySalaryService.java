package com.m4case.service;

import com.m4case.model.WeeklySalary;

import java.sql.Date;
import java.util.List;

public interface IWeeklySalaryService extends IGeneralService<WeeklySalary> {
    List<WeeklySalary> findByCoach_Id (Long id);
    List<WeeklySalary> findByPlayer_Id (Long id);
    WeeklySalary saveSalary(WeeklySalary weeklySalary);
    WeeklySalary findByCoach_IdAndDate(Long id, Date date);
    WeeklySalary findByPlayer_IdAndDate(Long id, Date date);
    void deleteAllByCoach_Id(long id);
    void deleteAllByPlayer_Id(long id);
}
