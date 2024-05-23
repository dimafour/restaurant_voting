package com.github.dimafour.restaurantvoting.service;

import com.github.dimafour.restaurantvoting.error.DataConflictException;
import com.github.dimafour.restaurantvoting.error.NotFoundException;
import com.github.dimafour.restaurantvoting.model.Vote;
import com.github.dimafour.restaurantvoting.repository.UserRepository;
import com.github.dimafour.restaurantvoting.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.github.dimafour.restaurantvoting.util.ValidationUtil.*;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class VoteService {
    static final String TOO_LATE = "It's too late to change your vote";
    static final String ALREADY_VOTED = "You have already voted today";
    static final String NOTHING_TO_UPDATE = "Your vote is not found - Nothing to update";
    static LocalTime deadline = LocalTime.of(11, 0);

    private final VoteRepository voteRepository;
    private final RestaurantService restaurantService;
    private final UserRepository userRepository;

    public Vote save(int userId, int restaurantId) {
        if (voteRepository.getVoteByDate(userId, LocalDate.now()).isPresent()) {
            log.info("can not save user's id={} vote because it already exists", userId);
            throw new DataConflictException(ALREADY_VOTED);
        }
        Vote vote = new Vote();
        vote.setDate(LocalDate.now());
        vote.setUser(userRepository.getReferenceById(userId));
        vote.setRestaurant(checkContains(restaurantService.getTodayList(), restaurantId));
        return voteRepository.save(vote);
    }

    public void update(int userId, int restaurantId) {
        if (LocalTime.now().isAfter(deadline)) {
            log.info("user id={} vote can not be updated due to the deadline", userId);
            throw new DataConflictException(TOO_LATE);
        }
        if (voteRepository.getVoteByDate(userId, LocalDate.now()).isEmpty()) {
            log.info("can not update user's id={} vote because it does not exist", userId);
            throw new NotFoundException(NOTHING_TO_UPDATE);
        }
        voteRepository.updateByDate(userId, restaurantId, LocalDate.now());
    }
}
