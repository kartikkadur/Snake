package de.unikl.seda.snake.gui.snake;

import de.unikl.seda.snake.gui.controller.Direction;
import de.unikl.seda.snake.gui.controller.MainController;
import de.unikl.seda.snake.gui.snake.variable.SnakeUIVariables;
import de.unikl.seda.snake.gui.tools.Drawable;
import de.unikl.seda.snake.gui.tools.ObstacleType;
import de.unikl.seda.snake.gui.snake.constants.SnakeUIConstants;
import de.unikl.seda.snake.gui.tools.leveleditor.LevelEditor;
import de.unikl.seda.snake.gui.tools.snakeparts.SnakeParts;
import de.unikl.seda.snake.gui.tools.cache.image.ImageCache;

import java.awt.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class Snake implements Drawable {
    int level;
    // square size
    private final int size;
    // snake parts count
    private int partsCount;
    // head position
    private int headPosn;
    // tail position
    private int tailPosn;
    // snake parts
    private final Point[] snakePartLocations;
    // obstacle locations
    private static final ArrayList<Point> obstacleLocations = new ArrayList<>();
    // direction
    private final MainController mainController;
    // snake head part
    private static final Map<Point, SnakeParts> SNAKE_HEAD_PARTS = new HashMap<>();
    // random number generator
    private static final Random random = new Random();

    public Snake(MainController mainController) {
        this.mainController = mainController;
        this.size = SnakeUIConstants.CELL_SIZE;
        this.level = this.mainController.getSnakeGameSettings().getLevel();
        this.snakePartLocations = new Point[this.mainController.getSnakeGameSettings().getRows() * this.mainController.getSnakeGameSettings().getCols()];
        this.headPosn = -1;
        this.tailPosn = -1;
        this.partsCount = 0;
        // map snake head part to direction
        SNAKE_HEAD_PARTS.put(this.mainController.getDirection().getLeft(), SnakeParts.HEAD_LEFT);
        SNAKE_HEAD_PARTS.put(this.mainController.getDirection().getRight(), SnakeParts.HEAD_RIGHT);
        SNAKE_HEAD_PARTS.put(this.mainController.getDirection().getTop(), SnakeParts.HEAD_TOP);
        SNAKE_HEAD_PARTS.put(this.mainController.getDirection().getBottom(), SnakeParts.HEAD_BOTTOM);
    }

    public void move(Point direction) {
        if (this.headPosn == -1) {
            this.snakePartLocations[++this.headPosn] = new Point(
                    this.mainController.getSnakeGameSettings().getRows() / 2,
                    this.mainController.getSnakeGameSettings().getCols() / 2);
            ++this.partsCount;
        } else {
            int newX = this.snakePartLocations[this.headPosn].x + direction.x;
            int newY = this.snakePartLocations[this.headPosn].y + direction.y;
            this.headPosn = (this.headPosn + 1) % this.snakePartLocations.length;
            this.snakePartLocations[this.headPosn] = this.borderCorrection(newX, newY);
        }
    }

    public void addPart(boolean request) {
        if (!request) {
            this.tailPosn = (this.tailPosn + 1) % this.snakePartLocations.length;
        } else {
            ++this.partsCount;
        }
    }

    public Point getHead() {
        return this.headPosn >= 0 ? this.snakePartLocations[this.headPosn] : null;
    }

    public Point borderCorrection(int x, int y) {
        if (x < 0) {
            x += this.mainController.getSnakeGameSettings().getCols();
        } else if (x > this.mainController.getSnakeGameSettings().getCols()) {
            x -= this.mainController.getSnakeGameSettings().getCols()+1;
        }
        if (y < 0) {
            y += this.mainController.getSnakeGameSettings().getRows();
        } else if (y > this.mainController.getSnakeGameSettings().getRows()) {
            y -= this.mainController.getSnakeGameSettings().getRows()+1;
        }
        return new Point(x, y);
    }

    // checks for snake overlapping with blocks or itself
    public boolean isOverlapping() {
        Point head = this.snakePartLocations[this.headPosn];
        Point part;
        if(this.partsCount > 1){
            // iterate over the snake parts and check if the head is overlapping with
            // any of the snake parts
            for (int i=this.tailPosn; i<this.tailPosn+this.partsCount-1; ++i){
                part = this.snakePartLocations[i % this.snakePartLocations.length];
                if (head.x == part.x  && head.y == part.y ){
                    return true;
                }
            }
        }
        if (obstacleLocations.contains(head)){
            return true;
        }
        return false;
    }

    // code for random food placement that is not overlapping with the
    // snake
    public Point foodPlacement() {
        Point result = null;
        int k = 0;
        for(int maxIt = this.snakePartLocations.length; result == null && k < maxIt; ++k) {
            int x = Snake.random.nextInt((int)(this.mainController.getSnakeGameSettings().getRows()*0.75));
            int y = Snake.random.nextInt((int)(this.mainController.getSnakeGameSettings().getCols()*0.75));
            Point p;

            if (obstacleLocations.contains(new Point(x,y))){
                continue;
            }

            for(int i = this.tailPosn; i < this.tailPosn+this.partsCount; ++i) {
                p = this.snakePartLocations[i % this.snakePartLocations.length];
                if (x == p.x && y == p.y) {
                    break;
                }
                if (i % this.snakePartLocations.length == this.headPosn){
                    result = new Point(x, y);
                }
            }
        }
        return result;
    }

    public SnakeParts getSnakePart(final int index) {
        final Point currentDirection = this.mainController.getDirection().getCurrentDirection();

        if (index == this.headPosn % this.snakePartLocations.length) {
            return SNAKE_HEAD_PARTS.get(currentDirection);
        }

        else if (index == this.tailPosn % this.snakePartLocations.length) {
            return this.getTailPart(this.snakePartLocations[index % this.snakePartLocations.length],
                                    this.snakePartLocations[(index+1) % this.snakePartLocations.length]);
        }

        final Point nextSnakePartLocation = this.snakePartLocations[
                (this.snakePartLocations.length + index - 1) % this.snakePartLocations.length];
        final Point previousSnakePartLocation = this.snakePartLocations[(index + 1) % this.snakePartLocations.length];

        if (previousSnakePartLocation.x == nextSnakePartLocation.x) {
            return SnakeParts.VERTICAL;
        }

        if (previousSnakePartLocation.y == nextSnakePartLocation.y) {
            return SnakeParts.HORIZONTAL;
        }

        return this.getCornerPart(this.snakePartLocations[index % this.snakePartLocations.length], previousSnakePartLocation, nextSnakePartLocation);
    }

    private SnakeParts getCornerPart(final Point currentPartLocation, final Point previousPartLocation, final Point nextPartLocation) {
        final boolean previousXSmall = previousPartLocation.x < currentPartLocation.x;
        final boolean previousXLarge = previousPartLocation.x > currentPartLocation.x;

        final boolean previousYSmall = previousPartLocation.y < currentPartLocation.y;
        final boolean previousYLarge = previousPartLocation.y > currentPartLocation.y;

        final boolean nextXSmall = nextPartLocation.x < currentPartLocation.x;
        final boolean nextXLarge = nextPartLocation.x > currentPartLocation.x;

        final boolean nextYSmall = nextPartLocation.y < currentPartLocation.y;
        final boolean nextYLarge = nextPartLocation.y > currentPartLocation.y;

        if (Math.abs(currentPartLocation.x - previousPartLocation.x) > 1 ||
                Math.abs(currentPartLocation.y - previousPartLocation.y) > 1){
            if (nextXSmall && previousYLarge
                || previousXLarge && nextYSmall) {
                return SnakeParts.BOTTOM_RIGHT;
            }

            if (nextXLarge && previousYLarge
                || previousXLarge && nextYSmall) {
                return SnakeParts.BOTTOM_LEFT;
            }

            if (nextXSmall && previousYLarge
                || previousXLarge && nextYLarge) {
                return SnakeParts.TOP_RIGHT;
            }
            return SnakeParts.TOP_LEFT;
        }
        else if(Math.abs(currentPartLocation.x - nextPartLocation.x) > 1 ||
                Math.abs(currentPartLocation.y - nextPartLocation.y) > 1){
            if (previousXSmall && nextYLarge
                || nextXLarge && previousYSmall) {
                return SnakeParts.BOTTOM_RIGHT;
            }

            if (nextXSmall && previousYSmall
                || previousXLarge && nextYLarge) {
                return SnakeParts.BOTTOM_LEFT;
            }

            if (nextXLarge && previousYLarge
                || previousXSmall && nextYSmall) {
                return SnakeParts.TOP_RIGHT;
            }
            return SnakeParts.TOP_LEFT;
        }
        else {
            if (previousXSmall && nextYSmall
                    || nextXSmall && previousYSmall) {
                return SnakeParts.BOTTOM_RIGHT;
            }

            if (previousXSmall && nextYLarge
                    || nextXSmall && previousYLarge) {
                return SnakeParts.TOP_RIGHT;
            }

            if (previousYSmall && nextXLarge
                    || nextYSmall && previousXLarge) {
                return SnakeParts.BOTTOM_LEFT;
            }

            return SnakeParts.TOP_LEFT;
        }
    }

    private SnakeParts getTailPart(final Point currentPartLocation, final Point previousPartLocation) {

        final boolean previousXSmall = previousPartLocation.x < currentPartLocation.x;
        final boolean previousYSmall = previousPartLocation.y < currentPartLocation.y;
        final boolean previousYLarge = previousPartLocation.y > currentPartLocation.y;
        final boolean previousXLarge = previousPartLocation.x > currentPartLocation.x;

        if (previousXSmall) {
            return SnakeParts.TAIL_LEFT;
        }

        if (previousYSmall) {
            return SnakeParts.TAIL_TOP;
        }

        if (previousXLarge) {
            return SnakeParts.TAIL_RIGHT;
        }

        return SnakeParts.TAIL_BOTTOM;
    }

    private Rectangle getRectangle(final Point location) {
        final int x1 = location.x * SnakeUIConstants.CELL_SIZE;
        final int y1 = location.y * SnakeUIConstants.CELL_SIZE;

        return new Rectangle(x1, y1, SnakeUIConstants.CELL_SIZE, SnakeUIConstants.CELL_SIZE);
    }

    public void renderSnakePart(Graphics g, final Point[] snakePartLocations, final int index) {
        final SnakeParts snakePart = getSnakePart(index);
        final Image snakePartImage = this.mainController.getImageCache().getSnakePartImage(snakePart);
        final Rectangle snakePartRectangle = getRectangle(snakePartLocations[index]);
        g.drawImage(snakePartImage, snakePartRectangle.x, snakePartRectangle.y, SnakeUIConstants.CELL_SIZE, SnakeUIConstants.CELL_SIZE, null);
    }

    public void renderObstacle(Graphics g, final int x, final int y, ObstacleType obstacleType) {
        final Image obstacleImage = this.mainController.getImageCache().getObstacleImage(obstacleType);
        g.drawImage(obstacleImage, x*this.size, y*this.size, SnakeUIConstants.CELL_SIZE, SnakeUIConstants.CELL_SIZE, null);
    }

    public void draw(Graphics g) {
        switch (this.level){
            case 0 -> {
                for (int i = this.tailPosn; i < this.tailPosn + this.partsCount; ++i) {
                    this.renderSnakePart(g, this.snakePartLocations, i % this.snakePartLocations.length);
                }
            }
            case 1 -> {
                Snake.obstacleLocations.clear();
                for (int i = this.tailPosn; i < this.tailPosn + this.partsCount; ++i) {
                    this.renderSnakePart(g, this.snakePartLocations, i % this.snakePartLocations.length);
                }
                for (int j = 0; j <= this.mainController.getSnakeGameSettings().getRows() * this.size; ++j) {
                    this.renderObstacle(g, 0, j, ObstacleType.VERTICAL_OBSTACLE);
                    this.renderObstacle(g, (this.mainController.getSnakeGameSettings().getCols()-1), j, ObstacleType.VERTICAL_OBSTACLE);
                    Snake.obstacleLocations.add(new Point(0, j));
                    Snake.obstacleLocations.add(new Point((this.mainController.getSnakeGameSettings().getCols()-1), j));
                }
                for (int k = 0; k <= this.mainController.getSnakeGameSettings().getCols() * this.size; ++k) {
                    this.renderObstacle(g, k, 0, ObstacleType.HORIZONTAL_OBSTACLE);
                    this.renderObstacle(g, k, (this.mainController.getSnakeGameSettings().getRows() - 1), ObstacleType.HORIZONTAL_OBSTACLE);
                    Snake.obstacleLocations.add(new Point(k, 0));
                    Snake.obstacleLocations.add(new Point(k, (this.mainController.getSnakeGameSettings().getRows() - 1)));
                }
            }
            case 2 -> {
                Snake.obstacleLocations.clear();
                int x1 = (int) (0.25*this.mainController.getSnakeGameSettings().getRows());
                int x2 = (int)(0.75*this.mainController.getSnakeGameSettings().getCols()-1);
                for (int i = this.tailPosn; i < this.tailPosn + this.partsCount; ++i) {
                    this.renderSnakePart(g, this.snakePartLocations, i % this.snakePartLocations.length);
                }
                for (int j = (int)(0.25*this.mainController.getSnakeGameSettings().getRows()); j <= (int)(0.75*this.mainController.getSnakeGameSettings().getRows()); ++j) {
                    this.renderObstacle(g, x1, j, ObstacleType.VERTICAL_OBSTACLE);
                    this.renderObstacle(g, x2, j, ObstacleType.VERTICAL_OBSTACLE);
                    Snake.obstacleLocations.add(new Point(x1, j));
                    Snake.obstacleLocations.add(new Point(x2, j));
                }
            }
            default -> {
                Snake.obstacleLocations.clear();
                ArrayList<LevelEditor> levelEditor = LevelEditor.readLevelEdit();
                for (int i = this.tailPosn; i < this.tailPosn + this.partsCount; ++i) {
                    this.renderSnakePart(g, this.snakePartLocations, i % this.snakePartLocations.length);
                }
                for (Point p : levelEditor.get(this.level).getObstacleLocation()) {
                    Snake.this.renderObstacle(g, p.x, p.y, ObstacleType.HORIZONTAL_OBSTACLE);
                    Snake.obstacleLocations.add(new Point(p.x, p.y));
                }
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(int i = this.tailPosn; i < this.tailPosn + this.partsCount; ++i) {
            Point p = this.snakePartLocations[i % this.snakePartLocations.length];
            sb.append(" (" + p.x + "|" + p.y + ") ");
        }

        return sb.toString();
    }
}

