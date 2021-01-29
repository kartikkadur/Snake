package de.unikl.seda.snake.gui.tools.cache.image;

import java.awt.*;
import de.unikl.seda.snake.gui.tools.Icon.Icon;
import de.unikl.seda.snake.gui.tools.ObstacleType;
import de.unikl.seda.snake.gui.tools.backgroundType.Backgrounds;
import de.unikl.seda.snake.gui.tools.foodType.FoodType;
import de.unikl.seda.snake.gui.tools.snakeparts.SnakeParts;

public interface ImageCache {
    Image getIcon(Icon icon);

    Image getFoodImage(FoodType foodType);

    Image getSnakePartImage(SnakeParts snakePart);

    Image getObstacleImage(ObstacleType obstacleType);

    Image getBackgroundImage(Backgrounds backgrounds);
}
