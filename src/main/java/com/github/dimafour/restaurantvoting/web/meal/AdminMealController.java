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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.github.dimafour.restaurantvoting.model.Meal;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.github.dimafour.restaurantvoting.util.MealUtil.*;
import static com.github.dimafour.restaurantvoting.util.ValidationUtil.*;

@Slf4j
@RestController
@AllArgsConstructor
@Validated
@RequestMapping(value = AdminMealController.URL_ADMIN_MEAL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Admin Meal controller", description = "Allows to manage every single meal & change the whole menu for today")
public class AdminMealController {

    static final String URL_ADMIN_MEAL = "/api/admin/restaurants/{restaurantId}";
    private MealRepository mealRepository;
    private MealService mealService;

    @GetMapping("/meals")
    @Operation(summary = "Get menu by restaurant ID (default - for today, or on the specific date)")
    public List<Meal> getMenu(@PathVariable int restaurantId,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate notNullDate = date == null ? LocalDate.now() : date;
        log.info("get menu dated {} from restaurant id={}", notNullDate, restaurantId);
        return mealRepository.getMenuByIdAndDate(restaurantId, notNullDate);
    }

    @PutMapping(value = "/meals", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(value = "restaurants", allEntries = true)
    @Operation(summary = "Change menu for Restaurant by ID for today, or on the specific date",
            description = "! Delete existed menu on this date & save new !")
    public List<MealTo> setMenu(@PathVariable int restaurantId, @Valid @RequestBody List<@Valid MealTo> menuTo,
                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate notNullDate = date == null ? LocalDate.now() : date;
        log.info("create menu {} in restaurant id={} on {}", menuTo, restaurantId, notNullDate);
        return getTosList(mealService.rewrite(restaurantId, getFromToList(menuTo), notNullDate));
    }

    @DeleteMapping("/meals")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants", allEntries = true)
    @Operation(summary = "Delete menu by restaurant ID for today, or on the specific date")
    public void deleteMenu(@PathVariable int restaurantId,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate notNullDate = date == null ? LocalDate.now() : date;
        log.info("delete menu from restaurant id={} dated {}", restaurantId, notNullDate);
        mealRepository.deleteByIdAndDate(restaurantId, notNullDate);
    }

    @GetMapping("/meals/{id}")
    @Operation(summary = "Get meal by ID in restaurant")
    public ResponseEntity<Meal> get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get meal id={} in restaurant id={}", id, restaurantId);
        return ResponseEntity.of(mealRepository.get(id, restaurantId));
    }

    @DeleteMapping("/meals/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants", allEntries = true)
    @Operation(summary = "Delete meal by ID in restaurant")
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        Meal meal = mealRepository.getBelonged(id, restaurantId);
        log.info("delete meal id={} in restaurant id={}", id, restaurantId);
        mealRepository.delete(meal);
    }

    @PutMapping(value = "/meals/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants", allEntries = true)
    @Operation(summary = "Update meal by ID in restaurant")
    public void update(@PathVariable int restaurantId, @PathVariable int id, @Valid @RequestBody Meal meal) {
        assureIdConsistent(meal, id);
        log.info("update {} in restaurant id={}", meal, restaurantId);
        mealService.update(restaurantId, meal);
    }

    @PatchMapping(value = "/meals", consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(value = "restaurants", allEntries = true)
    @Operation(summary = "Add new meal to the menu in restaurant for today, or on the specific date")
    public ResponseEntity<Meal> create(@PathVariable int restaurantId, @Valid @RequestBody Meal meal) {
        checkNew(meal);
        log.info("create {} in restaurant id={}", meal, restaurantId);
        Meal created = mealService.save(restaurantId, meal);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/admin/restaurants/" + restaurantId + "/meals/" + "{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uri).body(created);
    }
}
