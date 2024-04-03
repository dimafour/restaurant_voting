package ru.restaurant_voting.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurant_voting.model.Meal;

import java.util.List;

@Transactional(readOnly = true)
public interface MealRepository extends BaseRepository<Meal> {

    @Query("SELECT m FROM Meal m WHERE m.restaurant.id=:restaurantId AND m.meal_date=current_date")
    List<Meal> getMenu(int restaurantId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Meal m WHERE m.restaurant.id=:restaurantid AND m.meal_date=current_date")
    int delete(int restaurantid);

    @Modifying
    @Transactional
    default List<Meal> rewrite(int restaurantid, List<Meal> menu) {
        delete(restaurantid);
        return saveAll(menu);
    }
}
