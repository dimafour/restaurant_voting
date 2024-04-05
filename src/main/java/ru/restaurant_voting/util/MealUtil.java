package ru.restaurant_voting.util;

import lombok.experimental.UtilityClass;
import ru.restaurant_voting.model.Meal;
import ru.restaurant_voting.to.MealTo;

import java.util.Collections;
import java.util.List;

@UtilityClass
public class MealUtil {
    public static List<MealTo> getTosList(List<Meal> meals) {
        if (meals == null) {
            return Collections.emptyList();
        }
        return meals.stream().map(
                        meal -> new MealTo(meal.getId(), meal.getName(), meal.getPrice())
                )
                .toList();
    }

    public static List<Meal> getFromTo(List<MealTo> meals) {
        if (meals == null) {
            return Collections.emptyList();
        }
        return meals.stream().map(
                        mealTo -> new Meal(mealTo.getName(), mealTo.getPrice())
                )
                .toList();
    }
}
