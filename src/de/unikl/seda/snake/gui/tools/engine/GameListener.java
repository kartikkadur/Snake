package de.unikl.seda.snake.gui.tools.engine;

public interface GameListener {
    void startGame();

    void resumeGame();

    void pauseGame();

    void stopGame();
}