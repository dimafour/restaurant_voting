package ru.restaurant_voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.restaurant_voting.error.NotFoundException;

@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, Integer> {

    default T getExisted(int id) throws Exception {
        return findById(id).orElseThrow(() -> new NotFoundException("Entity with id=" + id + " not found"));
    }
}