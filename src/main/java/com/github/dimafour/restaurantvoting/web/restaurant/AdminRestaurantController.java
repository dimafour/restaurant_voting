package com.github.dimafour.restaurantvoting.web.restaurant;

import com.github.dimafour.restaurantvoting.model.Restaurant;
import com.github.dimafour.restaurantvoting.repository.RestaurantRepository;
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

import java.net.URI;
import java.util.List;

import static com.github.dimafour.restaurantvoting.util.ValidationUtil.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = AdminRestaurantController.URL_ADMIN_RESTAURANTS, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Admin Restaurant Controller", description = "Allows to manage restaurants")
public class AdminRestaurantController {
    static final String URL_ADMIN_RESTAURANTS = "/api/admin/restaurants";

    private RestaurantRepository restaurantRepository;

    @GetMapping
    @Operation(summary = "Get all existed restaurant in repository")
    public List<Restaurant> getAll() {
        log.info("get all restaurants");
        return restaurantRepository.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get existed restaurant by ID")
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        log.info("get restaurant id={}", id);
        return ResponseEntity.of(restaurantRepository.get(id));
    }

    @GetMapping("/by-name")
    @Operation(summary = "Get existed restaurant by name")
    public ResponseEntity<Restaurant> getByName(@RequestParam String name) {
        log.info("get restaurant {}", name);
        return ResponseEntity.of(restaurantRepository.getRestaurantByName(name));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(value = "restaurants", allEntries = true)
    @Operation(summary = "Create new restaurant")
    public ResponseEntity<Restaurant> create(@Valid @RequestBody Restaurant restaurant) {
        checkNew(restaurant);
        log.info("create {}", restaurant);
        Restaurant created = restaurantRepository.save(restaurant);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL_ADMIN_RESTAURANTS + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants", allEntries = true)
    @Operation(summary = "Update existed restaurant by ID")
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        assureIdConsistent(restaurant, id);
        log.info("update restaurant {}", restaurant);
        restaurantRepository.save(restaurant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants", allEntries = true)
    @Operation(summary = "Delete existed restaurant by ID")
    public void delete(@PathVariable int id) {
        log.info("delete restaurant id={}", id);
        restaurantRepository.deleteExisted(id);
    }
}
