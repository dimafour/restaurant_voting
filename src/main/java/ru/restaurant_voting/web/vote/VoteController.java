package ru.restaurant_voting.web.vote;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.restaurant_voting.error.AppException;
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
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    static final String REST_URL = "/api/vote";
    static final LocalTime deadline = LocalTime.of(11, 0);

    @GetMapping
    public ResponseEntity<VoteTo> get(@AuthenticationPrincipal AuthUser authUser) {
        return voteRepository.getTodayVote(authUser.id()).map(restaurantId ->
                ResponseEntity.ok(new VoteTo(restaurantId))).orElseGet(() ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoteTo> create(@AuthenticationPrincipal AuthUser authUser, @RequestParam int restaurantId) {
        Vote vote = new Vote();
        vote.setVote_date(LocalDate.now());
        vote.setUser(userRepository.getReferenceById(authUser.id()));
        vote.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        return ResponseEntity.ok(VoteUtil.createFromVote(voteRepository.save(vote)));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestParam int restaurantId) throws Exception {
        if (LocalTime.now().isAfter(deadline)) {
            throw new AppException("It's too late to change your vote");
        }
        voteRepository.update(authUser.id(), restaurantId);
    }
}
