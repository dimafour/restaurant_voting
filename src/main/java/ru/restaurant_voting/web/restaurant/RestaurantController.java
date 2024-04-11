package ru.restaurant_voting.web.restaurant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.restaurant_voting.service.RestaurantService;
import ru.restaurant_voting.to.RestaurantTo;

import java.util.List;

import static ru.restaurant_voting.util.RestaurantUtil.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = RestaurantController.URL_USER_RESTAURANTS, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User Restaurant Controller")
public class RestaurantController {
    static final String URL_USER_RESTAURANTS = "/api/restaurants";

    private RestaurantService restaurantService;

    @GetMapping
    @Operation(summary = "Get today's restaurant list with menu")
    public List<RestaurantTo> getAll() {
        log.info("get today's restaurants list with menu");
        return getTosList(restaurantService.getTodayList());
    }
}
