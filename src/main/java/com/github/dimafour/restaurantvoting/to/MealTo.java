package com.github.dimafour.restaurantvoting.to;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class MealTo extends NamedTo {
    @NotNull
    @Min(1)
    Integer price;

    public MealTo(Integer id, String name, Integer price) {
        super(id, name);
        this.price = price;
    }

    @Override
    public String toString() {
        return super.toString() + '[' + price + ']';
    }
}
