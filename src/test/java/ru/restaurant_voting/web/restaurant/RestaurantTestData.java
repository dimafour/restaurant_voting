package ru.restaurant_voting.web.restaurant;

import ru.restaurant_voting.model.Restaurant;

import java.util.List;

import static ru.restaurant_voting.web.meal.MealTestData.*;

public class RestaurantTestData {

    public static final Restaurant restaurant1 = new Restaurant(1, "Tasty and that-s it");
    public static final Restaurant restaurant2 = new Restaurant(2, "KFC");
    public static final Restaurant restaurant3 = new Restaurant(3, "Dolphinwolf");
    public static final Restaurant restaurant4 = new Restaurant(4, "Pizza Hut");

    static {
        restaurant1.addMenu(MEAL1_R1, MEAL2_R1, MEAL3_R1);
        restaurant2.addMenu(MEAL1_R2, MEAL2_R2);
        restaurant3.addMenu(MEAL1_R3, MEAL2_R3);
    }

    public static List<Restaurant> restaurantList = List.of(restaurant1, restaurant2, restaurant3, restaurant4);
}
