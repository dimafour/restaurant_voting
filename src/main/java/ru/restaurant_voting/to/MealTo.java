package ru.restaurant_voting.to;

import lombok.*;

@Value
@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor(force = true)
public class MealTo {
    String name;
    int price;
}
