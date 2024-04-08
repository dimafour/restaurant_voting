package ru.restaurant_voting.web.meal;

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
@Tag(name = "Admin Meal controller", description = "Allows to manage every single meal & change the whole menu for today")
public class AdminMealController {

    private MealRepository mealRepository;
    private RestaurantRepository restaurantRepository;
    private MealService mealService;
    static final String URL_ADMIN_MEAL = "/api/admin/restaurants/{restaurantId}";

    @GetMapping("/menu")
    @Operation(summary = "Get menu for today by restaurant ID")
    public List<MealTo> getMenu(@PathVariable int restaurantId) {
        log.info("get today's menu from restaurant id={}", restaurantId);
        return getTosList(mealRepository.getMenu(restaurantId));
    }

    @PostMapping(value = "/menu", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(value = "restaurants", allEntries = true)
    @Operation(summary = "Change today's menu for Restaurant by ID",
            description = "! Delete existed today's menu & save new !")
    public List<MealTo> changeMenu(@PathVariable int restaurantId, @Valid @RequestBody List<MealTo> menuTo) {
        List<Meal> menu = getFromToList(menuTo);
        menu.forEach(meal -> {
            meal.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
            meal.setMeal_date(LocalDate.now());
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
        mealRepository.delete(restaurantId);
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
        log.info("delete meal id={} in restaurant id={}", id, restaurantId);
        Meal meal = mealRepository.getBelonged(id, restaurantId);
        mealRepository.delete(meal);
    }

    @PatchMapping(value = "/meal/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update meal by ID in restaurant")
    public void update(@PathVariable int restaurantId, @PathVariable int id, @Valid @RequestBody MealTo mealTo) {
        assureIdConsistent(mealTo, id);
        log.info("update {} in restaurant id={}", mealTo, restaurantId);
        mealService.update(restaurantId, mealTo);
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
