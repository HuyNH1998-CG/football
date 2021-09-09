package com.m4case.service;

import com.m4case.model.WeeklySalary;

import java.sql.Date;

public interface IWeeklySalaryService extends IGeneralService<WeeklySalary> {
    WeeklySalary findByCoach_Id (Long id);
    WeeklySalary findByPlayer_Id (Long id);
    WeeklySalary saveSalary(WeeklySalary weeklySalary);
    WeeklySalary findByCoach_IdAndDate(Long id, Date date);
    WeeklySalary findByPlayer_IdAndDate(Long id, Date date);
}
