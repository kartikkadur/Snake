package de.unikl.seda.snake.gui.snake;

import de.unikl.seda.snake.gui.controller.Direction;
import de.unikl.seda.snake.gui.tools.Drawable;
import de.unikl.seda.snake.gui.tools.ObstacleType;
import de.unikl.seda.snake.gui.tools.constants.SnakeUIConstants;
import de.unikl.seda.snake.gui.tools.snakeparts.SnakeParts;
import de.unikl.seda.snake.gui.tools.cache.image.ImageCache;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Snake implements Drawable {
    int level;
    // head x coordinate
    private final int rows;
    // head y coordinate
    private final int columns;
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
    private static ArrayList<Point> obstacleLocations = new ArrayList<>();
    // direction
    private final Direction direction;
    // snake image cache
    private final ImageCache imageCache;
    // snake head part
    private static final Map<Point, SnakeParts> SNAKE_HEAD_PARTS = new HashMap<>();
    // random number generator
    private static final Random random = new Random();

    public Snake(int rows, int cols, int level, Direction direction, ImageCache imageCache) {
        this.rows = rows;
        this.columns = cols;
        this.size = SnakeUIConstants.CELL_SIZE;
        this.level = level;
        this.snakePartLocations = new Point[this.getRows() * this.getColumns()];
        this.headPosn = -1;
        this.tailPosn = -1;
        this.partsCount = 0;
        this.direction = direction;
        this.imageCache = imageCache;
        // map snake head part to direction
        SNAKE_HEAD_PARTS.put(direction.getLeft(), SnakeParts.HEAD_LEFT);
        SNAKE_HEAD_PARTS.put(direction.getRight(), SnakeParts.HEAD_RIGHT);
        SNAKE_HEAD_PARTS.put(direction.getTop(), SnakeParts.HEAD_TOP);
        SNAKE_HEAD_PARTS.put(direction.getBottom(), SnakeParts.HEAD_BOTTOM);
    }

    public int getRows() {
        return this.rows;
    }

    public int getColumns() {
        return this.columns;
    }

    public int getSize() {
        return this.size;
    }

    public void move(Point direction) {
        if (this.headPosn == -1) {
            this.snakePartLocations[++this.headPosn] = new Point(this.getRows() / 2, this.getColumns() / 2);
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
            x += this.getColumns();
        } else if (x > this.getColumns()) {
            x -= this.getColumns()+1;
        }
        if (y < 0) {
            y += this.getRows();
        } else if (y > this.getRows()) {
            y -= this.getRows()+1;
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
            int x = Snake.random.nextInt((int) (this.getRows() * 0.75));
            int y = Snake.random.nextInt((int) (this.getColumns() * 0.75));
            System.out.println("x : "+x+" y : "+y);
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
        final Point currentDirection = this.direction.getCurrentDirection();

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
        final Image snakePartImage = imageCache.getSnakePartImage(snakePart);
        final Rectangle snakePartRectangle = getRectangle(snakePartLocations[index]);
        g.drawImage(snakePartImage, snakePartRectangle.x, snakePartRectangle.y, SnakeUIConstants.CELL_SIZE, SnakeUIConstants.CELL_SIZE, null);
    }

    public void renderObstacle(Graphics g, final int x, final int y, ObstacleType obstacleType) {
        final Image obstacleImage = imageCache.getObstacleImage(obstacleType);
        g.drawImage(obstacleImage, x*this.size, y*this.size, SnakeUIConstants.CELL_SIZE, SnakeUIConstants.CELL_SIZE, null);
    }

    public void draw(Graphics g){
        switch (this.level) {
            case 1 -> {
                for (int i = this.tailPosn; i < this.tailPosn + this.partsCount; ++i) {
                    this.renderSnakePart(g, this.snakePartLocations, i % this.snakePartLocations.length);
                }
            }
            case 2 -> {
                Snake.obstacleLocations.clear();
                for (int i = this.tailPosn; i < this.tailPosn + this.partsCount; ++i) {
                    this.renderSnakePart(g, this.snakePartLocations, i % this.snakePartLocations.length);
                }
                for (int j = 0; j <= this.getRows() * this.size; ++j) {
                    this.renderObstacle(g, 0, j, ObstacleType.VERTICAL_OBSTACLE);
                    this.renderObstacle(g, (this.getColumns()-1), j, ObstacleType.VERTICAL_OBSTACLE);
                    Snake.obstacleLocations.add(new Point(0, j));
                    Snake.obstacleLocations.add(new Point((this.getColumns()-1), j));
                }
                for (int k = 0; k <= this.getColumns() * this.size; ++k) {
                    this.renderObstacle(g, k, 0, ObstacleType.HORIZONTAL_OBSTACLE);
                    this.renderObstacle(g, k, (this.getRows() - 1), ObstacleType.HORIZONTAL_OBSTACLE);
                    Snake.obstacleLocations.add(new Point(k, 0));
                    Snake.obstacleLocations.add(new Point(k, (this.getRows() - 1)));
                }
            }
            case 3 -> {
                Snake.obstacleLocations.clear();
                int x1 = (int) (0.25*this.getRows());
                int x2 = (int)(0.75*this.getColumns()-1);
                for (int i = this.tailPosn; i < this.tailPosn + this.partsCount; ++i) {
                    this.renderSnakePart(g, this.snakePartLocations, i % this.snakePartLocations.length);
                }
                for (int j = (int)(0.25*this.getRows()); j <= (int)(0.75*this.getRows()); ++j) {
                    this.renderObstacle(g, x1, j, ObstacleType.VERTICAL_OBSTACLE);
                    this.renderObstacle(g, x2, j, ObstacleType.VERTICAL_OBSTACLE);
                    Snake.obstacleLocations.add(new Point(x1, j));
                    Snake.obstacleLocations.add(new Point(x2, j));
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

