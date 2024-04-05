package ru.restaurant_voting.util;

import lombok.experimental.UtilityClass;
import ru.restaurant_voting.model.Restaurant;
import ru.restaurant_voting.to.RestaurantTo;

import java.util.List;

@UtilityClass
public class RestaurantUtil {
    public static List<RestaurantTo> getTosList(List<Restaurant> restaurantList) {
        return restaurantList.stream().map(
                        restaurant -> new RestaurantTo(restaurant.id(), restaurant.getName(),
                                MealUtil.getTosList(restaurant.getMenu())))
                .toList();
    }
}
