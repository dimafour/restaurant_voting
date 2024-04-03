package ru.restaurant_voting.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.restaurant_voting.repository.MealRepository;
import ru.restaurant_voting.util.JsonUtil;
import ru.restaurant_voting.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurant_voting.util.MealUtil.getTosList;
import static ru.restaurant_voting.web.meal.MealTestData.*;
import static ru.restaurant_voting.web.restaurant.RestaurantTestData.restaurant2;
import static ru.restaurant_voting.web.restaurant.RestaurantTestData.restaurant3;
import static ru.restaurant_voting.web.user.UserTestData.ADMIN_MAIL;

class AdminMealControllerTest extends AbstractControllerTest {

    @Autowired
    MealRepository mealRepository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getMenu() throws Exception {
        int restaurantId = restaurant3.id();
        perform(MockMvcRequestBuilders.get("/api/admin/restaurants/" + restaurantId + "/menu"))
                .andExpect(MEAL_TO_MATCHER.contentJson(getTosList(restaurant3.getMenu())));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void changeMenu() throws Exception {
        int restaurantId = restaurant2.id();
        perform(MockMvcRequestBuilders.post("/api/admin/restaurants/" + restaurantId + "/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getTosList(NEW_MENU_R2))))
                .andExpect(status().isCreated());

        MEAL_MATCHER.assertMatch(mealRepository.getMenu(restaurantId), NEW_MENU_R2);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        int restaurantId = restaurant3.id();
        perform(MockMvcRequestBuilders.delete("/api/admin/restaurants/" + restaurantId + "/menu"))
                .andExpect(status().isNoContent());
        assertTrue(mealRepository.getMenu(restaurantId).isEmpty());
    }
}