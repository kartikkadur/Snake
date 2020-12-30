package de.unikl.seda.snake.gui.tools.engine;

public class GameController {
    private boolean gameOver;
    private boolean running;
    private boolean isPaused;
    private Animator animator;
    private Thread animatorThread;

    public GameController() {
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

    public void startGame(AnimationLoop animationLoop, int FPS) {
        if (this.animatorThread == null || !this.isRunning()) {
            this.animator = new Animator(this, animationLoop, FPS);
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
        System.exit(0);
    }
}

