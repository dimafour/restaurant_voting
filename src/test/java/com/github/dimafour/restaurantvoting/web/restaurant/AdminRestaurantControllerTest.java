package com.github.dimafour.restaurantvoting.web.restaurant;

import com.github.dimafour.restaurantvoting.error.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.github.dimafour.restaurantvoting.model.Restaurant;
import com.github.dimafour.restaurantvoting.repository.RestaurantRepository;
import com.github.dimafour.restaurantvoting.util.JsonUtil;
import com.github.dimafour.restaurantvoting.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.dimafour.restaurantvoting.web.restaurant.AdminRestaurantController.URL_ADMIN_RESTAURANTS;
import static com.github.dimafour.restaurantvoting.web.restaurant.RestaurantTestData.*;
import static com.github.dimafour.restaurantvoting.web.user.UserTestData.ADMIN_MAIL;
import static com.github.dimafour.restaurantvoting.web.user.UserTestData.USER1_MAIL;

public class AdminRestaurantControllerTest extends AbstractControllerTest {

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
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.getExisted(id), restaurant5);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createNotValid() throws Exception {
        perform(MockMvcRequestBuilders.post(URL_ADMIN_RESTAURANTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(restaurant)))
                .andExpect(status().isUnprocessableEntity());
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
        assertEquals(restaurantRepository.getExisted(restaurantId).getName(), updated.getName());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        int restaurantId = restaurant2.id();
        perform(MockMvcRequestBuilders.delete(URL_ADMIN_RESTAURANTS + "/" + restaurantId))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> restaurantRepository.getExisted(restaurantId));
    }
}