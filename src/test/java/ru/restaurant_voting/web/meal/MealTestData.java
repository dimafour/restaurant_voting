package ru.restaurant_voting.web.meal;

import ru.restaurant_voting.model.Meal;
import ru.restaurant_voting.to.MealTo;
import ru.restaurant_voting.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

import static ru.restaurant_voting.web.restaurant.RestaurantTestData.*;

public class MealTestData {
    public static final MatcherFactory.Matcher<Meal> MEAL_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Meal.class,"restaurant");

    public static final MatcherFactory.Matcher<MealTo> MEAL_TO_MATCHER = MatcherFactory.usingEqualsComparator(MealTo.class);
    public static Meal MEAL1_R1 = new Meal(1, "Big Hit", 50000, LocalDate.now(), restaurant1);
    public static Meal MEAL2_R1 = new Meal(2, "Cheeseburger", 30000, LocalDate.now(), restaurant1);
    public static Meal MEAL3_R1 = new Meal(3, "Big Tasty", 70000, LocalDate.now(), restaurant1);
    public static Meal MEAL1_R2 = new Meal(4, "Hot Wings", 40000, LocalDate.now(), restaurant2);
    public static Meal MEAL2_R2 = new Meal(5, "Twister", 20000, LocalDate.now(), restaurant2);
    public static Meal MEAL1_R3 = new Meal(6, "Lager Beer", 60000, LocalDate.now(), restaurant3);
    public static Meal MEAL2_R3 = new Meal(7, "Indian Pale Ale", 60000, LocalDate.now(), restaurant3);
    public static Meal MEAL1_R4_OLD = new Meal(8, "Taco", 30000, LocalDate.of(2024,1,1), restaurant4);
    public static Meal MEAL2_R4_OLD = new Meal(9, "Burrito", 50000, LocalDate.now(), restaurant4);
    public static Meal MEAL1_R2_NEW = new Meal(10, "French Fries", 40000, LocalDate.now(), restaurant2);
    public static Meal MEAL2_R2_NEW = new Meal(11, "Strips", 30000, LocalDate.now(), restaurant2);
    public static List<Meal> NEW_MENU_R2 = List.of(MEAL1_R2_NEW, MEAL2_R2_NEW);
}
