package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
//        for (UserMealWithExceed meal: getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000) ){
//            System.out.println(meal);
//        }
    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExceed> result = new ArrayList<>();
        Map<LocalDate, Integer> map = new HashMap<>();
        List<UserMeal> filteredMeal = new ArrayList<>();
        for (UserMeal meal: mealList){
            map.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), (oldValue, newValue) -> oldValue + newValue);
            if (TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)){
                filteredMeal.add(meal);
            }
        }
        for (UserMeal filtered: filteredMeal){
            result.add(new UserMealWithExceed(
               filtered.getDateTime(),
               filtered.getDescription(),
               filtered.getCalories(),
               map.get(filtered.getDateTime().toLocalDate()) > caloriesPerDay ));
        }
        return result;
    }
}
