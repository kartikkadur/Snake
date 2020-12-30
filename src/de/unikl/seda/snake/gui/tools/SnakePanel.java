package de.unikl.seda.snake.gui.tools;

import de.unikl.seda.snake.gui.controller.MainController;
import de.unikl.seda.snake.gui.tools.constants.SnakeUIConstants;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class SnakePanel extends JPanel {
    private int PWidth;
    private static int PHeight;
    private final MainController mainController;
    private final Font font;
    private final FontMetrics metrics;
    private Graphics dbg;
    private Image dbImage;
    private final List<Drawable> drawables;

    public SnakePanel(MainController mainController, int rows, int columns, int size) {
        this(mainController, columns * size, rows * size);
    }

    public SnakePanel(MainController mainController, int PWidth, int PHeight) {
        this.dbImage = null;
        this.drawables = new LinkedList();
        this.mainController = mainController;
        this.setPWidth(PWidth);
        setPHeight(PHeight);
        this.setBackground(Color.white);
        this.setPreferredSize(new Dimension(this.getPWidth(), this.getPHeight()));
        this.setFocusable(true);
        this.requestFocus();
        this.readyForTermination();
        this.font = new Font("SansSerif", 1, 24);
        this.metrics = this.getFontMetrics(this.font);
    }

    public int getPWidth() {
        return this.PWidth;
    }

    public void setPWidth(int request) {
        this.PWidth = request;
    }

    public int getPHeight() {
        return PHeight;
    }

    public static void setPHeight(int request) {
        PHeight = request;
    }

    public MainController getMainController() {
        return this.mainController;
    }

    public void gameRender() {
        if (this.dbImage == null) {
            this.dbImage = this.createImage(this.getPWidth(), this.getPHeight());
            if (this.dbImage == null) {
                System.out.println("dbImage is null");
                return;
            }

            this.dbg = this.dbImage.getGraphics();
        }

        this.dbg.setColor(Color.black);
        /*
        Image image = null;
        try {
            image = ImageIO.read(new File("/Users/kartikkadur/Documents/Kartik/FSEProject/Task 5/SnakeCoregame/src/de/unikl/seda/snake/gui/tools/cache/image/img/background/background.png"));
        } catch(IOException exp){
            exp.printStackTrace();
        }
        for (int i=0; i<this.getPWidth()/SnakeUIConstants.CELL_SIZE; i++){
            for(int j=0; j<this.getPHeight()/SnakeUIConstants.CELL_SIZE; j++){
                this.dbg.drawImage(image, i* SnakeUIConstants.CELL_SIZE, j * SnakeUIConstants.CELL_SIZE, SnakeUIConstants.CELL_SIZE, SnakeUIConstants.CELL_SIZE, null);
            }
        }
        */
        this.dbg.fillRect(0, 0, this.getPWidth(), this.getPHeight());
        this.dbg.setColor(Color.black);

        for (Drawable d : this.drawables) {
            d.draw(this.dbg);
        }

        if (this.getMainController().getGameController().isGameOver()) {
            this.gameOverMessage(this.dbg, this.getMainController().getSnakeAnimationLoop().getScore());
        }
    }

    private void gameOverMessage(Graphics g, int score) {
        String msg = "Game Over. Your Score: " + score;
        int x = (this.getPWidth() - this.metrics.stringWidth(msg)) / 2;
        int y = (this.getPHeight() - this.metrics.getHeight()) / 2;
        g.setColor(SnakeUIConstants.DEFAULT_FONT_COLOR);
        g.setFont(this.font);
        g.drawString(msg, x, y);
    }

    public void paintScreen() {
        try {
            Graphics g = this.getGraphics();
            if (g != null && this.dbImage != null) {
                g.drawImage(this.dbImage, 0, 0, null);
            }
            g.dispose();
        } catch (Exception var3) {
            System.out.println("Graphics context error: " + var3);
        }
    }

    private void readyForTermination() {
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                SnakePanel.this.getMainController().getKeyController().keyPressed(e);
            }
        });
    }

    public int getNumberOfDrawables() {
        return this.drawables.size();
    }

    public void addDrawable(Drawable drawable) {
        this.drawables.add(drawable);
    }

    public void addDrawableAt(int index, Drawable drawable) throws IndexOutOfBoundsException {
        this.drawables.add(index, drawable);
    }

    public void removeDrawable(Drawable drawable) {
        this.drawables.remove(drawable);
    }

    public List<Drawable> getAllDrawables() {
        return Collections.unmodifiableList(this.drawables);
    }
}

