package ru.restaurant_voting.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurant_voting.web.AbstractControllerTest;
import ru.restaurant_voting.repository.VoteRepository;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.restaurant_voting.web.restaurant.RestaurantTestData.restaurant1;
import static ru.restaurant_voting.web.user.UserTestData.*;
import static ru.restaurant_voting.web.vote.VoteController.URL_USER_VOTES;
import static ru.restaurant_voting.web.vote.VoteController.TOO_LATE;
import static ru.restaurant_voting.web.vote.VoteTestData.*;

class VoteControllerTest extends AbstractControllerTest {

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_USER_VOTES))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.restaurantId").value(vote1.getRestaurant().id()));
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void getRating() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_USER_VOTES + "/rating"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RATING_MATCHER.contentJson(rating));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = USER2_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_USER_VOTES))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = USER2_MAIL)
    void create() throws Exception {
        int restaurantId = vote2.getRestaurant().id();
        perform(MockMvcRequestBuilders.post(URL_USER_VOTES)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("restaurantId", String.valueOf(restaurantId)))
                .andExpect(jsonPath("$.restaurantId").value(restaurantId));
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
                .contentType(MediaType.APPLICATION_JSON_VALUE)
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
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("restaurantId", String.valueOf(restaurantId)))
                .andExpect(status().isAccepted())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(TOO_LATE));
        Optional<Integer> voteFromRep = voteRepository.getTodayVote(user1.id());
        assertTrue(voteFromRep.isPresent());
        assertEquals(voteFromRep.get(), vote1.getRestaurant().id());
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void delete() throws Exception {
        changeTime(LocalTime.MAX);
        perform(MockMvcRequestBuilders.delete(URL_USER_VOTES))
                .andExpect(status().isNoContent());
        Optional<Integer> voteFromRep = voteRepository.getTodayVote(user1.id());
        assertFalse(voteFromRep.isPresent());
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void deleteTooLate() throws Exception {
        changeTime(LocalTime.MIN);
        MvcResult result = perform(MockMvcRequestBuilders.delete(URL_USER_VOTES))
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(TOO_LATE));
        Optional<Integer> voteFromRep = voteRepository.getTodayVote(user1.id());
        assertTrue(voteFromRep.isPresent());
        assertEquals(voteFromRep.get(), vote1.getRestaurant().id());
    }

    private static void changeTime(LocalTime time) throws NoSuchFieldException, IllegalAccessException {
        Field field = VoteController.class.getDeclaredField("deadline");
        field.setAccessible(true);
        field.set(null, time);
    }
}