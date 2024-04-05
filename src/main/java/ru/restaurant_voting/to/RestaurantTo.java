package ru.restaurant_voting.to;

import lombok.*;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo {
    List<MealTo> menu;

    public RestaurantTo(Integer id, String name, List<MealTo> menu) {
        super(id, name);
        this.menu = menu;
    }
}
