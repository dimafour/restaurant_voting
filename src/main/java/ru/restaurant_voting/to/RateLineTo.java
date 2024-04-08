package ru.restaurant_voting.to;

import lombok.*;
import ru.restaurant_voting.model.BaseEntity;

@Value
@EqualsAndHashCode(callSuper = true)
public class RateLineTo extends BaseEntity {
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
