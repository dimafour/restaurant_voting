package ru.restaurant_voting.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurant_voting.model.Meal;
import ru.restaurant_voting.repository.MealRepository;
import ru.restaurant_voting.util.JsonUtil;
import ru.restaurant_voting.web.AbstractControllerTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurant_voting.util.MealUtil.getTosList;
import static ru.restaurant_voting.web.meal.MealTestData.*;
import static ru.restaurant_voting.web.restaurant.RestaurantTestData.*;
import static ru.restaurant_voting.web.user.UserTestData.ADMIN_MAIL;

class AdminMealControllerTest extends AbstractControllerTest {

    @Autowired
    MealRepository mealRepository;
    private static final String URL = "/api/admin/restaurants/";

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getMenu() throws Exception {
        int restaurantId = restaurant3.id();
        perform(MockMvcRequestBuilders.get(URL + restaurantId + "/menu"))
                .andExpect(MEAL_TO_MATCHER.contentJson(getTosList(restaurant3.getMenu())));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void changeMenu() throws Exception {
        int restaurantId = restaurant2.id();
        perform(MockMvcRequestBuilders.post(URL + restaurantId + "/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getTosList(newMenuR2))))
                .andExpect(status().isCreated());

        MEAL_MATCHER.assertMatch(mealRepository.getMenu(restaurantId), newMenuR2);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    @Transactional(propagation = Propagation.NEVER)
    void changeMenuNotValid() throws Exception {
        int restaurantId = restaurant2.id();
        perform(MockMvcRequestBuilders.post(URL + restaurantId + "/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getTosList(notValidMenuR2))))
                .andExpect(status().is5xxServerError());

        MEAL_MATCHER.assertMatch(mealRepository.getMenu(restaurantId), restaurant2.getMenu());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteMenu() throws Exception {
        int restaurantId = restaurant3.id();
        perform(MockMvcRequestBuilders.delete(URL + restaurantId + "/menu"))
                .andExpect(status().isNoContent());
        assertTrue(mealRepository.getMenu(restaurantId).isEmpty());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getMeal() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + restaurant1.getId() + "/meal/" + meal1R1.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meal1R1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteMeal() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + restaurant1.getId() + "/meal/" + meal1R1.getId()))
                .andExpect(status().isNoContent());
        assertFalse(mealRepository.get(meal1R1.id(), restaurant1.id()).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createMeal() throws Exception {
        Meal newMeal = new Meal(null, "newMeal", 100_00, LocalDate.now(), restaurant1);
        ResultActions action = perform(MockMvcRequestBuilders.post(URL + restaurant1.getId() + "/meal")
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
        perform(MockMvcRequestBuilders.put(URL + restaurant1.getId() + "/meal/" + meal1R1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        MEAL_MATCHER.assertMatch(mealRepository.getExisted(meal1R1.getId()), updated);
    }

}