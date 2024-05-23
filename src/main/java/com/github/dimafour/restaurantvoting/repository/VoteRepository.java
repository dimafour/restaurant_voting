package com.github.dimafour.restaurantvoting.repository;

import com.github.dimafour.restaurantvoting.to.RestaurantRatingTo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.github.dimafour.restaurantvoting.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v.restaurant.id FROM Vote v WHERE v.user.id=:userId AND v.date=:date")
    Optional<Integer> getVoteByDate(int userId, LocalDate date);

    @Query("SELECT v FROM Vote v JOIN FETCH v.restaurant WHERE v.user.id=:userId ORDER BY v.date DESC")
    List<Vote> getByUserId(int userId);

    @Transactional
    @Modifying
    @Query("UPDATE Vote v SET v.restaurant.id=:restaurantId WHERE v.user.id=:userId AND v.date=:date")
    void updateByDate(int userId, int restaurantId, LocalDate date);

    @Query("SELECT new com.github.dimafour.restaurantvoting.to.RestaurantRatingTo(v.restaurant.id, v.restaurant.name," +
           "COUNT (v.restaurant.id)) FROM Vote v GROUP BY v.restaurant.id " +
           "HAVING v.date=current_date ORDER BY COUNT (v.restaurant.id) DESC")
    List<RestaurantRatingTo> getRating();
}
