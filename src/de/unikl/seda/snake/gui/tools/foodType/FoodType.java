package de.unikl.seda.snake.gui.tools.foodType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum FoodType {
    APPLE,
    ORANGE,
    STRAWBERRY,
    SPECIAL,
    BUG;

    private static final List<FoodType> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static FoodType randomFoodType() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
