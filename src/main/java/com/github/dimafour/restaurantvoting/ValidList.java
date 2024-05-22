package com.github.dimafour.restaurantvoting;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Delegate;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidList<E> implements List<E> {

    @Valid
    @Delegate
    @NotEmpty
    private List<E> list = new ArrayList<>();
}
