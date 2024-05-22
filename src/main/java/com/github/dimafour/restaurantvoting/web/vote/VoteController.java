package com.github.dimafour.restaurantvoting.web.vote;

import com.github.dimafour.restaurantvoting.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.github.dimafour.restaurantvoting.repository.VoteRepository;
import com.github.dimafour.restaurantvoting.to.RateLineTo;
import com.github.dimafour.restaurantvoting.to.VoteTo;
import com.github.dimafour.restaurantvoting.web.AuthUser;

import java.time.LocalDate;
import java.util.List;

import static com.github.dimafour.restaurantvoting.util.VoteUtil.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = VoteController.URL_USER_VOTES, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Vote Controller", description = "Allow to choose restaurant from list to have lunch today")
public class VoteController {
    static final String URL_USER_VOTES = "/api/vote";
    private final VoteService voteService;
    private final VoteRepository voteRepository;

    @GetMapping
    @Operation(summary = "Get your one of yours the previous votes (default - today's)")
    public ResponseEntity<VoteTo> get(@AuthenticationPrincipal AuthUser authUser,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        int userId = authUser.id();
        LocalDate notNullDate = date == null ? LocalDate.now() : date;
        log.info("get vote from user {} dated {}", userId, notNullDate);
        return voteRepository.getVoteByDate(userId, notNullDate).map(restaurantId ->
                ResponseEntity.ok(new VoteTo(restaurantId, notNullDate))).orElseGet(() ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/my-votes")
    @Operation(summary = "Get your votes history since today descending date order")
    public List<VoteTo> getVotesHistory(@AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.id();
        log.info("get vote history from user {}", userId);
        return getTo(voteRepository.getByUserId(userId));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(value = "rating", allEntries = true)
    @Operation(summary = "Vote for the restaurant by ID")
    public VoteTo create(@AuthenticationPrincipal AuthUser authUser, @RequestBody int restaurantId) {
        int userId = authUser.id();
        log.info("create vote for restaurant id={} from user id={}", restaurantId, userId);
        return getTo(voteService.save(userId, restaurantId));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "rating", allEntries = true)
    @Operation(summary = "Update your today's vote",
            description = "! If you update vote after 11.00 am your new vote will NOT be accepted !")
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestBody int restaurantId) {
        int userId = authUser.id();
        log.info("update vote for restaurant id={} from user {}", restaurantId, userId);
        voteService.update(userId, restaurantId);
    }

    @GetMapping("/rating")
    @Cacheable("rating")
    @Operation(summary = "Get today restaurants rating list with votes number")
    public List<RateLineTo> getRating() {
        log.info("get restaurant rating for now");
        return voteRepository.getRating();
    }
}
