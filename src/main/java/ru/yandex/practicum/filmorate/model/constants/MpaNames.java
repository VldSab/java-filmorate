package ru.yandex.practicum.filmorate.model.constants;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashMap;
import java.util.Map;

public class MpaNames {

    private static final Map<Integer, String> MPA_NAMES = new HashMap<>();

    static {
        MPA_NAMES.put(1, "G");
        MPA_NAMES.put(2, "PG");
        MPA_NAMES.put(3, "PG-13");
        MPA_NAMES.put(4, "R");
        MPA_NAMES.put(5, "NC-17");
    }

    public static String getNameById(int id) {
        return MPA_NAMES.get(id);
    }

    public static Map<Integer, String> getAllNames() {
        return MPA_NAMES;
    }
}
