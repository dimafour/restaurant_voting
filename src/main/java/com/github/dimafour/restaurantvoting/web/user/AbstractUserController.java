package com.github.dimafour.restaurantvoting.web.user;

import com.github.dimafour.restaurantvoting.error.UpdateRestrictionException;
import com.github.dimafour.restaurantvoting.repository.UserRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import com.github.dimafour.restaurantvoting.model.User;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class AbstractUserController {
    private static final List<Integer> notUpdatebleUsersIdList = List.of(1, 2, 3, 4);
    private static final String NOT_UPDATABLE = "You can not delete or update default profiles";

    protected final Logger log = getLogger(getClass());

    @Autowired
    private UniqueMailValidator emailValidator;

    @Autowired
    protected UserRepository userRepository;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    public User get(int id) {
        log.info("get user id={}", id);
        return userRepository.getExisted(id);
    }

    public void delete(int id) {
        checkModificationRestriction(id);
        log.info("delete user id={}", id);
        userRepository.deleteExisted(id);
    }

    protected void checkModificationRestriction(int id) {
        log.info("check modification restriction for user id={}", id);
        if (notUpdatebleUsersIdList.contains(id)) {
            throw new UpdateRestrictionException(NOT_UPDATABLE);
        }
    }
}
