package ru.restaurant_voting.to;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(force = true)
public class RateLineTo {
    int restaurantId;
    long votesNumber;
}
