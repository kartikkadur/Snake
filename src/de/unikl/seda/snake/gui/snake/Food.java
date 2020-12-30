package de.unikl.seda.snake.gui.snake;

import de.unikl.seda.snake.gui.tools.Drawable;
import de.unikl.seda.snake.gui.tools.cache.image.ImageCache;
import de.unikl.seda.snake.gui.tools.foodType.FoodType;
import de.unikl.seda.snake.gui.tools.constants.SnakeUIConstants;

import java.awt.*;

public class Food implements Drawable {
    private static int score;
    private final Rectangle rectangle;
    private final Color color;
    private Image foodImage;
    private final ImageCache imageCache;

    public Food(Point point, ImageCache imageCache) {
        this.rectangle = new Rectangle(point.x, point.y, SnakeUIConstants.CELL_SIZE, SnakeUIConstants.CELL_SIZE);
        this.imageCache = imageCache;
        this.color = SnakeUIConstants.DEFAULT_FONT_COLOR;
        this.setFoodType(FoodType.APPLE);
    }

    public int getScore() {
        return score;
    }

    public Rectangle getRectangle() {
        return this.rectangle;
    }

    public void setRectangle(Point point){
        this.getRectangle().x = point.x;
        this.getRectangle().y = point.y;
    }

    public void setFoodType(FoodType foodType){
        if(foodType == FoodType.APPLE || foodType == FoodType.ORANGE){
            score = 1;
            this.foodImage = this.imageCache.getFoodImage(FoodType.APPLE);
        }
        else if(foodType == FoodType.CHERRY){
            score = 3;
            this.foodImage = this.imageCache.getFoodImage(FoodType.CHERRY);
        }
        else if(foodType == FoodType.PEAR){
            score = 2;
            this.foodImage = this.imageCache.getFoodImage(FoodType.PEAR);
        }
    }

    public boolean isHit(Point point) {
        return this.getRectangle().x == point.x && this.getRectangle().y == point.y;
    }

    public boolean isDead(){
        return false;
    }

    public void draw(Graphics g) {
        if(this.foodImage != null){
            g.drawImage(foodImage, this.getRectangle().x * SnakeUIConstants.CELL_SIZE, this.getRectangle().y * SnakeUIConstants.CELL_SIZE, SnakeUIConstants.CELL_SIZE, SnakeUIConstants.CELL_SIZE, null);
        }
        else{
            g.setColor(this.color);
            g.fillOval(this.getRectangle().x * this.getRectangle().width, this.getRectangle().y * this.getRectangle().width, this.getRectangle().width, this.getRectangle().height);
        }
    }
}

