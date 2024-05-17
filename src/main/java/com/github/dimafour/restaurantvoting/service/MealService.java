package com.github.dimafour.restaurantvoting.service;

import com.github.dimafour.restaurantvoting.repository.MealRepository;
import com.github.dimafour.restaurantvoting.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.dimafour.restaurantvoting.model.Meal;

@Service
@AllArgsConstructor
@Transactional
@CacheEvict(value = "restaurants", allEntries = true)
public class MealService {
    private final MealRepository mealRepository;
    private final RestaurantRepository restaurantRepository;

    public Meal save(int restaurantId, Meal meal) {
        meal.setRestaurant(restaurantRepository.getExisted(restaurantId));
        return mealRepository.save(meal);
    }

    public void update(int restaurantId, Meal meal) {
        Meal oldMeal = mealRepository.getBelonged(meal.id(), restaurantId);
        meal.setDate(oldMeal.getDate());
        mealRepository.save(meal);
    }
}
