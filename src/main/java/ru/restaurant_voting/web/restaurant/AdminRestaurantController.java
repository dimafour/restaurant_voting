package ru.restaurant_voting.web.restaurant;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restaurant_voting.model.Restaurant;
import ru.restaurant_voting.repository.RestaurantRepository;

import java.net.URI;
import java.util.List;

import static ru.restaurant_voting.util.ValidationUtil.assureIdConsistent;
import static ru.restaurant_voting.util.ValidationUtil.checkNew;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = AdminRestaurantController.URL_ADMIN_RESTAURANTS, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantController {
    static final String URL_ADMIN_RESTAURANTS = "/api/admin/restaurants";

    private RestaurantRepository restaurantRepository;

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("get all restaurants");
        return restaurantRepository.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        log.info("get restaurant id={}", id);
        return ResponseEntity.of(restaurantRepository.get(id));
    }

    @GetMapping("/by-name")
    public ResponseEntity<Restaurant> getByName(@RequestParam String name) {
        log.info("get restaurant {}", name);
        return ResponseEntity.of(restaurantRepository.getRestaurantByName(name));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
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
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        assureIdConsistent(restaurant, id);
        log.info("update restaurant {}", restaurant);
        restaurantRepository.save(restaurant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete restaurant id={}", id);
        restaurantRepository.deleteExisted(id);
    }
}
