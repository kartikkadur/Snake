package de.unikl.seda.snake.gui.controller;

import java.awt.event.KeyEvent;

public class KeyController {
    int currKeyCode = 0;
    private final MainController mainController;
    private Direction direction;

    public KeyController(MainController mainController, Direction direction) {
        this.mainController = mainController;
        this.direction = direction;
    }

    public MainController getMainController() {
        return this.mainController;
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if ( keyCode == KeyEvent.VK_SPACE) {
            if (this.getMainController().getGameController().isPaused()) {
                this.getMainController().getGameController().resumeGame();
            }
            else {
                this.getMainController().getGameController().pauseGame();
            }
        }
        else if ( keyCode == KeyEvent.VK_ESCAPE){
            this.getMainController().getGameController().setRunning(false);

        }
        else if (keyCode == KeyEvent.VK_UP && this.currKeyCode != KeyEvent.VK_DOWN) {
            this.currKeyCode = KeyEvent.VK_UP;
            this.direction.setCurrentDirection(direction.getTop());
        }
        else if (keyCode == KeyEvent.VK_LEFT && this.currKeyCode != KeyEvent.VK_RIGHT) {
            this.currKeyCode = KeyEvent.VK_LEFT;
            this.direction.setCurrentDirection(direction.getLeft());
        }
        else if (keyCode == KeyEvent.VK_RIGHT && this.currKeyCode != KeyEvent.VK_LEFT) {
            this.currKeyCode = KeyEvent.VK_RIGHT;
            this.direction.setCurrentDirection(direction.getRight());
        }
        else if (keyCode == KeyEvent.VK_DOWN && this.currKeyCode != KeyEvent.VK_UP) {
            this.currKeyCode = KeyEvent.VK_DOWN;
            this.direction.setCurrentDirection(direction.getBottom());
        }
    }
}
