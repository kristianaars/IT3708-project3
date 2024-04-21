package model;

import java.util.Random;

public enum SIGraphDirection {
    Right, Left, Up, Down, End,
    Visited;

    public static SIGraphDirection GetRandom() {
        int r = (new Random()).nextInt(5);

        return switch (r) {
            case 0 -> Right;
            case 1 -> Left;
            case 2 -> Up;
            case 3 -> Down;
            case 4 -> End;
            default -> null;
        };
    }
}
