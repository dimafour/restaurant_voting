package ru.restaurant_voting.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.restaurant_voting.model.User;

@Transactional(readOnly = true)
public interface UserRepository extends BaseRepository<User> {
}
