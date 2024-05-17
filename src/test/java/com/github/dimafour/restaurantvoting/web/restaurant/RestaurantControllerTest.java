package com.github.dimafour.restaurantvoting.web.restaurant;

import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.github.dimafour.restaurantvoting.web.AbstractControllerTest;

import static com.github.dimafour.restaurantvoting.util.RestaurantUtil.getTosList;
import static com.github.dimafour.restaurantvoting.web.restaurant.RestaurantTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.dimafour.restaurantvoting.web.user.UserTestData.USER1_MAIL;

public class RestaurantControllerTest extends AbstractControllerTest {

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantController.URL_USER_RESTAURANTS))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(getTosList(restaurantTodayList)));
    }
}