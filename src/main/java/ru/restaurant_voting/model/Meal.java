package ru.restaurant_voting.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

@Entity
@Table(name = "meal", uniqueConstraints = {@UniqueConstraint(
        columnNames = {"restaurant_id", "name"}, name = "meal_unique_restaurant_id_name_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meal extends NamedEntity {

    @Column(name = "price", nullable = false)
    @NotBlank
    @Range(min = 1, max = 500000)
    private int price; // in cents

    @Column(name = "meal_date", nullable = false)
    @NotNull
    private LocalDate meal_date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public Meal(Integer id, String name, int price, LocalDate meal_date, Restaurant restaurant) {
        super(id, name);
        this.price = price;
        this.meal_date = meal_date;
        this.restaurant = restaurant;
    }
}
