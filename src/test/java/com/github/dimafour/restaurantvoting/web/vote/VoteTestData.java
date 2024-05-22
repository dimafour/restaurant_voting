package com.github.dimafour.restaurantvoting.web.vote;

import com.github.dimafour.restaurantvoting.model.Vote;
import com.github.dimafour.restaurantvoting.to.RateLineTo;
import com.github.dimafour.restaurantvoting.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

import static com.github.dimafour.restaurantvoting.web.restaurant.RestaurantTestData.*;
import static com.github.dimafour.restaurantvoting.web.user.UserTestData.*;

public class VoteTestData {
    public static final MatcherFactory.Matcher<RateLineTo> RATING_MATCHER = MatcherFactory.usingEqualsComparator(RateLineTo.class);
    public static final Vote voteUser1R3 = new Vote(1, user1, restaurant3, LocalDate.now());
    public static final Vote voteAdminR2 = new Vote(4, admin, restaurant2, LocalDate.now());
    public static List<RateLineTo> rating = List.of(new RateLineTo(restaurant3.id(), 2), new RateLineTo(restaurant2.id(), 1));
}
