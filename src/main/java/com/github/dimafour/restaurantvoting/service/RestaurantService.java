package com.github.dimafour.restaurantvoting.service;

import com.github.dimafour.restaurantvoting.model.Restaurant;
import com.github.dimafour.restaurantvoting.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@AllArgsConstructor
public class RestaurantService {
    private RestaurantRepository restaurantRepository;

    @Cacheable("restaurants")
    public List<Restaurant> getTodayList() {
        log.info("get today's restaurant list with menu from database");
        return restaurantRepository.getListByDate(LocalDate.now());
    }

    @Cacheable(value = "restaurant", key = "#id")
    public Restaurant getRestaurant(int id) {
        log.info("get restaurant from database");
        return restaurantRepository.getExisted(id);
    }
}
