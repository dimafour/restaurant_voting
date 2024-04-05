package ru.restaurant_voting.web.meal;

import ru.restaurant_voting.model.Meal;
import ru.restaurant_voting.to.MealTo;
import ru.restaurant_voting.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

import static ru.restaurant_voting.web.restaurant.RestaurantTestData.*;

public class MealTestData {
    public static final MatcherFactory.Matcher<Meal> MEAL_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Meal.class, "restaurant");

    public static final MatcherFactory.Matcher<MealTo> MEAL_TO_MATCHER = MatcherFactory.usingEqualsComparator(MealTo.class);
    public static Meal meal1R1 = new Meal(1, "Big Hit", 50000, LocalDate.now(), restaurant1);
    public static Meal meal2R1 = new Meal(2, "Cheeseburger", 30000, LocalDate.now(), restaurant1);
    public static Meal meal3R1 = new Meal(3, "Big Tasty", 70000, LocalDate.now(), restaurant1);
    public static Meal meal1R2 = new Meal(4, "Hot Wings", 40000, LocalDate.now(), restaurant2);
    public static Meal meal2R2 = new Meal(5, "Twister", 20000, LocalDate.now(), restaurant2);
    public static Meal meal1R3 = new Meal(6, "Lager Beer", 60000, LocalDate.now(), restaurant3);
    public static Meal meal2R3 = new Meal(7, "Indian Pale Ale", 60000, LocalDate.now(), restaurant3);
    public static Meal meal1R4Old = new Meal(8, "Taco", 30000, LocalDate.of(2024, 1, 1), restaurant4);
    public static Meal meal2R4Old = new Meal(9, "Burrito", 50000, LocalDate.now(), restaurant4);
    public static Meal meal1R2New = new Meal(10, "French Fries", 40000, LocalDate.now(), restaurant2);
    public static Meal meal2R2New = new Meal(11, "Strips", 30000, LocalDate.now(), restaurant2);
    public static Meal meal1R2NotValid = new Meal(10, "a", 40000000, LocalDate.now(), restaurant2);
    public static Meal meal2R2NotValid = new Meal(11, "b", -1000, LocalDate.now(), restaurant2);
    public static List<Meal> newMenuR2 = List.of(meal1R2New, meal2R2New);
    public static List<Meal> notValidMenuR2 = List.of(meal1R2NotValid, meal2R2NotValid);
}
