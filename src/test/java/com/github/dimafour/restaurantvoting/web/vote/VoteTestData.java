package com.github.dimafour.restaurantvoting.web.vote;

import com.github.dimafour.restaurantvoting.model.Vote;
import com.github.dimafour.restaurantvoting.to.RestaurantRatingTo;
import com.github.dimafour.restaurantvoting.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

import static com.github.dimafour.restaurantvoting.web.restaurant.RestaurantTestData.*;
import static com.github.dimafour.restaurantvoting.web.user.UserTestData.*;

public class VoteTestData {
    public static final MatcherFactory.Matcher<RestaurantRatingTo> RATING_MATCHER = MatcherFactory.usingEqualsComparator(RestaurantRatingTo.class);
    public static final Vote voteUser1R3 = new Vote(1, user1, restaurant3, LocalDate.now());
    public static final Vote voteAdminR2 = new Vote(4, admin, restaurant2, LocalDate.now());
    public static List<RestaurantRatingTo> rating = List.of(new RestaurantRatingTo(restaurant3.id(),"Dolphinwolf" ,2), new RestaurantRatingTo(restaurant2.id(), "KFC", 1));
}
