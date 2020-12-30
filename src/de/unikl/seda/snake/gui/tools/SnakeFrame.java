package de.unikl.seda.snake.gui.tools;

import de.unikl.seda.snake.gui.controller.MainController;
import java.awt.Container;
import javax.swing.JFrame;

public class SnakeFrame extends JFrame {
    private final SnakePanel snakePanel;
    private final StatusPanel statusPanel;

    public SnakeFrame(MainController mainController, int rows, int columns, int factor) {
        super("Snake");
        this.snakePanel = new SnakePanel(mainController, rows, columns, factor);
        this.statusPanel = new StatusPanel();
        this.makeGUI();
        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    }

    private void makeGUI() {
        Container c = this.getContentPane();
        c.add(this.snakePanel, "Center");
        c.add(this.statusPanel, "South");
    }

    public SnakePanel getSnakePanel() {
        return this.snakePanel;
    }

    public StatusPanel getStatusPanel() {
        return this.statusPanel;
    }
}
