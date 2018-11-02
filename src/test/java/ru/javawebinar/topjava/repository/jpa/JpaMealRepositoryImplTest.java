package ru.javawebinar.topjava.repository.jpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class JpaMealRepositoryImplTest {

    @Autowired
    MealRepository repository;

    @Test
    public void save() {
        Meal updated = getUpdated();
        repository.save(updated, USER_ID);
        assertMatch(repository.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, updated);
    }

    @Test
    public void saveNew() {
        Meal created = getCreated();
        repository.save(created, USER_ID);
        assertMatch(repository.getAll(USER_ID), created, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void delete() {
        repository.delete(MEAL1_ID, USER_ID);
        MealTestData.assertMatch(repository.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    public void get() {
        MealTestData.assertMatch(repository.get(MEAL1_ID, USER_ID), MEAL1);
    }

    @Test
    public void getAll() {
        assertMatch(repository.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void getBetween() {
        assertMatch(repository.getBetween(
                LocalDateTime.of(2015, Month.MAY, 30, 0, 0),
                LocalDateTime.of(2015, Month.MAY, 30, 23, 59),
                USER_ID),
                MEAL3, MEAL2, MEAL1);
    }
}