package ru.restaurant_voting.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurant_voting.model.Restaurant;
import ru.restaurant_voting.repository.RestaurantRepository;

import java.util.List;

import static ru.restaurant_voting.util.ValidationUtil.*;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
@Cacheable("restaurants")
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public List<Restaurant> getTodayList() {
        log.info("get from database");
        return restaurantRepository.getTodayList();
    }

    public Restaurant getRestaurant(int id) {
        return (Restaurant) checkContains(getTodayList(), id);
    }
}
