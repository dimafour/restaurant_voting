package ru.restaurant_voting.to;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.hibernate.validator.constraints.Range;

@Value
@EqualsAndHashCode(callSuper = true)
public class MealTo extends NamedTo {
    @NotNull
    @Range(min = 1, max = 5000_00)
    Integer price;

    public MealTo(Integer id, String name, Integer price) {
        super(id, name);
        this.price = price;
    }
}
