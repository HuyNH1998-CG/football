package com.m4case.service;

import com.m4case.model.Coach;

public interface ICoachService extends IGeneralService<Coach> {
    Coach findByEmail(String email);
}
