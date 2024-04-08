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

    @Modifying
    @Transactional
    @Query("DELETE FROM Meal m WHERE m.restaurant.id=:restaurantId AND m.meal_date=current_date")
    int delete(int restaurantId);

    @Modifying
    @Transactional
    default List<Meal> rewrite(int restaurantId, List<Meal> menu) {
        delete(restaurantId);
        return saveAll(menu);
    }

    @Query("SELECT m FROM Meal m WHERE m.id=:id and m.restaurant.id =:restaurantId")
    Optional<Meal> get(int id, int restaurantId);

    @Transactional
    @Modifying
    @Query("UPDATE Meal m SET m.name=:name, m.price=:price WHERE m.id=:id AND m.restaurant.id=:restaurantId")
    void update(int id, String name, int price, int restaurantId);

    default void getBelonged(int id, int restaurantId) {
        get(id, restaurantId).orElseThrow(
                () -> new DataConflictException("Meal id=" + id + " is not exist or doesn't belong to Restaurant id=" + restaurantId));
    }
}
