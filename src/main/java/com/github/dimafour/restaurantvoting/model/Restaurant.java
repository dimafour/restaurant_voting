package com.github.dimafour.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.dimafour.restaurantvoting.HasId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "restaurant", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity implements HasId {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<Meal> menu;

    public Restaurant(Integer id, String name) {
        super(id, name);
    }

}
