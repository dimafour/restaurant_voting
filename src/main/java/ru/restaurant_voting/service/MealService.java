package ru.restaurant_voting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurant_voting.model.Meal;
import ru.restaurant_voting.repository.MealRepository;
import ru.restaurant_voting.repository.RestaurantRepository;

@Service
@AllArgsConstructor
public class MealService {
    private final MealRepository mealRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public Meal save(int restaurantId, Meal meal) {
        meal.setRestaurant(restaurantRepository.getExisted(restaurantId));
        return mealRepository.save(meal);
    }
}
