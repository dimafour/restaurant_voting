package ru.restaurant_voting.web.meal;

import ru.restaurant_voting.model.Meal;

import java.time.LocalDate;

import static ru.restaurant_voting.web.restaurant.RestaurantTestData.*;

public class MealTestData {
    public static Meal MEAL1_R1 = new Meal(1, "Big Hit", 50000, LocalDate.now(), restaurant1);
    public static Meal MEAL2_R1 = new Meal(2, "Cheeseburger", 30000, LocalDate.now(), restaurant1);
    public static Meal MEAL3_R1 = new Meal(3, "Big Tasty", 70000, LocalDate.now(), restaurant1);
    public static Meal MEAL1_R2 = new Meal(4, "Hot Wings", 40000, LocalDate.now(), restaurant2);
    public static Meal MEAL2_R2 = new Meal(5, "Twister", 20000, LocalDate.now(), restaurant2);
    public static Meal MEAL1_R3 = new Meal(6, "Lager Beer", 60000, LocalDate.now(), restaurant3);
    public static Meal MEAL2_R3 = new Meal(7, "Indian Pale Ale", 60000, LocalDate.now(), restaurant3);
}
