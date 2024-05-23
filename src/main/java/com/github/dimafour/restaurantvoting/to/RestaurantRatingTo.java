package com.github.dimafour.restaurantvoting.to;

import lombok.*;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantRatingTo extends NamedTo {
    long votesNumber;

    public RestaurantRatingTo(int restaurantId, String restaurantName, long votesNumber) {
        super(restaurantId, restaurantName);
        this.votesNumber = votesNumber;
    }
    @Override
    public String toString() {
        return super.toString() + '[' + votesNumber + ']';
    }
}
