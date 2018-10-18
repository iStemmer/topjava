package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.mock.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController controller;
    ConfigurableApplicationContext context;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        //spring initialisation
        context = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = context.getBean(MealRestController.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        StringBuilder stringParam = new StringBuilder();
        if (request.getRequestURI().contains("meals")) {
            //append string parameters to responsestring
            if (!request.getParameter("startDate").equals("")) {
                stringParam.append("&");
                stringParam.append("startDate=").append(request.getParameter("startDate"));
            }
            if (!request.getParameter("endDate").equals("")) {
                stringParam.append("&");
                stringParam.append("endDate=").append(request.getParameter("endDate"));
            }
            if (!request.getParameter("startTime").equals("")) {
                stringParam.append("&");
                stringParam.append("startTime=").append(request.getParameter("startTime"));
            }
            if (!request.getParameter("endTime").equals(" ")) {
                stringParam.append("&");
                stringParam.append("endTime=").append(request.getParameter("endTime"));
            }

        } else {
            String id = request.getParameter("id");
            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")), SecurityUtil.authUserId());
            log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
            if (meal.isNew()) {
                controller.create(meal);
            } else controller.update(meal, meal.getId());
        }
        if (stringParam.capacity() > 0) stringParam.insert(0, "?action=filter");
        response.sendRedirect("meals" + stringParam.toString());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, SecurityUtil.authUserId()) :
                        controller.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;

            case "filter":
                log.info("getByFilter");
                request.setAttribute("startDate", request.getParameter("startDate"));
                request.setAttribute("endDate", request.getParameter("endDate"));
                request.setAttribute("startTime", request.getParameter("startTime"));
                request.setAttribute("endTime", request.getParameter("endTime"));
                List<MealWithExceed> meals = controller.getAllByDate(
                    LocalDate.parse(request.getParameter("startDate")),
                    LocalTime.parse(request.getParameter("startTime")),
                    LocalDate.parse(request.getParameter("endDate")),
                    LocalTime.parse(request.getParameter("endTime"))

            );
                log.info("Getting the size" + meals.size());
                request.setAttribute("meals",
                        controller.getAllByDate(
                                LocalDate.parse(request.getParameter("startDate")),
                                LocalTime.parse(request.getParameter("startTime")),
                                LocalDate.parse(request.getParameter("endDate")),
                                LocalTime.parse(request.getParameter("endTime"))
                        ));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;

            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals",
                        controller.getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    @Override
    public void destroy() {
        super.destroy();
        context.close();
    }
}
