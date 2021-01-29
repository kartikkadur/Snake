package de.unikl.seda.snake.gui.snake.engine;

import de.unikl.seda.snake.gui.controller.MainController;
import de.unikl.seda.snake.gui.snake.Main;

public class GameController {
    private boolean gameOver;
    private boolean running;
    private boolean isPaused;
    private Animator animator;
    private Thread animatorThread;

    private final MainController mainController;

    public GameController(MainController mainController) {
        this.mainController = mainController;
    }

    public boolean isGameOver() {
        return this.gameOver;
    }

    public void setGameOver(boolean request) {
        this.gameOver = request;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void setRunning(boolean request) {
        this.running = request;
    }

    public boolean isPaused() {
        return this.isPaused;
    }

    private void setPaused(boolean request) {
        this.isPaused = request;
        if(this.isPaused){
            this.animator.suspend();
        }
        else{
            this.animator.resume();
        }
    }

    public void startGame(AnimationLoop animationLoop) {
        if (this.animatorThread == null || !this.isRunning()) {
            this.animator = new Animator(this.mainController);
            this.animatorThread = new Thread(this.getAnimator());
            this.animatorThread.start();
        }
    }

    public Animator getAnimator() {
        return this.animator;
    }

    public void resumeGame() {
        this.setPaused(false);
    }

    public void pauseGame() {
        this.setPaused(true);
    }

    public void stopGame() {
        this.setRunning(false);
    }
}

