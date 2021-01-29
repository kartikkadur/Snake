package de.unikl.seda.snake.gui.snake.constants;

import java.awt.*;

public final class SnakeUIConstants {
    public static final int CELL_SIZE = 20;

    public static final Color DEFAULT_FONT_COLOR = Color.LIGHT_GRAY;
    public static final int FOOD_ANIMATION_FREQ = 10;
    public static final String ABOUT="WELCOME TO SNAKE WORLD\n"+
                                    "Snake World is a simple fun game to play.\n" +
                                    "There are 3 default levels. The player can create.\n" +
                                    "his own levels using the level editor\n"+
                                    "Enjoy the game\n\n"+
                                    "Version - 2.0\n" +
                                    "Version date - 28th January 2021\n" +
                                    "Contact -\nShilpa Narayan\nSayali Barve\nKartik Kadur";
    public static final String GAME_OVER = "GAME OVER";
    public static final String INSTRUCTIONS = "High Score Board";

    public static final int width = 24;
    public static final int height = 32;
    public static final String font = "Monospaced";
    public static final Color menuFontColor = Color.ORANGE;
    public static final int SCALE = 5;
    public static final int FPS_INCREASE = 20;
    public static final int SPEED_CHANGE_LIMIT = 10;
    public static final int DEFAULT_FPS = 10;
    public static final String DEFAULT_PLAYER_NAME = "Player";
    public static final int DEFAULT_POINTS = 0;
    public static final int DEFAULT_LEVEL = 1;

    private SnakeUIConstants() {
        throw new IllegalAccessError();
    }
}
