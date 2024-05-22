package com.github.dimafour.restaurantvoting.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.github.dimafour.restaurantvoting.model.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Query("SELECT r FROM Restaurant r JOIN FETCH r.menu m WHERE m.date=:date ORDER BY r.name, m.name")
    List<Restaurant> getListByDate(LocalDate date);

    @Query("SELECT r FROM Restaurant r ORDER BY r.name")
    List<Restaurant> getAll();

    Optional <Restaurant> getRestaurantByName(String name);
}
