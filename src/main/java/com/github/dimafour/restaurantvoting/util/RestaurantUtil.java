package com.github.dimafour.restaurantvoting.util;

import com.github.dimafour.restaurantvoting.to.RestaurantWithMenuTo;
import lombok.experimental.UtilityClass;
import com.github.dimafour.restaurantvoting.model.Restaurant;

import java.util.Collections;
import java.util.List;

@UtilityClass
public class RestaurantUtil {
    public static List<RestaurantWithMenuTo> getTosList(List<Restaurant> restaurantList) {
        if (restaurantList == null || restaurantList.isEmpty()) {
            return Collections.emptyList();
        }
        return restaurantList.stream().map(
                restaurant -> new RestaurantWithMenuTo(restaurant.id(), restaurant.getName(),
                        MealUtil.getTosList(restaurant.getMenu()))
        ).toList();
    }
}
