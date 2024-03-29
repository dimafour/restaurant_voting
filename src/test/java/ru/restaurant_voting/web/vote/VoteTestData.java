package ru.restaurant_voting.web.vote;

import ru.restaurant_voting.MatcherFactory;
import ru.restaurant_voting.model.Vote;

import java.time.LocalDate;

import static ru.restaurant_voting.web.restaurant.RestaurantTestData.*;
import static ru.restaurant_voting.web.user.UserTestData.*;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingEqualsComparator(Vote.class);
    public static final Vote vote1 = new Vote(user1, restaurant3, LocalDate.now());
    public static final Vote vote2 = new Vote(user2, restaurant1, LocalDate.now());
    public static final Vote vote3 = new Vote(user3, restaurant3, LocalDate.now());
    public static final Vote vote4 = new Vote(admin, restaurant2, LocalDate.now());

}
