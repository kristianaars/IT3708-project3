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

    public static SIGraphDirection Reverse(SIGraphDirection direction) {
        return switch (direction) {
            case Right -> SIGraphDirection.Left;
            case Left -> SIGraphDirection.Right;
            case Up -> SIGraphDirection.Down;
            case Down -> SIGraphDirection.Up;
            default -> direction;
        };
    }

    public static String ToStringSymbol(SIGraphDirection direction) {
        return switch (direction) {
            case Right -> "→";
            case Left -> "←";
            case Up -> "↑";
            case Down -> "↓";
            default -> "•";
        };
    }
}
