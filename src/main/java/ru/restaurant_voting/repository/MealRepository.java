package ru.restaurant_voting.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurant_voting.error.DataConflictException;
import ru.restaurant_voting.model.Meal;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MealRepository extends BaseRepository<Meal> {

    @Query("SELECT m FROM Meal m WHERE m.restaurant.id=:restaurantId AND m.meal_date=current_date")
    List<Meal> getMenu(int restaurantId);

    @Query("SELECT m FROM Meal m WHERE m.restaurant.id=:restaurantId")
    List<Meal> getHistory(int restaurantId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Meal m WHERE m.restaurant.id=:restaurantId AND m.meal_date=current_date")
    void deleteToday(int restaurantId);

    @Modifying
    @Transactional
    default List<Meal> rewrite(int restaurantId, List<Meal> menu) {
        deleteToday(restaurantId);
        return saveAll(menu);
    }

    @Query("SELECT m FROM Meal m WHERE m.id=:id and m.restaurant.id =:restaurantId")
    Optional<Meal> get(int id, int restaurantId);

    default Meal getBelonged(int id, int restaurantId) {
        return get(id, restaurantId).orElseThrow(
                () -> new DataConflictException("Meal id=" + id + " is not exist or doesn't belong to Restaurant id=" + restaurantId));
    }
}
