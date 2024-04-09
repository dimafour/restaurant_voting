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
    public static final Vote voteUser1R3 = new Vote(1, user1, restaurant3, LocalDate.now());
    public static final Vote voteAdminR2 = new Vote(4, admin, restaurant2, LocalDate.now());
    public static List<RateLineTo> rating = List.of(new RateLineTo(restaurant3.id(), 2), new RateLineTo(restaurant2.id(), 1));
}
