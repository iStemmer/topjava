package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MealDAOImpl implements MealDAO {

    private ConcurrentMap<Long, Meal> mealMap = new ConcurrentHashMap<>();

    @Override
    public Long addMeal(Meal meal) {
        //append new meal
        mealMap.putIfAbsent(meal.getId(), meal);
        return meal.getId();
    }

    @Override
    public void updateMeal(Meal meal) {
        mealMap.merge(meal.getId(), meal, (meal1, meal2) -> meal1 = meal2);
    }

    @Override
    public void deleteMeal(Long id) {
        mealMap.remove(id);
    }

    @Override
    public Meal getMealById(Long id) {
        return mealMap.get(id);
    }

    @Override
    public List<Meal> getAll() {
        //temp realisation
        return new ArrayList<>(mealMap.values());
    }
}
