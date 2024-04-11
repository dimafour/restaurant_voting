package ru.restaurant_voting.util;

import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import ru.restaurant_voting.HasId;
import ru.restaurant_voting.error.IllegalRequestDataException;
import ru.restaurant_voting.error.NotFoundException;

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

    public static HasId checkContains(@NotNull List<? extends HasId> beans, int id) {
        if (beans.isEmpty()) {
            throw new NotFoundException("List is empty");
        }
        return beans.stream()
                .filter(bean -> bean.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalRequestDataException(beans.getFirst().getClass().getSimpleName() + " id=" + id + " can not be accepted"));
    }
}
