package ru.restaurant_voting.web.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restaurant_voting.model.User;

import java.net.URI;
import java.util.List;

import static ru.restaurant_voting.util.ValidationUtil.*;

@RestController
@RequestMapping(value = AdminUserController.URL_ADMIN_USERS, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Admin Profile Controller", description = "Allows to manage all users profiles")
public class AdminUserController extends AbstractUserController {

    static final String URL_ADMIN_USERS = "/api/admin/users";

    @Override
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public User get(@PathVariable int id) {
        return super.get(id);
    }

    @GetMapping("/{id}/with-votes")
    @Operation(summary = "Get user by ID with votes history")
    public ResponseEntity<User> getWithVotes(@PathVariable int id) {
        return super.getWithVotes(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete user by ID")
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @GetMapping
    @Operation(summary = "Get all users list")
    public List<User> getAll() {
        log.info("getAll");
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "name", "email"));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create new user")
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        log.info("create {}", user);
        checkNew(user);
        User created = userRepository.prepareAndSave(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL_ADMIN_USERS + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update user by ID")
    public void update(@Valid @RequestBody User user, @PathVariable int id) {
        assureIdConsistent(user, id);
        checkModificationRestriction(id);
        log.info("update {} with id={}", user, id);
        userRepository.prepareAndSave(user);
    }

    @GetMapping("/by-email")
    @Operation(summary = "Get user by email")
    public User getByEmail(@RequestParam String email) {
        log.info("getByEmail {}", email);
        return userRepository.findByEmailIgnoreCase(email).orElseThrow();
    }
}