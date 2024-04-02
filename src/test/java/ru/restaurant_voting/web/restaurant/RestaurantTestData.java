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
    public static final Restaurant restaurant5 = new Restaurant(5, "Pizza Hut");

    static {
        restaurant1.addMenu(MEAL1_R1, MEAL2_R1, MEAL3_R1);
        restaurant2.addMenu(MEAL1_R2, MEAL2_R2);
        restaurant3.addMenu(MEAL1_R3, MEAL2_R3);
        restaurant4.addMenu(MEAL1_R4_OLD, MEAL2_R4_OLD);
    }
    public static List<Restaurant> restaurantTodayList = List.of(restaurant1, restaurant2, restaurant3);
    public static List<Restaurant> restaurantAllList = List.of(restaurant1, restaurant2, restaurant3, restaurant4);

    public static Restaurant getNew() {
        return new Restaurant(null, "Pizza Hut");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(null, "Rostics");
    }
}
