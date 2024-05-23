package com.github.dimafour.restaurantvoting.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.github.dimafour.restaurantvoting.model.Meal;
import com.github.dimafour.restaurantvoting.repository.MealRepository;
import com.github.dimafour.restaurantvoting.util.JsonUtil;
import com.github.dimafour.restaurantvoting.web.AbstractControllerTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.dimafour.restaurantvoting.util.MealUtil.getTosList;
import static com.github.dimafour.restaurantvoting.web.meal.MealTestData.*;
import static com.github.dimafour.restaurantvoting.web.restaurant.RestaurantTestData.*;
import static com.github.dimafour.restaurantvoting.web.user.UserTestData.ADMIN_MAIL;

public class AdminMealControllerTest extends AbstractControllerTest {

    @Autowired
    MealRepository mealRepository;
    private static final String URL = "/api/admin/restaurants/";

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getMenu() throws Exception {
        int restaurantId = restaurant3.id();
        perform(MockMvcRequestBuilders.get(URL + restaurantId + "/meals"))
                .andExpect(MEAL_TO_MATCHER.contentJson(getTosList(restaurant3.getMenu())));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void setMenu() throws Exception {
        int restaurantId = restaurant2.id();
        perform(MockMvcRequestBuilders.put(URL + restaurantId + "/meals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getTosList(newMenuR2))))
                .andExpect(status().isCreated());

        MEAL_MATCHER.assertMatch(mealRepository.getMenuByIdAndDate(restaurantId, LocalDate.now()), newMenuR2);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void setMenuNotValid() throws Exception {
        int restaurantId = restaurant2.id();
        perform(MockMvcRequestBuilders.put(URL + restaurantId + "/meals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getTosList(notValidMenuR2))))
                .andExpect(status().isUnprocessableEntity());

        MEAL_MATCHER.assertMatch(mealRepository.getMenuByIdAndDate(restaurantId, LocalDate.now()), restaurant2.getMenu());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteMenu() throws Exception {
        int restaurantId = restaurant3.id();
        perform(MockMvcRequestBuilders.delete(URL + restaurantId + "/meals"))
                .andExpect(status().isNoContent());
        assertTrue(mealRepository.getMenuByIdAndDate(restaurantId, LocalDate.now()).isEmpty());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getMeal() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + restaurant1.getId() + "/meals/" + meal1R1.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meal1R1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteMeal() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + restaurant1.getId() + "/meals/" + meal1R1.getId()))
                .andExpect(status().isNoContent());
        assertFalse(mealRepository.get(meal1R1.id(), restaurant1.id()).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createMeal() throws Exception {
        Meal newMeal = new Meal(null, "newMeal", 100_00, LocalDate.now(), restaurant1);
        ResultActions action = perform(MockMvcRequestBuilders.patch(URL + restaurant1.getId() + "/meals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)));

        Meal created = MEAL_MATCHER.readFromJson(action);
        int newId = created.getId();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(mealRepository.getExisted(newId), newMeal);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Meal updated = new Meal(meal1R1.getId(), "updatedMeal", 1000_00, LocalDate.now(), restaurant1);
        perform(MockMvcRequestBuilders.put(URL + restaurant1.getId() + "/meals/" + meal1R1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        MEAL_MATCHER.assertMatch(mealRepository.getExisted(meal1R1.getId()), updated);
    }
}