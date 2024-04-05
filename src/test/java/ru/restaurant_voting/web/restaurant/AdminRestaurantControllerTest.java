package ru.restaurant_voting.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.restaurant_voting.model.Restaurant;
import ru.restaurant_voting.repository.RestaurantRepository;
import ru.restaurant_voting.util.JsonUtil;
import ru.restaurant_voting.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurant_voting.web.restaurant.AdminRestaurantController.URL_ADMIN_RESTAURANTS;
import static ru.restaurant_voting.web.restaurant.RestaurantTestData.*;
import static ru.restaurant_voting.web.user.UserTestData.ADMIN_MAIL;
import static ru.restaurant_voting.web.user.UserTestData.USER1_MAIL;

class AdminRestaurantControllerTest extends AbstractControllerTest {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_ADMIN_RESTAURANTS))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurantAllList));
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void getAllForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_ADMIN_RESTAURANTS))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_ADMIN_RESTAURANTS + "/" + restaurant1.id()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getByName() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_ADMIN_RESTAURANTS + "/by-name")
                .param("name", "Tasty and that-s it"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void create() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.post(URL_ADMIN_RESTAURANTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(restaurant5)));
        Restaurant created = RESTAURANT_MATCHER.readFromJson(action);
        int id = created.id();
        restaurant5.setId(id);
        RESTAURANT_MATCHER.assertMatch(created, restaurant5);
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.get(id).orElseThrow(), restaurant5);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createNotValid() throws Exception {
        perform(MockMvcRequestBuilders.post(URL_ADMIN_RESTAURANTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(restaurant)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        int restaurantId = restaurant2.id();
        perform(MockMvcRequestBuilders.put(URL_ADMIN_RESTAURANTS + "/" + restaurantId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getUpdated())))
                .andExpect(status().isNoContent());
        Restaurant updated = getUpdated();
        assertEquals(restaurantRepository.get(restaurantId).orElseThrow().getName(), updated.getName());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        int restaurantId = restaurant2.id();
        perform(MockMvcRequestBuilders.delete(URL_ADMIN_RESTAURANTS + "/" + restaurantId))
                .andExpect(status().isNoContent());
        assertFalse(restaurantRepository.get(restaurantId).isPresent());
    }
}