package com.m4case.service;

import com.m4case.model.Status;
import com.m4case.repository.IStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StatusService implements IStatusService {

    @Autowired
    private IStatusRepository statusRepository;

    @Override
    public Iterable<Status> findAll() {
        return statusRepository.findAll();
    }

    @Override
    public Optional<Status> findById(Long id) {
        return statusRepository.findById(id);
    }

    @Override
    public Object saveObj(Status status) {
        return statusRepository.save(status);
    }

    @Override
    public void save(Status status) {
        statusRepository.save(status);
    }

    @Override
    public void delete(Long id) {
        statusRepository.deleteById(id);
    }
}
