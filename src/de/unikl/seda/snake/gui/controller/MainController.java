package de.unikl.seda.snake.gui.controller;

import com.sun.tools.javac.Main;
import de.unikl.seda.snake.gui.tools.SnakeFrame;
import de.unikl.seda.snake.gui.tools.audio.Audio;
import de.unikl.seda.snake.gui.tools.cache.image.ImageCache;
import de.unikl.seda.snake.gui.tools.cache.image.ImageCacheCreation;
import de.unikl.seda.snake.gui.tools.engine.GameController;
import de.unikl.seda.snake.gui.tools.engine.JFrameListener;
import de.unikl.seda.snake.gui.tools.constants.SnakeUIConstants;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainController {
    // number of rows squares.
    private static int rows = 50;
    // number of column squares
    private static int columns = 50;
    // game fps
    private static int fps = 10;
    // level variable. currently there are 3 levels.
    private static int level = 1;
    // player name
    private static String name = "player";
    // class for basic game controls. Defines methods that sets
    // the game states.
    private final GameController gameController;
    // direction
    private Direction direction;
    // image cache
    private static ImageCache imageCache = new ImageCacheCreation();
    // audio class
    private static Audio audio = new Audio();
    // class for handling keyboard inputs. Defines methods that handles
    // key presses.
    private final KeyController keyController;
    private final SnakeAnimationLoop snakeAnimationLoop;
    private final SnakeFrame snakeFrame;

    public MainController(int level, int fps, int rows, int cols, String name) {
        this.name = name;
        this.level = level;
        this.rows = rows;
        this.columns = cols;
        this.fps = fps;
        this.direction = new Direction();
        // snake frame
        this.snakeFrame = new SnakeFrame(this, this.rows, this.columns, SnakeUIConstants.CELL_SIZE);
        this.snakeFrame.setTitle("Player : " + this.name);
        this.snakeAnimationLoop = new SnakeAnimationLoop(this,
                                                                      this.direction,
                                                                      this.imageCache,
                                                                      this.audio);
        this.keyController = new KeyController(this, this.direction);
        this.gameController = new GameController();
        this.getSnakeFrame().addWindowListener(new JFrameListener(this.getGameController()));
    }

    public int getRows() {
        return this.rows;
    }

    public int getColumns(){
        return this.columns;
    }

    public int getFps() {
        return this.fps;
    }

    public int getLevel() {
        return this.level;
    }

    public GameController getGameController() {
        return this.gameController;
    }

    public KeyController getKeyController() {
        return this.keyController;
    }

    public SnakeAnimationLoop getSnakeAnimationLoop() {
        return this.snakeAnimationLoop;
    }

    public SnakeFrame getSnakeFrame() {
        return this.snakeFrame;
    }

    public static void MainMenu(){
        JFrame f = new JFrame("Snake Game");
        f.setSize(400, 400);
        JLabel title = new JLabel();
        title.setText("Snake Game");
        title.setBounds(50, 50, 300, 30);
        f.add(title);
        JButton button1 = new JButton();
        button1.setText("Level 1");
        button1.setBounds(50, 150, 100, 30);
        f.add(button1);
        JButton button2 = new JButton();
        button2.setText("Level 2");
        button2.setBounds(160, 150, 100, 30);
        f.add(button2);
        JButton button3 = new JButton();
        button3.setText("Level 3");
        button3.setBounds(270, 150, 100, 30);
        f.add(button3);
        JLabel setting = new JLabel();
        setting.setText("Settings");
        setting.setBounds(50, 200, 300, 30);
        f.add(setting);
        final JTextField name = new JTextField();
        name.setBounds(50, 100, 300, 30);
        name.setText("Player");
        f.add(name);
        JLabel fps = new JLabel();
        fps.setText("Game Speed (FPS)");
        fps.setBounds(50, 250, 300, 30);
        f.add(fps);
        final JTextField speed = new JTextField();
        speed.setBounds(160, 250, 50, 30);
        speed.setText("10");
        f.add(speed);
        JLabel panelSize = new JLabel();
        panelSize.setText("Squre Size");
        panelSize.setBounds(50, 300, 300, 30);
        f.add(panelSize);
        final JTextField size = new JTextField();
        size.setBounds(160, 300, 50, 30);
        size.setText("10");
        f.add(size);
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int fps = Integer.parseInt(speed.getText().toString());
                int s = Integer.parseInt(size.getText().toString());
                String player = name.getText().toString();
                MainController main = new MainController(1, fps, 24, 32, player);
                main.getGameController().startGame(main.getSnakeAnimationLoop(), fps);
                main.getGameController().getAnimator().getStatisticsManager().addStatisticsListener(main.getSnakeFrame().getStatusPanel());
            }
        });
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int fps = Integer.parseInt(speed.getText().toString());
                int s = Integer.parseInt(size.getText().toString());
                String player = name.getText().toString();
                MainController main = new MainController(2, fps, 25, 25, player);
                main.getGameController().startGame(main.getSnakeAnimationLoop(), fps);
                main.getGameController().getAnimator().getStatisticsManager().addStatisticsListener(main.getSnakeFrame().getStatusPanel());
            }
        });
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int fps = Integer.parseInt(speed.getText().toString());
                int s = Integer.parseInt(size.getText().toString());
                String player = name.getText().toString();
                MainController main = new MainController(3, fps, 25, 25, player);
                main.getGameController().startGame(main.getSnakeAnimationLoop(), fps);
                main.getGameController().getAnimator().getStatisticsManager().addStatisticsListener(main.getSnakeFrame().getStatusPanel());
            }
        });
        f.setLayout(null);
        f.setVisible(true);
    }

    public static void main(String[] str) {
        MainMenu();
    }
}

