package de.unikl.seda.snake.gui.controller;

import java.awt.*;

public class Direction {
    private static final Point LEFT = new Point(-1, 0);
    private static final Point RIGHT = new Point(1, 0);
    private static final Point TOP = new Point(0,-1);
    private static final Point BOTTOM = new Point(0, 1);
    private Point currDirection;

    Direction(){
        this.currDirection = this.getLeft();
    }

    public Point getLeft(){
        return  this.LEFT;
    }

    public Point getRight(){
        return this.RIGHT;
    }

    public Point getBottom() {
        return this.BOTTOM;
    }

    public Point getTop() {
        return this.TOP;
    }

    public void setCurrentDirection(Point direction){
        this.currDirection = direction;
    }

    public Point getCurrentDirection(){
        return this.currDirection;
    }
}
