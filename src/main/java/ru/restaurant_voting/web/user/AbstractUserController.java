package ru.restaurant_voting.web.user;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ru.restaurant_voting.error.UpdateRestrictionException;
import ru.restaurant_voting.model.User;
import ru.restaurant_voting.repository.UserRepository;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class AbstractUserController {
    protected final Logger log = getLogger(getClass());

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    private UniqueMailValidator emailValidator;

    private static final List<Integer> notUpdatebleUsersIdList = List.of(1, 2, 3, 4);
    private static final String NOT_UPDATABLE = "You can not delete or update default profiles";

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

    public ResponseEntity<User> getWithVotes(int id) {
        log.info("get user id = {} with votes history", id);
        return ResponseEntity.of(userRepository.getWithVotes(id));
    }

    protected void checkModificationRestriction(int id) {
        log.info("check modification restriction for user id={}", id);
        if (notUpdatebleUsersIdList.contains(id)) {
            throw new UpdateRestrictionException(NOT_UPDATABLE);
        }
    }
}
