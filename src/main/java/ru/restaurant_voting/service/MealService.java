package ru.restaurant_voting.service;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurant_voting.model.Meal;
import ru.restaurant_voting.repository.MealRepository;
import ru.restaurant_voting.repository.RestaurantRepository;
import ru.restaurant_voting.to.MealTo;

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

    public void update(int restaurantId, MealTo meal) {
        mealRepository.update(meal.getId(), meal.getName(), meal.getPrice(), restaurantId);
    }
}
