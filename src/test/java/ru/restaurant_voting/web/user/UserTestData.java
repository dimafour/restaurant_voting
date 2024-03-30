package ru.restaurant_voting.web.user;

import ru.restaurant_voting.model.Role;
import ru.restaurant_voting.model.User;

public class UserTestData {
    public static final int USER1_ID = 1;
    public static final int USER2_ID = 2;
    public static final int USER3_ID = 3;
    public static final int ADMIN_ID = 4;
    public static final String USER1_MAIL = "user1@ya.ru";
    public static final String USER2_MAIL = "user2@ya.ru";
    public static final String USER3_MAIL = "user3@ya.ru";
    public static final String ADMIN_MAIL = "admin@ya.ru";

    public static final User user1 = new User(USER1_ID, "User", USER1_MAIL, "user1", Role.USER);
    public static final User user2 = new User(USER2_ID, "User", USER2_MAIL, "user2", Role.USER);
    public static final User user3 = new User(USER3_ID, "User", USER3_MAIL, "user3", Role.USER);
    public static final User admin = new User(ADMIN_ID, "User", ADMIN_MAIL, "admin", Role.USER, Role.ADMIN);
}