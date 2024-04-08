package ru.restaurant_voting.web.restaurant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.restaurant_voting.repository.RestaurantRepository;
import ru.restaurant_voting.to.RestaurantTo;
import ru.restaurant_voting.util.RestaurantUtil;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = RestaurantController.URL_USER_RESTAURANTS, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User Restaurant Controller")
public class RestaurantController {
    static final String URL_USER_RESTAURANTS = "/api/restaurants";

    private RestaurantRepository restaurantRepository;

    @GetMapping
    @Cacheable("restaurants")
    @Operation(summary = "Get today's restaurant list with menu")
    public List<RestaurantTo> getAll() {
        log.info("get today restaurant list");
        return RestaurantUtil.getTosList(restaurantRepository.getTodayList());
    }
}
