package com.github.dimafour.restaurantvoting.repository;

import com.github.dimafour.restaurantvoting.error.DataConflictException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.github.dimafour.restaurantvoting.model.Meal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MealRepository extends BaseRepository<Meal> {

    @Query("SELECT m FROM Meal m WHERE m.restaurant.id=:restaurantId AND m.date=:date ORDER BY m.name")
    List<Meal> getMenuByIdAndDate(int restaurantId, LocalDate date);

    @Modifying
    @Transactional
    @Query("DELETE FROM Meal m WHERE m.restaurant.id=:restaurantId AND m.date=:date")
    void deleteByIdAndDate(int restaurantId, LocalDate date);

    @Modifying
    @Transactional
    default List<Meal> rewrite(int restaurantId, List<Meal> menu) {
        deleteByIdAndDate(restaurantId, menu.getFirst().getDate());
        return saveAll(menu);
    }

    @Query("SELECT m FROM Meal m WHERE m.id=:id and m.restaurant.id =:restaurantId")
    Optional<Meal> get(int id, int restaurantId);

    default Meal getBelonged(int id, int restaurantId) {
        return get(id, restaurantId).orElseThrow(
                () -> new DataConflictException("Meal id=" + id + " is not exist or doesn't belong to Restaurant id=" + restaurantId));
    }
}
