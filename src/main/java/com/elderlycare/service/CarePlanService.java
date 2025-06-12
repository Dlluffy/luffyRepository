package com.elderlycare.service;

import com.elderlycare.model.CarePlan;

import java.util.List;

public interface CarePlanService {
    List<CarePlan> listAll();

    void save(CarePlan cp);

    CarePlan getById(Long id);

    void update(CarePlan cp);

    void delete(Long id);

    void archive(Long id);

    CarePlan getPlanByElderId(Long elderId);

}
