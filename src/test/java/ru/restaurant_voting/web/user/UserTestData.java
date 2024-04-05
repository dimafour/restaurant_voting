package ru.restaurant_voting.web.user;

import ru.restaurant_voting.model.Role;
import ru.restaurant_voting.model.User;
import ru.restaurant_voting.util.JsonUtil;
import ru.restaurant_voting.web.MatcherFactory;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.restaurant_voting.web.vote.VoteTestData.vote1;
import static ru.restaurant_voting.web.vote.VoteTestData.vote4;

public class UserTestData {
    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "registered", "votes", "password");
    public static MatcherFactory.Matcher<User> USER_WITH_VOTES_MATCHER =
            MatcherFactory.usingAssertions(User.class,
                    (a, e) -> assertThat(a).usingRecursiveComparison()
                            .ignoringFields("registered", "votes.user", "votes.id", "password", "votes.restaurant.menu").isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });

    public static final int USER1_ID = 1;
    public static final int USER2_ID = 2;
    public static final int USER3_ID = 3;
    public static final int ADMIN_ID = 4;
    public static final int NOT_FOUND_ID = 100;

    public static final String USER1_MAIL = "user1@ya.ru";
    public static final String USER2_MAIL = "user2@ya.ru";
    public static final String USER3_MAIL = "user3@ya.ru";
    public static final String ADMIN_MAIL = "admin@ya.ru";

    public static final User user1 = new User(USER1_ID, "User1", USER1_MAIL, "user1", Role.USER);
    public static final User user2 = new User(USER2_ID, "User2", USER2_MAIL, "user2", Role.USER);
    public static final User user3 = new User(USER3_ID, "User3", USER3_MAIL, "user3", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", ADMIN_MAIL, "admin", Role.USER, Role.ADMIN);

    static {
        user1.setVotes(List.of(vote1));
        admin.setVotes(List.of(vote4));
    }

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", new Date(), Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        return new User(USER1_ID, "UpdatedName", USER1_MAIL, "newPass", new Date(), List.of(Role.ADMIN));
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
