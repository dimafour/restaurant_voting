package com.github.dimafour.restaurantvoting.util;

import com.github.dimafour.restaurantvoting.to.RestaurantTo;
import lombok.experimental.UtilityClass;
import com.github.dimafour.restaurantvoting.model.Restaurant;

import java.util.List;

@UtilityClass
public class RestaurantUtil {
    public static List<RestaurantTo> getTosList(List<Restaurant> restaurantList) {
        return restaurantList.stream().map(
                restaurant -> new RestaurantTo(restaurant.id(), restaurant.getName(),
                        MealUtil.getTosList(restaurant.getMenu()))
        ).toList();
    }
}
