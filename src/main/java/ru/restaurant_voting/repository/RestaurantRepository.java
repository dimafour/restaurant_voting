package ru.restaurant_voting.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurant_voting.model.Restaurant;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
    @Query("SELECT r FROM Restaurant r JOIN FETCH r.menu m WHERE m.meal_date=current_date")
    List<Restaurant> getTodayList();

    @Query("SELECT r FROM Restaurant r WHERE r.id=:id ORDER BY r.id")
    Optional<Restaurant> get(int id);

    @Query("SELECT r FROM Restaurant r ORDER BY r.id")
    List<Restaurant> getAll();
}
