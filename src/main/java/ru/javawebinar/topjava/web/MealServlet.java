package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDAO;
import ru.javawebinar.topjava.dao.MealDAOImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.Counter;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private MealDAO dao = new MealDAOImpl();

    private Counter counter = new Counter();

    private static String LIST_MEAL = "/meals.jsp";

    private static String FORM_MEAL = "/mealEdit.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        String action = request.getParameter("action");
        if (action == null) action = "";
        String forwardTo;
        if (action.equalsIgnoreCase("add")) {
            forwardTo = FORM_MEAL;
        } else if (action.equalsIgnoreCase("update")) {
            forwardTo = FORM_MEAL;
            Long mealId = Long.parseLong(request.getParameter("id"));
            request.setAttribute("meal", dao.getMealById(mealId));

        } else if (action.equalsIgnoreCase("delete")) {
            Long mealId = Long.parseLong(request.getParameter("id"));
            dao.deleteMeal(mealId);
            response.sendRedirect("meals");
            return;
        } else {
            request.setAttribute("listMeal", MealsUtil.getFilteredWithExceededInOnePass(dao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000));
            forwardTo = LIST_MEAL;
        }
        request.getRequestDispatcher(forwardTo).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        log.debug("update & add will be here");
        String description = request.getParameter("description");
        String time = request.getParameter("dateTime");
        int calories = Integer.valueOf(request.getParameter("calories"));
        Meal meal;
        LocalDate dateTime = LocalDate.parse(time, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        if (request.getParameter("id") == null) {
            meal = new Meal(dateTime.atTime(0, 0), description, calories, counter.getIncrement());
            dao.addMeal(meal);
        } else {
            meal = new Meal(dateTime.atTime(0, 0), description, calories, Long.parseLong(request.getParameter("id")));
            dao.updateMeal(meal);
        }
        response.sendRedirect("meals");
    }

}
