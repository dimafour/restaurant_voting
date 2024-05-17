package com.github.dimafour.restaurantvoting.web.user;

import com.github.dimafour.restaurantvoting.to.UserTo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.github.dimafour.restaurantvoting.model.User;
import com.github.dimafour.restaurantvoting.util.UserUtil;
import com.github.dimafour.restaurantvoting.web.AuthUser;

import java.net.URI;

import static com.github.dimafour.restaurantvoting.util.ValidationUtil.assureIdConsistent;
import static com.github.dimafour.restaurantvoting.util.ValidationUtil.checkNew;

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
        int id = authUser.id();
        assureIdConsistent(userTo, id);
        checkModificationRestriction(id);
        User user = authUser.getUser();
        log.info("update {} with id={}", userTo, id);
        userRepository.prepareAndSave(UserUtil.updateFromTo(user, userTo));
    }
}
