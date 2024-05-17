package com.github.dimafour.restaurantvoting.util;

import com.github.dimafour.restaurantvoting.to.VoteTo;
import lombok.experimental.UtilityClass;
import com.github.dimafour.restaurantvoting.model.Vote;

@UtilityClass
public class VoteUtil {
    public static VoteTo createTo(Vote vote) {
        return new VoteTo(vote.getRestaurant().id());
    }
}
