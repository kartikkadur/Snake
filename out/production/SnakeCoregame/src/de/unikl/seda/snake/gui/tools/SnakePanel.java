package de.unikl.seda.snake.gui.tools;

import de.unikl.seda.snake.gui.controller.MainController;
import de.unikl.seda.snake.gui.snake.constants.SnakeUIConstants;
import de.unikl.seda.snake.gui.snake.gamestats.GameStatistics;
import de.unikl.seda.snake.gui.snake.variable.SnakeUIVariables;
import de.unikl.seda.snake.gui.tools.Icon.Icon;
import de.unikl.seda.snake.gui.tools.backgroundType.Backgrounds;
import org.w3c.dom.Text;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

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
        this.setPHeight(PHeight);
        this.setOpaque(false);
        this.setPreferredSize(new Dimension(this.getPWidth(), this.getPHeight()));
        this.setFocusable(true);

        this.font = new Font(SnakeUIConstants.font, 1, 24);
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

        this.dbg.fillRect(0, 0, this.getPWidth(), this.getPHeight());
        this.dbg.drawImage(this.getMainController().getImageCache().getBackgroundImage(Backgrounds.GAME_BACKGROUND),
                0,
                0,
                this.getWidth(),
                this.getHeight(),
                null);

        for (Drawable d : this.drawables) {
            d.draw(this.dbg);
        }

        if (this.getMainController().getGameController().isGameOver()){
            this.gameOverOptions();
        }
    }

    public void gameOverOptions(){
        this.removeAll();
        this.setLayout(new BorderLayout());

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new GridLayout(3,0));
        messagePanel.setOpaque(false);

        JLabel gameOverLabel = new JLabel("Game Over");
        gameOverLabel.setOpaque(false);
        gameOverLabel.setForeground(Color.RED);
        gameOverLabel.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 40));
        gameOverLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setOpaque(false);

        JButton replayButton = new JButton("One More Go?");
        replayButton.setOpaque(false);
        replayButton.setContentAreaFilled(false);
        replayButton.setBorderPainted(false);
        replayButton.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 20));
        replayButton.setForeground(SnakeUIConstants.menuFontColor);
        replayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SnakePanel.this.getMainController().startGameLoop();
            }
        });

        JButton mainMenuButton = new JButton("Back to Main menu");
        mainMenuButton.setOpaque(false);
        mainMenuButton.setContentAreaFilled(false);
        mainMenuButton.setBorderPainted(false);
        mainMenuButton.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 20));
        mainMenuButton.setForeground(SnakeUIConstants.menuFontColor);
        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SnakePanel.this.getMainController().makeGUI();
            }
        });

        JButton highScoreButton = new JButton("High Scores");
        highScoreButton.setOpaque(false);
        highScoreButton.setContentAreaFilled(false);
        highScoreButton.setBorderPainted(false);
        highScoreButton.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 20));
        highScoreButton.setForeground(SnakeUIConstants.menuFontColor);
        highScoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SnakePanel.this.removeAll();
                SnakePanel.this.setLayout(new BorderLayout());
                JPanel titlePanel = new JPanel();
                titlePanel.setLayout(new GridBagLayout());
                titlePanel.setOpaque(false);

                JLabel titleLabel = new JLabel();
                titleLabel.setText("High Scores");
                titleLabel.setForeground(SnakeUIConstants.menuFontColor);
                titleLabel.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 25));

                JPanel statPanel = new JPanel();
                statPanel.setLayout(new BorderLayout());
                statPanel.setOpaque(false);

                JTable statTable = SnakePanel.this.getMainController().getStatTable();

                JTableHeader statTableHeader = statTable.getTableHeader();
                statTableHeader.setOpaque(false);
                ((DefaultTableCellRenderer)statTableHeader.getDefaultRenderer()).setOpaque(false);
                statTableHeader.setForeground(SnakeUIConstants.menuFontColor);
                statTableHeader.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 15));

                JPanel backPanel = new JPanel();
                backPanel.setLayout(new GridBagLayout());
                backPanel.setOpaque(false);

                JButton backButton = new JButton();
                backButton.setText("Back");
                backButton.setForeground(SnakeUIConstants.menuFontColor);
                backButton.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 20));
                backButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SnakePanel.this.gameOverOptions();
                    }
                });
                backButton.setOpaque(false);
                backButton.setContentAreaFilled(false);
                backButton.setBorderPainted(false);

                titlePanel.add(titleLabel);
                statPanel.add(statTableHeader, BorderLayout.NORTH);
                statPanel.add(statTable, BorderLayout.CENTER);
                backPanel.add(backButton);

                SnakePanel.this.add(titlePanel, BorderLayout.NORTH);
                SnakePanel.this.add(statPanel, BorderLayout.CENTER);
                SnakePanel.this.add(backPanel, BorderLayout.SOUTH);

                SnakePanel.this.revalidate();
                SnakePanel.this.repaint();
            }
        });

        buttonPanel.add(replayButton);
        buttonPanel.add(highScoreButton);
        buttonPanel.add(mainMenuButton);

        messagePanel.add(gameOverLabel);
        messagePanel.add(buttonPanel);

        this.add(messagePanel, BorderLayout.CENTER);

        this.revalidate();
        this.repaint();
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

    public void addDrawable(Drawable drawable) {
        this.drawables.add(drawable);
    }
}

