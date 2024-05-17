package com.github.dimafour.restaurantvoting.to;

import lombok.*;

@Value
@EqualsAndHashCode(callSuper = true)
public class RateLineTo extends BaseTo {
    long votesNumber;

    public RateLineTo(int restaurantId, long votesNumber) {
        super(restaurantId);
        this.votesNumber = votesNumber;
    }
    @Override
    public String toString() {
        return super.toString() + '[' + votesNumber + ']';
    }
}
