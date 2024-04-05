package ru.restaurant_voting.web.restaurant;

import ru.restaurant_voting.model.Restaurant;
import ru.restaurant_voting.to.RestaurantTo;
import ru.restaurant_voting.web.MatcherFactory;

import java.util.List;

import static ru.restaurant_voting.web.meal.MealTestData.*;

public class RestaurantTestData {

    public static final MatcherFactory.Matcher<RestaurantTo> RESTAURANT_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(RestaurantTo.class, "meal.restaurant");
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingEqualsComparator(Restaurant.class);

    public static final Restaurant restaurant1 = new Restaurant(1, "Tasty and that-s it");
    public static final Restaurant restaurant2 = new Restaurant(2, "KFC");
    public static final Restaurant restaurant3 = new Restaurant(3, "Dolphinwolf");
    public static final Restaurant restaurant4 = new Restaurant(4, "Taco Bell");
    public static final Restaurant restaurant5 = new Restaurant(null, "Pizza Hut");
    public static final Restaurant restaurant = new Restaurant(null, "!");

    static {
        restaurant1.addMenu(meal1R1, meal2R1, meal3R1);
        restaurant2.addMenu(meal1R2, meal2R2);
        restaurant3.addMenu(meal1R3, meal2R3);
        restaurant4.addMenu(meal1R4Old, meal2R4Old);
    }
    public static List<Restaurant> restaurantTodayList = List.of(restaurant1, restaurant2, restaurant3);
    public static List<Restaurant> restaurantAllList = List.of(restaurant1, restaurant2, restaurant3, restaurant4);

    public static Restaurant getUpdated() {
        return new Restaurant(null, "Rostics");
    }
}
