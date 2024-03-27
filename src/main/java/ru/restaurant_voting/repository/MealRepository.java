package ru.restaurant_voting.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.restaurant_voting.model.Meal;

@Transactional(readOnly = true)
public interface MealRepository extends BaseRepository<Meal> {
}
