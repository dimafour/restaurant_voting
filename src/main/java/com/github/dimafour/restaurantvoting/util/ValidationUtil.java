package com.github.dimafour.restaurantvoting.util;

import com.github.dimafour.restaurantvoting.HasId;
import com.github.dimafour.restaurantvoting.error.IllegalRequestDataException;
import com.github.dimafour.restaurantvoting.error.NotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ValidationUtil {

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must has id=" + id);
        }
    }

    public static <T extends HasId> T checkContains(@NotNull List<T> beans, int id) {
        if (beans.isEmpty()) {
            throw new NotFoundException("List is empty");
        }
        return beans.stream()
                .filter(bean -> bean.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalRequestDataException(beans.getFirst().getClass().getSimpleName() + " id=" + id + " can not be accepted"));
    }
}
