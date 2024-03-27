package ru.restaurant_voting.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.restaurant_voting.model.Vote;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote>{
}
