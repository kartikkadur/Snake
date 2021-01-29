package de.unikl.seda.snake.gui.tools.leveleditor;

import de.unikl.seda.snake.gui.controller.MainController;
import de.unikl.seda.snake.gui.snake.Snake;
import de.unikl.seda.snake.gui.snake.constants.SnakeUIConstants;
import de.unikl.seda.snake.gui.snake.variable.SnakeUIVariables;
import de.unikl.seda.snake.gui.tools.ObstacleType;
import de.unikl.seda.snake.gui.tools.SnakePanel;
import de.unikl.seda.snake.gui.tools.backgroundType.Backgrounds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;

public class LevelEditor implements ActionListener, Serializable{
    private ArrayList<Point> obstacleLocation;
    private static MainController mainController;

    public LevelEditor(MainController mainController){
        LevelEditor.mainController = mainController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Container container = mainController.getContentPane();
        container.removeAll();
        mainController.setLayout(new BorderLayout());

        JPanel editPanel = new JPanel();
        editPanel.setOpaque(false);

        LevelEditor.this.obstacleLocation = new ArrayList<>();
        JPanel editBlock = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int Y = 0; Y < mainController.getSnakeGameSettings().getCols() * SnakeUIConstants.CELL_SIZE; ++Y) {
                    for (int X = 0; X < mainController.getSnakeGameSettings().getRows() * SnakeUIConstants.CELL_SIZE; ++X) {
                        int x = X * SnakeUIConstants.CELL_SIZE;
                        int y = Y * SnakeUIConstants.CELL_SIZE;
                        g.drawRect(x, y, SnakeUIConstants.CELL_SIZE, SnakeUIConstants.CELL_SIZE);
                        g.setColor(Color.BLACK);
                    }
                }
            }
        };
        editBlock.setOpaque(false);
        editBlock.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Graphics g = editBlock.getGraphics();
                int xCoordinate = e.getX();
                int yCoordinate = e.getY();
                for (int Y = 0; Y < mainController.getSnakeGameSettings().getCols() * SnakeUIConstants.CELL_SIZE; ++Y) {
                    for (int X = 0; X < mainController.getSnakeGameSettings().getRows() * SnakeUIConstants.CELL_SIZE; ++X) {
                        int x = X * SnakeUIConstants.CELL_SIZE;
                        int y = Y * SnakeUIConstants.CELL_SIZE;
                        if (xCoordinate > x && yCoordinate > y && xCoordinate < (x + SnakeUIConstants.CELL_SIZE) && yCoordinate < (y + SnakeUIConstants.CELL_SIZE)) {
                            g.drawImage(LevelEditor.mainController.getImageCache().getObstacleImage(ObstacleType.HORIZONTAL_OBSTACLE), x, y, SnakeUIConstants.CELL_SIZE, SnakeUIConstants.CELL_SIZE, null);
                            LevelEditor.this.obstacleLocation.add(new Point(X, Y));
                        }
                    }
                }
            }
            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
        editBlock.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Graphics g = editBlock.getGraphics();
                int xCoordinate = e.getX();
                int yCoordinate = e.getY();
                for (int Y = 0; Y < mainController.getSnakeGameSettings().getCols() * SnakeUIConstants.CELL_SIZE; ++Y) {
                    for (int X = 0; X < mainController.getSnakeGameSettings().getRows() * SnakeUIConstants.CELL_SIZE; ++X) {
                        int x = X * SnakeUIConstants.CELL_SIZE;
                        int y = Y * SnakeUIConstants.CELL_SIZE;
                        if (xCoordinate > x && yCoordinate > y && xCoordinate < (x + SnakeUIConstants.CELL_SIZE)
                                && yCoordinate < (y + SnakeUIConstants.CELL_SIZE)
                                && obstacleLocation.contains(new Point(X,Y))) {
                            g.clearRect(x,y,SnakeUIConstants.CELL_SIZE, SnakeUIConstants.CELL_SIZE);
                            g.setColor(Color.BLACK);
                            g.drawRect(x,y,SnakeUIConstants.CELL_SIZE, SnakeUIConstants.CELL_SIZE);
                            g.fillRect(x,y,SnakeUIConstants.CELL_SIZE, SnakeUIConstants.CELL_SIZE);
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) { }

            @Override
            public void mouseReleased(MouseEvent e) { }

            @Override
            public void mouseEntered(MouseEvent e) { }

            @Override
            public void mouseExited(MouseEvent e) { }
        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(Color.BLACK);
        buttonsPanel.setLayout(new GridBagLayout());
        buttonsPanel.setOpaque(false);
        // Generate button
        JButton generateLevelButton = new JButton();
        generateLevelButton.setText("Generate Level");
        generateLevelButton.setOpaque(false);
        generateLevelButton.setContentAreaFilled(false);
        generateLevelButton.setBorderPainted(false);
        generateLevelButton.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 20));
        generateLevelButton.setForeground(SnakeUIConstants.menuFontColor);
        generateLevelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] options = {"Yes", "No"};
                int optionPane = JOptionPane.showOptionDialog(LevelEditor.mainController, "Do you want to save your changes ?","Level Edit",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                if (optionPane == 0) {
                    LevelEditor.updateEditLevel();
                    LevelEditor.mainController.displayLevelEditorPanel();
                    }
            }
        });

        JButton clearAllButton = new JButton();
        clearAllButton.setText("Clear All");
        clearAllButton.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 20));
        clearAllButton.setForeground(SnakeUIConstants.menuFontColor);
        clearAllButton.setOpaque(false);
        clearAllButton.setContentAreaFilled(false);
        clearAllButton.setBorderPainted(false);
        clearAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editBlock.repaint();
                LevelEditor.this.obstacleLocation.clear();
            }
        });

        // back button
        JButton backButton = new JButton();
        backButton.setText("Exit");
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 20));
        backButton.setForeground(SnakeUIConstants.menuFontColor);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LevelEditor.mainController.displayLevelEditorPanel();
            }
        });

        buttonsPanel.add(generateLevelButton);
        buttonsPanel.add(clearAllButton);
        buttonsPanel.add(backButton);

        container.add(editPanel, BorderLayout.CENTER);
        container.add(editBlock, BorderLayout.CENTER);
        container.add(buttonsPanel, BorderLayout.PAGE_END);

        container.revalidate();
        container.repaint();
    }

    public ArrayList<Point> getObstacleLocation() {
            return this.obstacleLocation;
        }

    public void setObstacleLocation(ArrayList<Point> obstacleLocations){
            this.obstacleLocation = obstacleLocations;
        }

    public static ArrayList<LevelEditor> updateEditLevel(){
        ArrayList<LevelEditor> levelEditor = LevelEditor.readLevelEdit();
        if (levelEditor == null) {
            levelEditor = new ArrayList<>();
        }
        levelEditor.add(LevelEditor.mainController.getLevelEditor());
        levelEditor = LevelEditor.writeLevelEdit(levelEditor);
        return levelEditor;
    }

    public static File getSerialPath(){
        String levelEditFilePath = System.getenv("APPDATA");
        if (levelEditFilePath == null) {
            levelEditFilePath = System.getProperty("user.home");
        }
        File CONFIG_HOME = new File(levelEditFilePath, ".snakegame").getAbsoluteFile();
        CONFIG_HOME.mkdirs();
        return CONFIG_HOME;
    }

    public static ArrayList<LevelEditor> writeLevelEdit(ArrayList<LevelEditor> levelEdit){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(
                    Paths.get(LevelEditor.getSerialPath().toString(), "levelEditFile.ser").toString());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(levelEdit);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception exp){
            exp.printStackTrace();
        }
        return levelEdit;
    }

    public static ArrayList<LevelEditor> readLevelEdit(){
        ArrayList<LevelEditor> levelEditors;
        try {
            FileInputStream fileInputStream = new FileInputStream(
                    Paths.get(LevelEditor.getSerialPath().toString(), "levelEditFile.ser").toString());
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            levelEditors = (ArrayList<LevelEditor>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception exp){
            levelEditors = null;
        }
        return levelEditors;
    }
}