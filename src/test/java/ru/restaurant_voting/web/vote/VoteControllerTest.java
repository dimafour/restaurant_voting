package ru.restaurant_voting.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.restaurant_voting.AbstractControllerTest;
import ru.restaurant_voting.repository.VoteRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.restaurant_voting.web.user.UserTestData.*;
import static ru.restaurant_voting.web.vote.VoteController.REST_URL;
import static ru.restaurant_voting.web.vote.VoteTestData.vote1;
import static ru.restaurant_voting.web.vote.VoteTestData.vote2;

class VoteControllerTest extends AbstractControllerTest {

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.restaurantId").value(vote1.getRestaurant().id()));
    }

    @Test
    @WithUserDetails(value = USER2_MAIL)
    void create() throws Exception {
        int restaurantId = vote2.getRestaurant().id();
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("restaurantId", String.valueOf(restaurantId)))
                .andExpect(jsonPath("$.restaurantId").value(restaurantId));
        Optional<Integer> voteFromRep = voteRepository.getTodayVote(user2.id());
        assertTrue(voteFromRep.isPresent());
        assertEquals(voteFromRep.get(), restaurantId);
    }
}