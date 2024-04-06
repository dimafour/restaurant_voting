package ru.restaurant_voting.web.meal;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restaurant_voting.model.Meal;
import ru.restaurant_voting.repository.MealRepository;
import ru.restaurant_voting.repository.RestaurantRepository;
import ru.restaurant_voting.service.MealService;
import ru.restaurant_voting.to.MealTo;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.restaurant_voting.util.MealUtil.*;
import static ru.restaurant_voting.util.ValidationUtil.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = AdminMealController.URL_ADMIN_MEAL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminMealController {

    private MealRepository mealRepository;
    private RestaurantRepository restaurantRepository;
    private MealService mealService;
    static final String URL_ADMIN_MEAL = "/api/admin/restaurants/{restaurantid}";

    @GetMapping("/menu")
    public List<MealTo> getMenu(@PathVariable int restaurantid) {
        log.info("get today's menu from restaurant id={}", restaurantid);
        return getTosList(mealRepository.getMenu(restaurantid));
    }

    @PostMapping(value = "/menu", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public List<MealTo> changeMenu(@PathVariable int restaurantid, @Valid @RequestBody List<MealTo> menuTo) {
        List<Meal> menu = getFromTo(menuTo);
        menu.forEach(meal -> {
            meal.setRestaurant(restaurantRepository.getReferenceById(restaurantid));
            meal.setMeal_date(LocalDate.now());
        });
        log.info("create menu {} in restaurant id={}", menuTo, restaurantid);
        List<Meal> rewrite = mealRepository.rewrite(restaurantid, menu);
        return getTosList(rewrite);
    }

    @DeleteMapping("/menu")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMenu(@PathVariable int restaurantid) {
        log.info("delete menu from restaurant id={}", restaurantid);
        restaurantRepository.deleteExisted(restaurantid);
    }

    @GetMapping("/meal/{id}")
    public ResponseEntity<Meal> get(@PathVariable int restaurantid, @PathVariable int id) {
        log.info("get meal id={} in restaurant id={}", id, restaurantid);
        return ResponseEntity.of(mealRepository.get(id, restaurantid));
    }

    @DeleteMapping("/meal/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantid, @PathVariable int id) {
        log.info("delete meal id={} in restaurant id={}", id, restaurantid);
        Meal meal = mealRepository.getBelonged(id, restaurantid);
        mealRepository.delete(meal);
    }

    @PutMapping(value = "/meal/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int restaurantid, @PathVariable int id, @Valid @RequestBody Meal meal) {
        log.info("update {} in restaurant id={}", meal, restaurantid);
        assureIdConsistent(meal, id);
        mealRepository.getBelonged(id, restaurantid);
        mealService.save(restaurantid, meal);
    }

    @PostMapping(value = "/meal", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> create(@PathVariable int restaurantid, @Valid @RequestBody Meal meal) {
        log.info("create {} in restaurant id={}", meal, restaurantid);
        checkNew(meal);
        Meal created = mealService.save(restaurantid, meal);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/admin/restaurants/" + restaurantid + "/meal" + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

}
