package com.github.dimafour.restaurantvoting.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.github.dimafour.restaurantvoting.web.AbstractControllerTest;
import com.github.dimafour.restaurantvoting.repository.VoteRepository;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.github.dimafour.restaurantvoting.web.restaurant.RestaurantTestData.restaurant1;
import static com.github.dimafour.restaurantvoting.web.user.UserTestData.*;
import static com.github.dimafour.restaurantvoting.web.vote.VoteController.TOO_LATE;
import static com.github.dimafour.restaurantvoting.web.vote.VoteController.URL_USER_VOTES;
import static com.github.dimafour.restaurantvoting.web.vote.VoteTestData.*;

public class VoteControllerTest extends AbstractControllerTest {

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_USER_VOTES))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(voteUser1R3.getRestaurant().id()));
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void getRating() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_USER_VOTES + "/rating"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RATING_MATCHER.contentJson(rating));
    }

    @Test
    @WithUserDetails(value = USER2_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_USER_VOTES))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = USER2_MAIL)
    void create() throws Exception {
        int restaurantId = restaurant1.id();
        perform(MockMvcRequestBuilders.post(URL_USER_VOTES)
                .param("restaurantId", String.valueOf(restaurantId)))
                .andExpect(jsonPath("$.id").value(restaurantId));
        Optional<Integer> voteFromRep = voteRepository.getTodayVote(user2.id());
        assertTrue(voteFromRep.isPresent());
        assertEquals(voteFromRep.get(), restaurantId);
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void update() throws Exception {
        changeTime(LocalTime.MAX);
        int restaurantId = restaurant1.id();
        perform(MockMvcRequestBuilders.patch(URL_USER_VOTES)
                .param("restaurantId", String.valueOf(restaurantId)))
                .andExpect(status().isNoContent());
        Optional<Integer> voteFromRep = voteRepository.getTodayVote(user1.id());
        assertTrue(voteFromRep.isPresent());
        assertEquals(voteFromRep.get(), restaurantId);
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void updateTooLate() throws Exception {
        changeTime(LocalTime.MIN);
        int restaurantId = restaurant1.id();
        MvcResult result = perform(MockMvcRequestBuilders.patch(URL_USER_VOTES)
                .param("restaurantId", String.valueOf(restaurantId)))
                .andExpect(status().isConflict())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(TOO_LATE));
        Optional<Integer> voteFromRep = voteRepository.getTodayVote(user1.id());
        assertTrue(voteFromRep.isPresent());
        assertEquals(voteFromRep.get(), voteUser1R3.getRestaurant().id());
    }

    private static void changeTime(LocalTime time) throws NoSuchFieldException, IllegalAccessException {
        Field field = VoteController.class.getDeclaredField("deadline");
        field.setAccessible(true);
        field.set(null, time);
    }
}