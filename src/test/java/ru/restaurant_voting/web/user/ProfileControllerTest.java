package ru.restaurant_voting.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import ru.restaurant_voting.model.User;
import ru.restaurant_voting.repository.UserRepository;
import ru.restaurant_voting.to.UserTo;
import ru.restaurant_voting.util.JsonUtil;
import ru.restaurant_voting.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurant_voting.util.UserUtil.*;
import static ru.restaurant_voting.web.user.ProfileController.URL_USER_PROFILE;
import static ru.restaurant_voting.web.user.UserTestData.*;

public class ProfileControllerTest extends AbstractControllerTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_USER_PROFILE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(user1));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_USER_PROFILE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER5_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL_USER_PROFILE))
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(userRepository.findAll(), user1, user2, user3, admin);
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void deleteNotUpdatable() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL_USER_PROFILE))
                .andExpect(status().isForbidden());
    }

    @Test
    void register() throws Exception {
        UserTo newTo = new UserTo(null, "newName", "newemail@ya.ru", "newPassword");
        User newUser = createNewFromTo(newTo);
        ResultActions action = perform(MockMvcRequestBuilders.post(URL_USER_PROFILE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        User created = USER_MATCHER.readFromJson(action);
        int newId = created.id();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userRepository.getExisted(newId), newUser);
    }

    @Test
    void registerInvalid() throws Exception {
        UserTo newTo = new UserTo(null, null, null, null);
        perform(MockMvcRequestBuilders.post(URL_USER_PROFILE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void registerDuplicate() throws Exception {
        UserTo newTo = new UserTo(null, "newName", USER1_MAIL, "newpass");
        perform(MockMvcRequestBuilders.post(URL_USER_PROFILE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void updateNotUpdatable() throws Exception {
        UserTo updatedTo = new UserTo(null, "newName", USER1_MAIL, "newPassword");
        perform(MockMvcRequestBuilders.put(URL_USER_PROFILE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = USER5_MAIL)
    void update() throws Exception {
        UserTo updatedTo = new UserTo(null, "newName", USER5_MAIL, "newPassword");
        perform(MockMvcRequestBuilders.put(URL_USER_PROFILE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());
        User user = createNewFromTo(updatedTo);
        user.setId(USER5_ID);
        USER_MATCHER.assertMatch(userRepository.getExisted(USER5_ID), user);
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void getWithVotes() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_USER_PROFILE + "/with-votes"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_WITH_VOTES_MATCHER.contentJson(user1));
    }
}