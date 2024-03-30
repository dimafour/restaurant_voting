package ru.restaurant_voting.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurant_voting.error.NotFoundException;
import ru.restaurant_voting.model.Vote;

import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v.restaurant.id FROM Vote v WHERE v.user.id=:userId AND v.vote_date=current_date")
    Optional<Integer> getTodayVote(int userId);

    @Transactional
    @Modifying
    @Query("UPDATE Vote v SET v.restaurant.id=:restaurantId WHERE v.user.id=:userId AND v.vote_date=current_date")
    void update(int userId, int restaurantId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.user.id=:userId AND v.vote_date=current_date")
    int delete(int userId);

    @SuppressWarnings("all")
    default void deleteExisted(int id) {
        if (delete(id) == 0) {
            throw new NotFoundException("Entity with id=" + id + " not found");
        }
    }
}
