package ru.restaurant_voting.web.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restaurant_voting.model.User;
import ru.restaurant_voting.to.UserTo;
import ru.restaurant_voting.util.UserUtil;
import ru.restaurant_voting.web.AuthUser;

import java.net.URI;

import static ru.restaurant_voting.util.ValidationUtil.assureIdConsistent;
import static ru.restaurant_voting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = ProfileController.URL_USER_PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Profile Controller", description = "Allow to manage your personal profile")
public class ProfileController extends AbstractUserController {
    static final String URL_USER_PROFILE = "/api/profile";

    @GetMapping
    @Operation(summary = "Get your profile info")
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get {}", authUser);
        return authUser.getUser();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete your profile")
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        super.delete(authUser.id());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register new user")
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) {
        log.info("register {}", userTo);
        checkNew(userTo);
        User created = userRepository.prepareAndSave(UserUtil.createNewFromTo(userTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL_USER_PROFILE).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update your profile")
    public void update(@RequestBody @Valid UserTo userTo, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update {} with id={}", userTo, authUser.id());
        assureIdConsistent(userTo, authUser.id());
        User user = authUser.getUser();
        userRepository.prepareAndSave(UserUtil.updateFromTo(user, userTo));
    }

    @GetMapping("/with-votes")
    @Operation(summary = "Get your profile info with votes history")
    public ResponseEntity<User> getWithVotes(@AuthenticationPrincipal AuthUser authUser) {
        return super.getWithVotes(authUser.id());
    }
}
