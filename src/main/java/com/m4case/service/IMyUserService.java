package com.m4case.service;

import com.m4case.model.MyUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IMyUserService extends IGeneralService<MyUser>, UserDetailsService {
}
