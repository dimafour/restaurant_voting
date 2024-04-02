package ru.restaurant_voting.web.vote;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.restaurant_voting.model.Vote;
import ru.restaurant_voting.repository.RestaurantRepository;
import ru.restaurant_voting.repository.UserRepository;
import ru.restaurant_voting.repository.VoteRepository;
import ru.restaurant_voting.to.VoteTo;
import ru.restaurant_voting.util.VoteUtil;
import ru.restaurant_voting.web.AuthUser;

import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = VoteController.URL_USER_VOTES, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    static final String URL_USER_VOTES = "/api/user/votes";
    static final String TOO_LATE = "It's too late to change your vote";
    static LocalTime deadline = LocalTime.of(11, 0);

    @GetMapping
    public ResponseEntity<VoteTo> get(@AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.id();
        log.info("get today vote from user {}", userId);
        return voteRepository.getTodayVote(userId).map(restaurantId ->
                ResponseEntity.ok(new VoteTo(restaurantId))).orElseGet(() ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public VoteTo create(@AuthenticationPrincipal AuthUser authUser, @RequestParam int restaurantId) {
        int userId = authUser.id();
        log.info("create vote for restaurant id={} from user id={}", restaurantId, userId);
        Vote vote = new Vote();
        vote.setVote_date(LocalDate.now());
        vote.setUser(userRepository.getReferenceById(userId));
        vote.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        return VoteUtil.createFromVote(voteRepository.save(vote));
    }

    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> update(@AuthenticationPrincipal AuthUser authUser, @RequestParam int restaurantId) {
        int userId = authUser.id();
        if (LocalTime.now().isAfter(deadline)) {
            log.info("user id={} vote can not be updated due to the deadline", userId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(TOO_LATE);
        }
        log.info("update vote for restaurant id={} from user {}", restaurantId, userId);
        voteRepository.update(authUser.id(), restaurantId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.id();
        if (LocalTime.now().isAfter(deadline)) {
            log.info("user id={} vote can not be deleted due to the deadline", userId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(TOO_LATE);
        }
        log.info("delete vote from user id={}", userId);
        voteRepository.deleteExisted(authUser.id());
        return ResponseEntity.noContent().build();
    }
}
