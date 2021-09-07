package com.m4case.service;

import com.m4case.model.MyUser;
import com.m4case.repository.IMyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class MyUserService implements IMyUserService {
    @Autowired
    private IMyUserRepository userRepository;

    @Override
    public Iterable<MyUser> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<MyUser> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void save(MyUser myUser) {
        userRepository.save(myUser);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser myUser = userRepository.findByEmail(username);
        ArrayList<GrantedAuthority> list = new ArrayList<>();
        list.add(myUser.getRole());
        User user = new User(myUser.getEmail(), myUser.getPassword(), list);
        return user;
    }
}
