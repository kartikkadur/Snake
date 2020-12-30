package de.unikl.seda.snake.gui.tools.engine;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class JFrameListener implements WindowListener {
    private GameController gameController;

    public JFrameListener(GameController gameController) {
        this.gameController = gameController;
    }

    public GameController getGameController() {
        return this.gameController;
    }

    public void windowActivated(WindowEvent e) {
        this.getGameController().resumeGame();
    }

    public void windowDeactivated(WindowEvent e) {
        this.getGameController().pauseGame();
    }

    public void windowDeiconified(WindowEvent e) {
        this.getGameController().resumeGame();
    }

    public void windowIconified(WindowEvent e) {
        this.getGameController().pauseGame();
    }

    public void windowClosing(WindowEvent e) {
        this.getGameController().stopGame();
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }
}

