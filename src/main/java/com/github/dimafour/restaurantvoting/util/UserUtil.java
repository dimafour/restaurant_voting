package com.github.dimafour.restaurantvoting.util;

import com.github.dimafour.restaurantvoting.to.UserTo;
import lombok.experimental.UtilityClass;
import com.github.dimafour.restaurantvoting.model.Role;
import com.github.dimafour.restaurantvoting.model.User;

@UtilityClass
public class UserUtil {

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.USER);
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }
}
