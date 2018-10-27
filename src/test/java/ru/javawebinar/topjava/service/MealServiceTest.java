package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.assertMatch;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
}
)
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(MealTestData.MEAL_ID, UserTestData.USER_ID);
        assertMatch(meal, MealTestData.MEAL_1);
    }


    @Test(expected = NotFoundException.class)
    public void getNotMy() throws Exception {
        Meal meal = service.get(MealTestData.MEAL_ID, UserTestData.ADMIN_ID);
    }


    @Test
    public void delete() {
        service.delete(MealTestData.MEAL_ID, UserTestData.USER_ID);
        assertMatch(service.getAll(UserTestData.USER_ID), MealTestData.MEAL_3, MealTestData.MEAL_2);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotMy() throws Exception {
        service.delete(MealTestData.MEAL_ID, UserTestData.ADMIN_ID);
    }


    @Test
    public void getBetweenDates() {
        List<Meal> betweenDates = service.getBetweenDates(
                LocalDate.of(2015, 05, 30),
                LocalDate.of(2015, 06, 1),
                UserTestData.USER_ID);
        assertMatch(betweenDates, MealTestData.MEAL_3, MealTestData.MEAL_2, MealTestData.MEAL_1);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> betweenTimes = service.getBetweenDateTimes(
                LocalDateTime.of(2015, 05, 30, 0, 0),
                LocalDateTime.of(2015, 05, 30, 23, 59),
                UserTestData.USER_ID
        );
        assertMatch(betweenTimes, MealTestData.MEAL_3, MealTestData.MEAL_2, MealTestData.MEAL_1);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(UserTestData.USER_ID);
        assertMatch(all, MealTestData.MEAL_3, MealTestData.MEAL_2, MealTestData.MEAL_1);
    }

    @Test
    public void update() {
        Meal updated = new Meal(MealTestData.MEAL_1);
        updated.setDescription("Обновил12233");
        updated.setCalories(125);
        service.update(updated, UserTestData.USER_ID);
        assertMatch(service.get(MealTestData.MEAL_ID, UserTestData.USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotMy() throws Exception {
        Meal updated = new Meal(MealTestData.MEAL_1);
        updated.setDescription("Обновил12233");
        updated.setCalories(125);
        service.update(updated, UserTestData.ADMIN_ID);
    }


    @Test
    public void create() {
        Meal newMeal = new Meal(
                null,
                LocalDateTime.of(2015, Month.MAY, 30, 21, 0),
                "Ужин",
                500);
        Meal created = service.create(newMeal, UserTestData.USER_ID);
        newMeal.setId(created.getId());
        assertMatch(service.getAll(UserTestData.USER_ID), newMeal, MealTestData.MEAL_3, MealTestData.MEAL_2, MealTestData.MEAL_1);
    }
}