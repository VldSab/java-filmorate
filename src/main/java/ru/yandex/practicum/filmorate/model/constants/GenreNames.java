package ru.yandex.practicum.filmorate.model.constants;

import java.util.HashMap;
import java.util.Map;

public class GenreNames {
    private static final Map<Integer, String> GENRE_NAMES = new HashMap<>();

    static {
        GENRE_NAMES.put(1, "Комедия");
        GENRE_NAMES.put(2, "Драма");
        GENRE_NAMES.put(3, "Мультфильм");
        GENRE_NAMES.put(4, "Триллер");
        GENRE_NAMES.put(5, "Документальный");
        GENRE_NAMES.put(6, "Боевик");
    }

    public static String getNameById(int id) {
        return GENRE_NAMES.get(id);
    }

    public static Map<Integer, String> getAllNames() {
        return GENRE_NAMES;
    }
}
