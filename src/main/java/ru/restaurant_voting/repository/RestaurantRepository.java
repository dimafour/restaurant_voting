package ru.restaurant_voting.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.restaurant_voting.model.Restaurant;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant>{
}
