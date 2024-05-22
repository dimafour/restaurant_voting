package com.github.dimafour.restaurantvoting.util;

import com.github.dimafour.restaurantvoting.to.VoteTo;
import lombok.experimental.UtilityClass;
import com.github.dimafour.restaurantvoting.model.Vote;

import java.util.Collections;
import java.util.List;

@UtilityClass
public class VoteUtil {
    public static VoteTo getTo(Vote vote) {
        return new VoteTo(vote.getRestaurant().id(), vote.getDate());
    }

    public static List<VoteTo> getTo(List<Vote> votes) {
        if (votes == null || votes.isEmpty()) {
            return Collections.emptyList();
        }
        return votes.stream().map(VoteUtil::getTo).toList();
    }
}
