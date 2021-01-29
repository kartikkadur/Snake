package de.unikl.seda.snake.gui.snake.engine;

public interface GameListener {
    void startGame();

    void resumeGame();

    void pauseGame();

    void stopGame();
}