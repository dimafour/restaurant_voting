package ru.restaurant_voting;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.Assert;

public interface HasId {
    Integer getId();

    void setId(Integer id);

    @JsonIgnore
    default boolean isNew() {
        return this.getId() == null;
    }

    default int id() {
        Assert.notNull(this.getId(), "Entity must has id");
        return this.getId();
    }
}
