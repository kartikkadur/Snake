package de.unikl.seda.snake.gui.controller;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyController extends KeyAdapter{
    int currKeyCode = 0;
    private final MainController mainController;
    private Direction direction;

    public KeyController(MainController mainController) {
        this.mainController = mainController;
        this.direction = this.mainController.getDirection();
    }

    public MainController getMainController() {
        return this.mainController;
    }

    @Override
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
            int result = JOptionPane.showConfirmDialog(null ,"Are you Sure? You want to exit?", "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if(result == JOptionPane.YES_OPTION){
                this.getMainController().getGameController().stopGame();
            }
            else if(result == JOptionPane.NO_OPTION){
                this.getMainController().getGameController().resumeGame();
            }
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
