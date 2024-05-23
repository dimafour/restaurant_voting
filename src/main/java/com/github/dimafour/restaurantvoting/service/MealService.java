package com.github.dimafour.restaurantvoting.service;

import com.github.dimafour.restaurantvoting.model.Restaurant;
import com.github.dimafour.restaurantvoting.repository.MealRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.dimafour.restaurantvoting.model.Meal;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class MealService {
    private final MealRepository mealRepository;
    private final RestaurantService restaurantService;

    public Meal save(int restaurantId, Meal meal) {
        meal.setRestaurant(restaurantService.getRestaurant(restaurantId));
        return mealRepository.save(meal);
    }

    public void update(int restaurantId, Meal meal) {
        mealRepository.getBelonged(meal.id(), restaurantId);
        meal.setRestaurant(restaurantService.getRestaurant(restaurantId));
        mealRepository.save(meal);
    }

    public List<Meal> rewrite(int restaurantId, List<Meal> menu, LocalDate date) {
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        menu.forEach(meal -> {
            meal.setRestaurant(restaurant);
            meal.setDate(date);
        });
        return mealRepository.rewrite(restaurantId, menu);
    }
}
