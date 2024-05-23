package com.github.dimafour.restaurantvoting.web.restaurant;

import com.github.dimafour.restaurantvoting.model.Restaurant;
import com.github.dimafour.restaurantvoting.to.RestaurantWithMenuTo;
import com.github.dimafour.restaurantvoting.web.MatcherFactory;

import java.util.List;

import static com.github.dimafour.restaurantvoting.web.meal.MealTestData.*;

public class RestaurantTestData {

    public static final MatcherFactory.Matcher<RestaurantWithMenuTo> RESTAURANT_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(RestaurantWithMenuTo.class, "meal.restaurant");
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "menu");

    public static final Restaurant restaurant1 = new Restaurant(1, "Tasty and that-s it");
    public static final Restaurant restaurant2 = new Restaurant(2, "KFC");
    public static final Restaurant restaurant3 = new Restaurant(3, "Dolphinwolf");
    public static final Restaurant restaurant4 = new Restaurant(4, "Taco Bell");
    public static final Restaurant restaurant5 = new Restaurant(null, "Pizza Hut");
    public static final Restaurant restaurant = new Restaurant(null, "!");

    static {
        restaurant1.setMenu(List.of(meal1R1, meal3R1, meal2R1));
        restaurant2.setMenu(List.of(meal1R2, meal2R2));
        restaurant3.setMenu(List.of(meal2R3, meal1R3));
        restaurant4.setMenu(List.of(meal1R4Old, meal2R4Old));
    }

    public static List<Restaurant> restaurantTodayList = List.of(restaurant3, restaurant2, restaurant1);
    public static List<Restaurant> restaurantAllList = List.of(restaurant3, restaurant2, restaurant4, restaurant1);

    public static Restaurant getUpdated() {
        return new Restaurant(null, "Rostics");
    }
}
