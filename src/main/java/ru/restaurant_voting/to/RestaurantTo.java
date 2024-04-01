package ru.restaurant_voting.to;

import lombok.*;

import java.util.List;

@Value
@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor(force = true)
public class RestaurantTo {
    int restaurantId;
    String name;
    List<MealTo> menu;
}
