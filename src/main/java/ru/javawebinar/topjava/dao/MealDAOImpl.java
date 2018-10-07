package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MealDAOImpl implements MealDAO {

    private ConcurrentMap<Long, Meal> mealMap = new ConcurrentHashMap<>();
    private Long recordId = 0L;

    public synchronized Long getValue() {
        return recordId;
    }

    public synchronized Long getIncrement() {
        return ++recordId;
    }

    @Override
    public Long addMeal(Meal meal) {
        //append new meal
        mealMap.putIfAbsent(meal.getId(), meal);
        return null;
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
        if (mealMap.size() == 0) {
            addMeal(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500, getIncrement()));
            addMeal(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000, getIncrement()));
            addMeal(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500, getIncrement()));
            addMeal(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000, getIncrement()));
            addMeal(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500, getIncrement()));
            addMeal(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510, getIncrement()));
        }
        return new ArrayList<>(mealMap.values());
    }
}
