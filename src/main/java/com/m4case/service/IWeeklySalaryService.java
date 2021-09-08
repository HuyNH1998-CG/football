package com.m4case.service;

import com.m4case.model.WeeklySalary;

public interface IWeeklySalaryService extends IGeneralService<WeeklySalary> {
    WeeklySalary findByCoach_Id (Long id);
    WeeklySalary findByPlayer_Id (Long id);
}
