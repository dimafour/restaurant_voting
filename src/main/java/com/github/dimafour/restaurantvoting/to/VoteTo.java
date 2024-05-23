package com.github.dimafour.restaurantvoting.to;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo {

    @NotNull
    LocalDate date;

    public VoteTo(Integer restaurantId, LocalDate date) {
        super(restaurantId);
        this.date = date;
    }

    @Override
    public String toString() {
        return super.toString() + '[' + date + ']';
    }
}
