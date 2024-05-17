package com.github.dimafour.restaurantvoting.service;

import com.github.dimafour.restaurantvoting.model.Restaurant;
import com.github.dimafour.restaurantvoting.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.github.dimafour.restaurantvoting.util.ValidationUtil.*;

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
        return checkContains(getTodayList(), id);
    }
}
