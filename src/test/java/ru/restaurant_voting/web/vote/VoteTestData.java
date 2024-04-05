package ru.restaurant_voting.web.vote;

import ru.restaurant_voting.model.Vote;
import ru.restaurant_voting.to.RateLineTo;
import ru.restaurant_voting.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

import static ru.restaurant_voting.web.restaurant.RestaurantTestData.*;
import static ru.restaurant_voting.web.user.UserTestData.*;

public class VoteTestData {
    public static final MatcherFactory.Matcher<RateLineTo> RATING_MATCHER = MatcherFactory.usingEqualsComparator(RateLineTo.class);
    public static final Vote vote1 = new Vote(user1, restaurant3, LocalDate.now());
    public static final Vote vote2 = new Vote(user2, restaurant1, LocalDate.now()); // to create-test, not included in DB population
    public static final Vote vote3 = new Vote(user3, restaurant3, LocalDate.now());
    public static final Vote vote4 = new Vote(admin, restaurant2, LocalDate.now());
    public static List<RateLineTo> rating = List.of(new RateLineTo(restaurant3.id(), 2), new RateLineTo(restaurant2.id(), 1));
}
