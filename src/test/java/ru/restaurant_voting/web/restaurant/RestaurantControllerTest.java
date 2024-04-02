package ru.restaurant_voting.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.restaurant_voting.util.RestaurantUtil;
import ru.restaurant_voting.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurant_voting.web.restaurant.RestaurantTestData.RESTAURANT_TO_MATCHER;
import static ru.restaurant_voting.web.restaurant.RestaurantTestData.restaurantTodayList;
import static ru.restaurant_voting.web.user.UserTestData.USER1_MAIL;


class RestaurantControllerTest extends AbstractControllerTest {

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantController.URL_USER_RESTAURANTS))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(RestaurantUtil.getTosList(restaurantTodayList)));
    }
}