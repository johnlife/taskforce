package ru.johnlife.lifetools.data;

/**
 * Created by User on 12/28/2017.
 */

public class Constrain {
    public static int range(int value, int min, int max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    public static int minimum(int value, int min) {
        return (value < min) ? min : value;
    }

    public static int maximum(int value, int max) {
        return (value > max) ? max : value;
    }
}
