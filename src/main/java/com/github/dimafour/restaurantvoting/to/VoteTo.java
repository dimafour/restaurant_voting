package com.github.dimafour.restaurantvoting.to;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo {
    public VoteTo(Integer restaurantId) {
        super(restaurantId);
    }
}
