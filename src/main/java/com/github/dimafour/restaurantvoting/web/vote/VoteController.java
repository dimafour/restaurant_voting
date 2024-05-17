package com.github.dimafour.restaurantvoting.web.vote;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.github.dimafour.restaurantvoting.error.DataConflictException;
import com.github.dimafour.restaurantvoting.error.NotFoundException;
import com.github.dimafour.restaurantvoting.model.Vote;
import com.github.dimafour.restaurantvoting.repository.UserRepository;
import com.github.dimafour.restaurantvoting.repository.VoteRepository;
import com.github.dimafour.restaurantvoting.service.RestaurantService;
import com.github.dimafour.restaurantvoting.to.RateLineTo;
import com.github.dimafour.restaurantvoting.to.VoteTo;
import com.github.dimafour.restaurantvoting.web.AuthUser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.github.dimafour.restaurantvoting.util.VoteUtil.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = VoteController.URL_USER_VOTES, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Vote Controller", description = "Allow to choose restaurant from list to have lunch today")
public class VoteController {
    static final String URL_USER_VOTES = "/api/votes";
    static final String TOO_LATE = "It's too late to change your vote";
    static final String ALREADY_VOTED = "You have already voted today";
    static final String NOTHING_TO_UPDATE = "Your vote is not found - Nothing to update";
    static LocalTime deadline = LocalTime.of(11, 0);

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final RestaurantService restaurantService;

    @GetMapping
    @Operation(summary = "Get your today's vote")
    public ResponseEntity<VoteTo> get(@AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.id();
        log.info("get today vote from user {}", userId);
        return voteRepository.getTodayVote(userId).map(restaurantId ->
                ResponseEntity.ok(new VoteTo(restaurantId))).orElseGet(() ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Vote for the restaurant by ID")
    public VoteTo create(@AuthenticationPrincipal AuthUser authUser, @RequestParam int restaurantId) {
        int userId = authUser.id();
        if (voteRepository.getTodayVote(userId).isPresent()) {
            throw new DataConflictException(ALREADY_VOTED);
        }
        log.info("create vote for restaurant id={} from user id={}", restaurantId, userId);
        Vote vote = new Vote();
        vote.setDate(LocalDate.now());
        vote.setUser(userRepository.getReferenceById(userId));
        vote.setRestaurant(restaurantService.getRestaurant(restaurantId));
        return createTo(voteRepository.save(vote));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update your today's vote",
            description = "! If you update vote after 11.00 am your new vote will NOT be accepted !")
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestParam int restaurantId) {
        int userId = authUser.id();
        if (LocalTime.now().isAfter(deadline)) {
            log.info("user id={} vote can not be updated due to the deadline", userId);
            throw new DataConflictException(TOO_LATE);
        }
        if (voteRepository.getTodayVote(userId).isEmpty()) {
            throw new NotFoundException(NOTHING_TO_UPDATE);
        }
        log.info("update vote for restaurant id={} from user {}", restaurantId, userId);
        voteRepository.update(authUser.id(), restaurantId);
    }

    @GetMapping("/rating")
    @Operation(summary = "Get today restaurants rating list with votes number")
    public List<RateLineTo> getRating() {
        log.info("get restaurant rating for now");
        return voteRepository.getRating();
    }
}
