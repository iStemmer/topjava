package ru.javawebinar.topjava.repository.jdbc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.assertMatch;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
}
)
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class JdbcMealRepositoryImplTest {

    @Autowired
    MealRepository repository;

    @Test
    public void save() {
        Meal updated = new Meal(MealTestData.MEAL_1);
        updated.setDescription("Pfhfsds");
        updated.setCalories(12);
        repository.save(updated, UserTestData.USER_ID);
        assertMatch(repository.get(MealTestData.MEAL_ID, UserTestData.USER_ID), updated);

    }

    @Test
    public void delete() {
        repository.delete(MealTestData.MEAL_ID, UserTestData.USER_ID);
        assertMatch(repository.getAll(UserTestData.USER_ID), MealTestData.MEAL_3, MealTestData.MEAL_2);

    }

    @Test
    public void get() {
        Meal meal = repository.get(MealTestData.MEAL_ID, UserTestData.USER_ID);
        assertMatch(meal, MealTestData.MEAL_1);
    }

    @Test
    public void getAll() {
        List<Meal> all = repository.getAll(UserTestData.USER_ID);
        assertMatch(all, MealTestData.MEAL_3, MealTestData.MEAL_2, MealTestData.MEAL_1);
    }

    @Test
    public void getBetween() {
        List<Meal> all = repository.getBetween(
                LocalDateTime.MIN,
                LocalDateTime.MAX,
                UserTestData.USER_ID);
        assertMatch(all, MealTestData.MEAL_3, MealTestData.MEAL_2, MealTestData.MEAL_1);
    }
}