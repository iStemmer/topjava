package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDAO {
    Long addMeal(Meal meal);

    void updateMeal(Meal meal);

    void deleteMeal(Long id);

    Meal getMealById(Long id);

    List<Meal> getAll();

}
