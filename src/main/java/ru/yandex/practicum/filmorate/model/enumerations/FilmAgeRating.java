package ru.yandex.practicum.filmorate.model.enumerations;

public enum FilmAgeRating {
    G,
    PG,
    PG13,
    R,
    NC17;

    public FilmAgeRating nameToEnum(String name) {
        switch (name) {
            case "G":
                return G;
            case "PG":
                return PG;
            case "PG-13":
                return PG13;
            case "R":
                return R;
            case "NC-17":
                return NC17;
            default:
                throw new IllegalStateException("Unexpected value: " + name);
        }
    }

}
