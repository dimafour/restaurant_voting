package com.github.dimafour.restaurantvoting.to;

import lombok.*;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantWithMenuTo extends NamedTo {

    List<MealTo> menu;

    public RestaurantWithMenuTo(Integer id, String name, List<MealTo> menu) {
        super(id, name);
        this.menu = menu;
    }

    @Override
    public String toString() {
        return super.toString() + '[' + menu + ']';
    }
}
