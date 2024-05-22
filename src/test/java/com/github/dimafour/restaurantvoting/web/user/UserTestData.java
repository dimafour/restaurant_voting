package com.github.dimafour.restaurantvoting.web.user;

import com.github.dimafour.restaurantvoting.model.Role;
import com.github.dimafour.restaurantvoting.model.User;
import com.github.dimafour.restaurantvoting.util.JsonUtil;
import com.github.dimafour.restaurantvoting.web.MatcherFactory;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.github.dimafour.restaurantvoting.web.vote.VoteTestData.*;

public class UserTestData {
    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "registered", "votes", "password");

    public static final int USER1_ID = 1;
    public static final int USER2_ID = 2;
    public static final int USER3_ID = 3;
    public static final int ADMIN_ID = 4;
    public static final int USER5_ID = 5;
    public static final int NOT_FOUND_ID = 100;

    public static final String USER1_MAIL = "user1@ya.ru";
    public static final String USER2_MAIL = "user2@ya.ru";
    public static final String USER3_MAIL = "user3@ya.ru";
    public static final String ADMIN_MAIL = "admin@ya.ru";
    public static final String USER5_MAIL = "user5@ya.ru";

    public static final User user1 = new User(USER1_ID, "User1", USER1_MAIL, "user1", Role.USER);
    public static final User user2 = new User(USER2_ID, "User2", USER2_MAIL, "user2", Role.USER);
    public static final User user3 = new User(USER3_ID, "User3", USER3_MAIL, "user3", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", ADMIN_MAIL, "admin", Role.USER, Role.ADMIN);
    public static final User user5 = new User(USER5_ID, "User5", USER5_MAIL, "user5", Role.USER);

    static {
        user1.setVotes(List.of(voteUser1R3));
        admin.setVotes(List.of(voteAdminR2));
    }

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", new Date(), Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        return new User(USER5_ID, "UpdatedName", USER5_MAIL, "newPass", new Date(), List.of(Role.USER));
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
