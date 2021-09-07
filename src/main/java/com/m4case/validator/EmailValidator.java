package com.m4case.validator;

import com.m4case.model.MyUser;
import com.m4case.repository.IMyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EmailValidator implements Validator {
    @Autowired
    private IMyUserRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        MyUser myUser = (MyUser) target;
        MyUser db = repository.findByEmail(myUser.getEmail());
        if(db!= null){
            errors.rejectValue("email", "Duplicate.MyUser.email");
        }
    }
}
