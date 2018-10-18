package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> this.save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {

        if (meal.getUserId() != userId) return null;
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            log.info("save {}", meal);
            repository.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but absent in storage
        log.info("save {}", meal);
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {}", id);
        Meal meal = repository.get(id);
        if (meal != null) {
            if (meal.getUserId() == userId) {
                repository.remove(id);
                return true;
            }
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {}", id);
        Meal meal = repository.get(id);
        if (meal.getUserId() == userId) {
            return repository.get(id);
        }
        return null;
    }

//only by userId & in reverse order
    @Override
    public List<Meal> getAll(int userId) {
        log.info("get All");
        return repository.values()
                .stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAllByDate(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getAllByDate");
        log.info(startDate.toString());
        log.info(endDate.toString());
        log.info("How to boolean:" + DateTimeUtil.isBetween(
                LocalDateTime.of(2015, 05, 30, 11, 12),
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59)));
        return getAll(userId)
                .stream()
                .filter(meal ->
                        DateTimeUtil.isBetween(
                                meal.getDateTime(),
                                startDate.atStartOfDay(),
                                endDate.atTime(23, 59, 59)
                        ))
                .collect(Collectors.toList());
    }
}

