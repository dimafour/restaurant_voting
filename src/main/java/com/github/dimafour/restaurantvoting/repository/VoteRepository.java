package com.github.dimafour.restaurantvoting.repository;

import com.github.dimafour.restaurantvoting.to.RateLineTo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.github.dimafour.restaurantvoting.model.Vote;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v.restaurant.id FROM Vote v WHERE v.user.id=:userId AND v.date=current_date")
    Optional<Integer> getTodayVote(int userId);

    @Transactional
    @Modifying
    @Query("UPDATE Vote v SET v.restaurant.id=:restaurantId WHERE v.user.id=:userId AND v.date=current_date")
    void update(int userId, int restaurantId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.user.id=:userId AND v.date=current_date")
    int delete(int userId);

    @Query("SELECT new com.github.dimafour.restaurantvoting.to.RateLineTo(v.restaurant.id, " +
           "COUNT (v.restaurant.id)) FROM Vote v GROUP BY v.restaurant.id " +
           "HAVING v.date=current_date ORDER BY COUNT (v.restaurant.id) DESC")
    List<RateLineTo> getRating();

}
