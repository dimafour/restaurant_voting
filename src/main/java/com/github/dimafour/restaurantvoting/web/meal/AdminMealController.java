package com.github.dimafour.restaurantvoting.web.meal;

import com.github.dimafour.restaurantvoting.repository.MealRepository;
import com.github.dimafour.restaurantvoting.service.MealService;
import com.github.dimafour.restaurantvoting.to.MealTo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.github.dimafour.restaurantvoting.model.Meal;
import com.github.dimafour.restaurantvoting.model.Restaurant;
import com.github.dimafour.restaurantvoting.service.RestaurantService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.github.dimafour.restaurantvoting.util.MealUtil.*;
import static com.github.dimafour.restaurantvoting.util.ValidationUtil.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = AdminMealController.URL_ADMIN_MEAL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Admin Meal controller", description = "Allows to manage every single meal & change the whole menu for today")
public class AdminMealController {

    static final String URL_ADMIN_MEAL = "/api/admin/restaurants/{restaurantId}";
    private MealRepository mealRepository;
    private RestaurantService restaurantService;
    private MealService mealService;

    @GetMapping("/menu")
    @Operation(summary = "Get menu by restaurant ID (default - for today, or show all history)")
    public List<Meal> getMenu(@PathVariable int restaurantId, @RequestParam(defaultValue = "false") boolean showHistory) {
        log.info("get menu for {} from restaurant id={}", showHistory ? "all the time" : "today", restaurantId);
        return showHistory ? mealRepository.getHistory(restaurantId) : mealRepository.getMenu(restaurantId);
    }

    @PostMapping(value = "/menu", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(value = "restaurants", allEntries = true)
    @Operation(summary = "Change today's menu for Restaurant by ID",
            description = "! Delete existed today's menu & save new !")
    public List<MealTo> changeMenu(@PathVariable int restaurantId, @Valid @RequestBody List<MealTo> menuTo) {
        List<Meal> menu = getFromToList(menuTo);
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        menu.forEach(meal -> {
            meal.setRestaurant(restaurant);
            meal.setDate(LocalDate.now());
        });
        log.info("create menu {} in restaurant id={}", menuTo, restaurantId);
        return getTosList(mealRepository.rewrite(restaurantId, menu));
    }

    @DeleteMapping("/menu")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants", allEntries = true)
    @Operation(summary = "Delete today's menu by restaurant ID")
    public void deleteMenu(@PathVariable int restaurantId) {
        log.info("delete menu from restaurant id={}", restaurantId);
        mealRepository.deleteToday(restaurantId);
    }

    @GetMapping("/meal/{id}")
    @Operation(summary = "Get meal by ID in restaurant")
    public ResponseEntity<Meal> get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get meal id={} in restaurant id={}", id, restaurantId);
        return ResponseEntity.of(mealRepository.get(id, restaurantId));
    }

    @DeleteMapping("/meal/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants", allEntries = true)
    @Operation(summary = "Delete meal by ID in restaurant")
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        Meal meal = mealRepository.getBelonged(id, restaurantId);
        log.info("delete meal id={} in restaurant id={}", id, restaurantId);
        mealRepository.delete(meal);
    }

    @PutMapping(value = "/meal/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update meal by ID in restaurant")
    public void update(@PathVariable int restaurantId, @PathVariable int id, @Valid @RequestBody MealTo mealTo) {
        assureIdConsistent(mealTo, id);
        log.info("update {} in restaurant id={}", mealTo, restaurantId);
        mealService.update(restaurantId, createFromTo(mealTo));
    }

    @PostMapping(value = "/meal", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add new meal to today's menu in restaurant")
    public ResponseEntity<Meal> create(@PathVariable int restaurantId, @Valid @RequestBody MealTo mealTo) {
        checkNew(mealTo);
        log.info("create {} in restaurant id={}", mealTo, restaurantId);
        Meal created = mealService.save(restaurantId, createFromTo(mealTo));
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/admin/restaurants/" + restaurantId + "/meal" + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uri).body(created);
    }
}
