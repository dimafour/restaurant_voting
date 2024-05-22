package com.github.dimafour.restaurantvoting.util;

import com.github.dimafour.restaurantvoting.to.MealTo;
import lombok.experimental.UtilityClass;
import com.github.dimafour.restaurantvoting.model.Meal;

import java.util.Collections;
import java.util.List;

@UtilityClass
public class MealUtil {
    public static List<MealTo> getTosList(List<Meal> meals) {
        if (meals == null || meals.isEmpty()) {
            return Collections.emptyList();
        }
        return meals.stream().map(
                        meal -> new MealTo(meal.getId(), meal.getName(), meal.getPrice())
                )
                .toList();
    }

    public static List<Meal> getFromToList(List<MealTo> meals) {
        if (meals == null || meals.isEmpty()) {
            return Collections.emptyList();
        }
        return meals.stream().map(
                        mealTo -> new Meal(mealTo.getName(), mealTo.getPrice())
                )
                .toList();
    }
}
