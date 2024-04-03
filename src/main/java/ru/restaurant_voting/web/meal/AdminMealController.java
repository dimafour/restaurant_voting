package ru.restaurant_voting.web.meal;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.restaurant_voting.model.Meal;
import ru.restaurant_voting.repository.MealRepository;
import ru.restaurant_voting.repository.RestaurantRepository;
import ru.restaurant_voting.to.MealTo;

import java.time.LocalDate;
import java.util.List;

import static ru.restaurant_voting.util.MealUtil.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = AdminMealController.URL_ADMIN_MEAL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminMealController {

    private MealRepository mealRepository;
    private RestaurantRepository restaurantRepository;
    static final String URL_ADMIN_MEAL = "/api/admin/restaurants/{restaurantid}/menu";

    @GetMapping
    public List<MealTo> getMenu(@PathVariable int restaurantid) {
        log.info("get today's menu from restaurant id={}", restaurantid);
        return getTosList(mealRepository.getMenu(restaurantid));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public List<MealTo> changeMenu(@PathVariable int restaurantid, @RequestBody List<MealTo> menuTo) {
        List<Meal> menu = getFromTo(menuTo);
        menu.forEach(meal -> {
            meal.setRestaurant(restaurantRepository.getReferenceById(restaurantid));
            meal.setMeal_date(LocalDate.now());
        });
        log.info("create menu {} in restaurant id={}", menuTo, restaurantid);
        List<Meal> rewrite = mealRepository.rewrite(restaurantid, menu);
        return getTosList(rewrite);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantid) {
        log.info("delete menu from restaurant id={}", restaurantid);
        restaurantRepository.deleteExisted(restaurantid);
    }
}
