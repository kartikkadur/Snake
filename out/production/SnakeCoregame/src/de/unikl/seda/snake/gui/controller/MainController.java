package de.unikl.seda.snake.gui.controller;

import de.unikl.seda.snake.gui.snake.constants.SnakeUIConstants;
import de.unikl.seda.snake.gui.snake.engine.GameController;
import de.unikl.seda.snake.gui.snake.engine.JFrameListener;
import de.unikl.seda.snake.gui.snake.gamestats.GameStatistics;
import de.unikl.seda.snake.gui.snake.variable.SnakeUIVariables;
import de.unikl.seda.snake.gui.tools.SnakePanel;
import de.unikl.seda.snake.gui.tools.StatusPanel;
import de.unikl.seda.snake.gui.tools.audio.Audio;
import de.unikl.seda.snake.gui.tools.audio.AudioType;
import de.unikl.seda.snake.gui.tools.audio.VolumeLevel;
import de.unikl.seda.snake.gui.tools.backgroundType.Backgrounds;
import de.unikl.seda.snake.gui.tools.cache.image.ImageCache;
import de.unikl.seda.snake.gui.tools.cache.image.ImageCacheCreation;
import de.unikl.seda.snake.gui.tools.leveleditor.LevelEditor;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

public class MainController extends JFrame{

    // class for basic game controls. Defines methods that sets
    // the game states.
    private GameController gameController;
    // direction
    private Direction direction;
    // image cache
    private ImageCache imageCache;
    // audio class
    private  Audio audio;
    // game stats class
    private GameStatistics gameStats;
    // class for handling keyboard inputs. Defines methods that handles
    // key presses.
    private KeyController keyController;
    private SnakeAnimationLoop snakeAnimationLoop;

    // Level Editor
    private LevelEditor levelEditor;

    //Game settings
    SnakeUIVariables snakeGameSettings;

    // UI components
    private static JRadioButton optionSmall;
    private static JRadioButton optionMedium;
    private static JRadioButton optionLarge;
    private static JRadioButton optionHuge;
    private static JSlider gameSpeedSlider;
    private static JSlider gameVolumeSlider;
    private static JCheckBox muteCheckBox;

    // panels
    private SnakePanel snakePanel;
    private StatusPanel statusPanel;

    public MainController() {
        // Game Settings
        this.snakeGameSettings = SnakeUIVariables.readLevelSettings();
        if(this.snakeGameSettings == null){
            this.snakeGameSettings = new SnakeUIVariables();
        }
        //Game audio
        this.audio = new Audio();
        // Image cache
        this.imageCache = new ImageCacheCreation();
        // object handling snake directions
        this.direction = new Direction();
        // Game stats
        this.gameStats = new GameStatistics();
        // Level Editor
        // add default levels to serialized file
        this.generateDefaultLevels();
        this.levelEditor = new LevelEditor(this);
        // Main menu GUI
        this.makeGUI();
    }

    class BackgroundPanel extends JPanel
    {
        @Override
        protected void paintComponent(Graphics g)
        {
            g.drawImage(MainController.this.getImageCache().getBackgroundImage(Backgrounds.MAIN_MENU_BACKGROUND),
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    this);
        }
    }

    public class InGameBackground extends JPanel
    {
        @Override
        protected void paintComponent(Graphics g)
        {
            g.drawImage(MainController.this.getImageCache().getBackgroundImage(Backgrounds.GAME_BACKGROUND),
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    this);
        }
    }


    public int getLevel() {
        return this.snakeGameSettings.getLevel();
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

    public GameStatistics getGameStats(){
        return this.gameStats;
    }

    public Audio getAudio(){
        return this.audio;
    }

    public ImageCache getImageCache(){
        return this.imageCache;
    }

    public SnakePanel getSnakePanel(){
        return this.snakePanel;
    }

    public StatusPanel getStatusPanel(){
        return this.statusPanel;
    }

    public Direction getDirection(){
        return this.direction;
    }

    public LevelEditor getLevelEditor(){
        return this.levelEditor;
    }

    public SnakeUIVariables getSnakeGameSettings() {
        return this.snakeGameSettings;
    }

    public void makeGUI() {
        // snake frame
        this.setTitle("Snake World");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.BLACK);
        this.handleScreenSize();

        this.setLayout(new BorderLayout());
        this.displayMainMenu();
        this.setVisible(true);
    }

    public void handleScreenSize(){
        this.setSize(this.snakeGameSettings.getCols()*SnakeUIConstants.CELL_SIZE,
                this.snakeGameSettings.getRows()*SnakeUIConstants.CELL_SIZE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2,
                dim.height / 2 - this.getSize().height / 2);
    }

    public void generateDefaultLevels(){
        if(LevelEditor.readLevelEdit() == null) {
            ArrayList<LevelEditor> defaultLevels = new ArrayList<>();
            ArrayList<Point> obstacleLocations = new ArrayList<>();

            LevelEditor secondLevel = new LevelEditor(null);
            LevelEditor thirdLevel = new LevelEditor(null);
            LevelEditor firstLevel = new LevelEditor(null);

            firstLevel.setObstacleLocation(obstacleLocations);
            defaultLevels.add(firstLevel);

            obstacleLocations = new ArrayList<>();
            for (int j = 0; j <= this.snakeGameSettings.getRows() * SnakeUIConstants.CELL_SIZE; ++j) {
                obstacleLocations.add(new Point(0, j));
                obstacleLocations.add(new Point((this.snakeGameSettings.getCols() - 1), j));
            }
            for (int k = 0; k <= this.snakeGameSettings.getCols() * SnakeUIConstants.CELL_SIZE; ++k) {
                obstacleLocations.add(new Point(k, 0));
                obstacleLocations.add(new Point(k, (this.snakeGameSettings.getRows() - 1)));
            }
            secondLevel.setObstacleLocation(obstacleLocations);
            defaultLevels.add(secondLevel);

            obstacleLocations = new ArrayList<>();
            int x1 = (int) (0.25 * this.snakeGameSettings.getRows());
            int x2 = (int) (0.75 * this.snakeGameSettings.getCols() - 1);
            for (int j = (int) (0.25 * this.snakeGameSettings.getRows()); j <= (int) (0.75 * this.snakeGameSettings.getRows()); ++j) {
                obstacleLocations.add(new Point(x1, j));
                obstacleLocations.add(new Point(x2, j));
            }
            thirdLevel.setObstacleLocation(obstacleLocations);
            defaultLevels.add(thirdLevel);

            LevelEditor.writeLevelEdit(defaultLevels);
        }
    }

    public void displayMainMenu() {
        if(! this.getSnakeGameSettings().getMuted()) {
            this.audio.play(AudioType.MAIN_MENU, true);
        }
        this.setTitle("Main Menu");
        Container container = this.getContentPane();
        container.removeAll();

        BackgroundPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new GridBagLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel();
        titleLabel.setText("Snake World");
        titleLabel.setForeground(SnakeUIConstants.menuFontColor);
        titleLabel.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 40));

        JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new GridLayout(8,0));

        JButton startButton = new JButton("Start");
        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setForeground(SnakeUIConstants.menuFontColor);
        startButton.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 25));
        startButton.addActionListener(new startButtonHandler());

        JButton scoreButton = new JButton("High Scores");
        scoreButton.setOpaque(false);
        scoreButton.setContentAreaFilled(false);
        scoreButton.setBorderPainted(false);
        scoreButton.setForeground(SnakeUIConstants.menuFontColor);
        scoreButton.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 25));
        scoreButton.addActionListener(new highScoreButtonHandler());

        JButton levelEditorButton = new JButton("Level Editor");
        levelEditorButton.setOpaque(false);
        levelEditorButton.setContentAreaFilled(false);
        levelEditorButton.setBorderPainted(false);
        levelEditorButton.setForeground(SnakeUIConstants.menuFontColor);
        levelEditorButton.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 25));
        levelEditorButton.addActionListener(new levelEditorButtonHandler());

        JButton settingsButton = new JButton("Settings");
        settingsButton.setOpaque(false);
        settingsButton.setContentAreaFilled(false);
        settingsButton.setBorderPainted(false);
        settingsButton.setForeground(SnakeUIConstants.menuFontColor);
        settingsButton.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 25));
        settingsButton.addActionListener(new settingsButtonHandler());

        JButton aboutButton = new JButton("About");
        aboutButton.setOpaque(false);
        aboutButton.setContentAreaFilled(false);
        aboutButton.setBorderPainted(false);
        aboutButton.setForeground(SnakeUIConstants.menuFontColor);
        aboutButton.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 25));
        aboutButton.addActionListener(new aboutButtonHandler());

        JButton exitButton = new JButton("Exit");
        exitButton.setOpaque(false);
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);
        exitButton.setForeground(SnakeUIConstants.menuFontColor);
        exitButton.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 25));
        exitButton.addActionListener(new exitButtonHandler());

        titlePanel.add(titleLabel);
        menuPanel.add(startButton);
        menuPanel.add(scoreButton);
        menuPanel.add(levelEditorButton);
        menuPanel.add(settingsButton);
        menuPanel.add(aboutButton);
        menuPanel.add(exitButton);

        mainPanel.add(titlePanel, BorderLayout.CENTER);
        mainPanel.add(menuPanel, BorderLayout.SOUTH);

        container.add(mainPanel);

        container.revalidate();
        container.repaint();
    }

    public void displaySettingsMenu() {
        this.setTitle("Settings");

        Container container = this.getContentPane();
        container.removeAll();

        BackgroundPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new GridBagLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel();
        titleLabel.setText("Settings");
        titleLabel.setForeground(SnakeUIConstants.menuFontColor);
        titleLabel.setOpaque(false);
        titleLabel.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 25));

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(5,1));
        menuPanel.setOpaque(false);

        JLabel gameSpeedLabel = new JLabel();
        gameSpeedLabel.setText("Game Speed (FPS):");
        gameSpeedLabel.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 15));
        gameSpeedLabel.setForeground(SnakeUIConstants.menuFontColor);
        gameSpeedLabel.setOpaque(false);

        MainController.gameSpeedSlider = new JSlider(JSlider.HORIZONTAL, 5, 25, 10);
        MainController.gameSpeedSlider.setForeground(SnakeUIConstants.menuFontColor);
        Hashtable labelTable = new Hashtable();
        labelTable.put(6, new JLabel("Slow"));
        labelTable.put(12, new JLabel("Mid"));
        labelTable.put(18, new JLabel("Fast"));
        labelTable.put(25, new JLabel(" VFast"));
        MainController.gameSpeedSlider.setLabelTable(labelTable);
        MainController.gameSpeedSlider.setPaintTicks(true);
        MainController.gameSpeedSlider.setMajorTickSpacing(6);
        MainController.gameSpeedSlider.setPaintLabels(true);
        MainController.gameSpeedSlider.setValue(this.snakeGameSettings.getFps());
        MainController.gameSpeedSlider.addChangeListener(new gameSpeedHandler());
        MainController.gameSpeedSlider.setOpaque(false);

        JLabel gameVolumeLabel = new JLabel();
        gameVolumeLabel.setText("Game Volume:");
        gameVolumeLabel.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 15));
        gameVolumeLabel.setForeground(SnakeUIConstants.menuFontColor);
        gameVolumeLabel.setOpaque(false);

        MainController.gameVolumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 30, 10);
        MainController.gameVolumeSlider.setForeground(SnakeUIConstants.menuFontColor);
        Hashtable volumeTable = new Hashtable();
        volumeTable.put(0, new JLabel("Mute"));
        volumeTable.put(10, new JLabel("Low"));
        volumeTable.put(20, new JLabel("Medium"));
        volumeTable.put(30, new JLabel(" High"));
        MainController.gameVolumeSlider.setLabelTable(volumeTable);
        MainController.gameVolumeSlider.setMajorTickSpacing(10);
        MainController.gameVolumeSlider.setPaintTicks(true);
        MainController.gameVolumeSlider.setPaintLabels(true);
        MainController.gameVolumeSlider.setValue(this.snakeGameSettings.getVolumeLevel());
        MainController.gameVolumeSlider.addChangeListener(new gameVolumeHandler());
        MainController.gameVolumeSlider.setOpaque(false);

        JLabel windowSizeLabel = new JLabel();
        windowSizeLabel.setText("Window Size:");
        windowSizeLabel.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 15));
        windowSizeLabel.setForeground(SnakeUIConstants.menuFontColor);
        windowSizeLabel.setOpaque(false);

        JPanel windowSizePanel = new JPanel();
        windowSizePanel.setOpaque(false);
        windowSizePanel.setLayout(new GridLayout(0,4));

        windowSizeHandler windowSizeListener = new windowSizeHandler();

        MainController.optionSmall = new JRadioButton("Small");
        MainController.optionSmall.setOpaque(false);
        MainController.optionSmall.setBorderPainted(false);
        MainController.optionSmall.setContentAreaFilled(false);
        MainController.optionSmall.addChangeListener(windowSizeListener);

        MainController.optionMedium = new JRadioButton("Medium");
        MainController.optionMedium.setOpaque(false);
        MainController.optionMedium.setBorderPainted(false);
        MainController.optionMedium.setContentAreaFilled(false);
        MainController.optionMedium.addChangeListener(windowSizeListener);

        MainController.optionLarge = new JRadioButton("Large");
        MainController.optionLarge.setOpaque(false);
        MainController.optionLarge.setBorderPainted(false);
        MainController.optionLarge.setContentAreaFilled(false);
        MainController.optionLarge.addChangeListener(windowSizeListener);

        MainController.optionHuge = new JRadioButton("Huge");
        MainController.optionHuge.setOpaque(false);
        MainController.optionHuge.setBorderPainted(false);
        MainController.optionHuge.setContentAreaFilled(false);
        MainController.optionHuge.addChangeListener(windowSizeListener);

        ButtonGroup windowSizeOptions = new ButtonGroup();
        windowSizeOptions.add(MainController.optionSmall);
        windowSizeOptions.add(MainController.optionMedium);
        windowSizeOptions.add(MainController.optionLarge);
        windowSizeOptions.add(MainController.optionHuge);

        JLabel muteLabel = new JLabel();
        muteLabel.setText("Mute:");
        muteLabel.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 15));
        muteLabel.setForeground(SnakeUIConstants.menuFontColor);
        muteLabel.setOpaque(false);

        MainController.muteCheckBox = new JCheckBox();
        MainController.muteCheckBox.setForeground(SnakeUIConstants.menuFontColor);
        MainController.muteCheckBox.addActionListener(new muteCheckBoxHandler());
        MainController.muteCheckBox.setSelected(this.snakeGameSettings.getMuted());
        MainController.muteCheckBox.setOpaque(false);

        JPanel backButtonPanel = new JPanel();
        backButtonPanel.setLayout(new GridBagLayout());
        backButtonPanel.setOpaque(false);

        JButton backButton = new JButton();
        backButton.setText("Back");
        backButton.setForeground(SnakeUIConstants.menuFontColor);
        backButton.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 18));
        backButton.addActionListener(new settingsBackButtonHandler());
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);

        titlePanel.add(titleLabel);

        windowSizePanel.add(MainController.optionSmall);
        windowSizePanel.add(MainController.optionMedium);
        windowSizePanel.add(MainController.optionLarge);
        windowSizePanel.add(MainController.optionHuge);

        menuPanel.add(gameSpeedLabel);
        menuPanel.add(MainController.gameSpeedSlider);

        menuPanel.add(gameVolumeLabel);
        menuPanel.add(MainController.gameVolumeSlider);

        menuPanel.add(windowSizeLabel);
        menuPanel.add(windowSizePanel);

        menuPanel.add(muteLabel);
        menuPanel.add(muteCheckBox);

        backButtonPanel.add(backButton);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(menuPanel, BorderLayout.CENTER);
        mainPanel.add(backButtonPanel, BorderLayout.SOUTH);

        container.add(mainPanel);

        container.revalidate();
        container.repaint();
    }

    public void displayAbout() {
        Container container = this.getContentPane();
        container.removeAll();
        this.setTitle("About");
        this.setLayout(new BorderLayout());

        BackgroundPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new GridBagLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel();
        titleLabel.setText("About");
        titleLabel.setForeground(SnakeUIConstants.menuFontColor);
        titleLabel.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 25));
        titleLabel.setOpaque(false);

        JPanel aboutPanel = new JPanel();
        aboutPanel.setLayout(new BorderLayout());
        aboutPanel.setOpaque(false);

        JTextArea aboutText = new JTextArea();
        aboutText.setText(SnakeUIConstants.ABOUT);
        aboutText.setForeground(SnakeUIConstants.menuFontColor);
        aboutText.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 20));
        aboutText.setOpaque(false);

        JPanel backPanel = new JPanel();
        backPanel.setLayout(new GridBagLayout());
        backPanel.setOpaque(false);

        JButton backButton = new JButton();
        backButton.setText("Back");
        backButton.setForeground(SnakeUIConstants.menuFontColor);
        backButton.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 20));
        backButton.addActionListener(new backButtonHandler());
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);

        titlePanel.add(titleLabel);
        aboutPanel.add(aboutText);
        backPanel.add(backButton);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(aboutPanel,BorderLayout.CENTER);
        mainPanel.add(backPanel, BorderLayout.SOUTH);

        container.add(mainPanel);

        container.revalidate();
        container.repaint();
    }

    public void levelSelection() {
        Container container = this.getContentPane();
        container.removeAll();
        this.setTitle("Level Selection");
        this.setLayout(new BorderLayout());

        BackgroundPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new GridBagLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel();
        titleLabel.setText("Level Selection");
        titleLabel.setForeground(SnakeUIConstants.menuFontColor);
        titleLabel.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 25));

        JPanel levelPanel = new JPanel();
        levelPanel.setOpaque(false);
        levelPanel.setLayout(new GridBagLayout());

        JScrollPane levelList = getLevelList();
        levelList.setForeground(SnakeUIConstants.menuFontColor);

        JPanel backPanel = new JPanel();
        backPanel.setOpaque(false);
        backPanel.setLayout(new GridBagLayout());

        JButton backButton = new JButton();
        backButton.setText("Back");
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setForeground(SnakeUIConstants.menuFontColor);
        backButton.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 20));
        backButton.addActionListener(new backButtonHandler());

        titlePanel.add(titleLabel);
        levelPanel.add(levelList);
        backPanel.add(backButton);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(levelPanel, BorderLayout.CENTER);
        mainPanel.add(backPanel, BorderLayout.SOUTH);

        container.add(mainPanel);

        container.revalidate();
        container.repaint();
    }

    public void displayHighScorePanel(){
        Container container = this.getContentPane();
        container.removeAll();
        this.setTitle("High Scores");
        this.setLayout(new BorderLayout());

        BackgroundPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setOpaque(false);

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

        JTable statTable = getStatTable();

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
        backButton.addActionListener(new backButtonHandler());
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);

        titlePanel.add(titleLabel);
        statPanel.add(statTableHeader, BorderLayout.NORTH);
        statPanel.add(statTable, BorderLayout.CENTER);
        backPanel.add(backButton);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(statPanel, BorderLayout.CENTER);
        mainPanel.add(backPanel, BorderLayout.SOUTH);

        container.add(mainPanel);

        container.revalidate();
        container.repaint();
    }

    public JTable getStatTable(){
        String col[] = {"Name", "Rank", "Points", "Date", "Speed", "Level"};

        DefaultTableModel tableModel = new DefaultTableModel(col, 0);
        JTable statTable = new JTable(tableModel);
        statTable.setForeground(SnakeUIConstants.menuFontColor);
        statTable.setOpaque(false);
        statTable.setShowGrid(false);
        statTable.setIntercellSpacing(new Dimension(0, 0));
        ((DefaultTableCellRenderer)statTable.getDefaultRenderer(Object.class)).setOpaque(false);
        ((DefaultTableCellRenderer)statTable.getDefaultRenderer(Object.class)).setHorizontalAlignment(JLabel.CENTER);
        statTable.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 15));
        try {
            ArrayList<GameStatistics> data = GameStatistics.readGameStats();
            for (int i = 0; i < data.size(); ++i) {
                String name = data.get(i).getPlayerName();
                int rank = data.get(i).getPlayerRank();
                int points = data.get(i).getTotalPoints();
                String date = data.get(i).getPlayDate();
                int speed = data.get(i).getSpeed();
                int level = data.get(i).getLevel();

                Object[] statData = {name, rank, points, date, speed, level};
                tableModel.addRow(statData);
            }
        } catch (Exception e) {
            String name = "null";
            int rank = -1;
            int points = -1;
            String date = "null";
            int speed = -1;
            int level = -1;

            Object[] statData = {name, rank, points, date, speed, level};
            tableModel.addRow(statData);
        }
        return statTable;
    }

    public void startLevelSelection() {
        this.audio.play(AudioType.CLICK, false);
        this.levelSelection();
    }

    public void displayLevelEditorPanel(){
        Container container = this.getContentPane();
        container.removeAll();

        this.setTitle("Level Editor");
        this.setLayout(new BorderLayout());

        BackgroundPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new GridBagLayout());

        JLabel titleLabel = new JLabel();
        titleLabel.setText("Level Editor");
        titleLabel.setForeground(SnakeUIConstants.menuFontColor);
        titleLabel.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 25));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BorderLayout());

        JButton modifyLevelButton = new JButton();
        modifyLevelButton.setText("Modify Level");
        modifyLevelButton.setForeground(SnakeUIConstants.menuFontColor);
        modifyLevelButton.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 20));
        modifyLevelButton.addActionListener(this.getLevelEditor());
        modifyLevelButton.setOpaque(false);
        modifyLevelButton.setContentAreaFilled(false);
        modifyLevelButton.setBorderPainted(false);

        JButton backButton = new JButton();
        backButton.setText("Back");
        backButton.setForeground(SnakeUIConstants.menuFontColor);
        backButton.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 20));
        backButton.addActionListener(new backButtonHandler());
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);

        titlePanel.add(titleLabel);
        buttonPanel.add(modifyLevelButton,BorderLayout.CENTER);
        buttonPanel.add(backButton, BorderLayout.SOUTH);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        container.add(mainPanel);

        container.revalidate();
        container.repaint();
    }

    public JScrollPane getLevelList(){
        DefaultListModel listModel = new DefaultListModel();
        JList list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setVisibleRowCount(-1);
        list.setForeground(SnakeUIConstants.menuFontColor);
        list.setOpaque(false);
        list.setBackground(new Color(0,0,0,0));
        DefaultListCellRenderer renderer =
                (DefaultListCellRenderer)list.getCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        list.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 20));
        list.setLayout(new BorderLayout());
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    // set level to the selected item in the list
                    MainController.this.getSnakeGameSettings().setLevel(e.getLastIndex());
                    // start the game
                    MainController.this.startGameLoop();
                }
            }
        });

        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension((int) (0.4 * this.getWidth()),
                (int) (0.4 * this.getHeight())));
        listScroller.setOpaque(false);
        listScroller.setBackground(new Color(0,0,0,0));
        listScroller.setBorder(BorderFactory.createEmptyBorder());
        listScroller.getViewport().setOpaque(false);

        for(int i = 0; i< LevelEditor.readLevelEdit().size(); i++){
            listModel.addElement("Level" + " " + (int)(i+1));
        }
        return listScroller;
    }

    public void startGameLoop(){
        // create objects
        // Animation Loop
        this.snakeAnimationLoop = new SnakeAnimationLoop(this);
        // Game Controller
        this.gameController = new GameController(this);
        // object handling keyboard events
        this.keyController = new KeyController(this);
        //Snake Panel
        this.snakePanel = new SnakePanel(this,
                this.getSnakeGameSettings().getRows(),
                this.getSnakeGameSettings().getCols(),
                SnakeUIConstants.CELL_SIZE);
        this.snakePanel.setOpaque(false);
        // create status panel
        this.statusPanel = new StatusPanel();
        this.statusPanel.setOpaque(false);

        this.setTitle("Snake World");
        this.audio.play(AudioType.CLICK, false);
        this.setSize(this.getSnakeGameSettings().getCols()*SnakeUIConstants.CELL_SIZE,
                this.getSnakeGameSettings().getRows()*SnakeUIConstants.CELL_SIZE);
        this.setLayout(new BorderLayout());

        Container container = this.getContentPane();
        container.removeAll();

        InGameBackground gamePanel = new InGameBackground();
        gamePanel.setLayout(new BorderLayout());
        gamePanel.setOpaque(false);

        // create snake add food images
        this.getSnakeAnimationLoop().createModels();

        // add panels to frame
        gamePanel.add(this.snakePanel, BorderLayout.CENTER);
        gamePanel.add(this.statusPanel, BorderLayout.NORTH);

        container.add(gamePanel);
        // Start the game
        this.getGameController().startGame(this.getSnakeAnimationLoop());
        this.getGameController().getAnimator().getStatisticsManager().addStatisticsListener(this.getStatusPanel());
        this.addWindowListener(new JFrameListener(this.getGameController()));
        // set the focus to snake panel
        this.snakePanel.requestFocus();
        this.snakePanel.addKeyListener(this.getKeyController());
        this.pack();

        this.snakePanel.revalidate();
        this.snakePanel.repaint();

        this.statusPanel.revalidate();
        this.statusPanel.repaint();

        container.revalidate();
        container.repaint();
    }

    public class startButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            getAudio().play(AudioType.CLICK, false);
             startLevelSelection();
        }
    }

    public class settingsButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            getAudio().play(AudioType.CLICK, false);
            displaySettingsMenu();
        }
    }

    public class aboutButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            getAudio().play(AudioType.CLICK, false);
            displayAbout();
        }
    }

    public class backButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            getAudio().play(AudioType.CLICK, false);
            displayMainMenu();
        }
    }

    public class settingsBackButtonHandler implements  ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            SnakeUIVariables.writeLevelSettings(MainController.this.getSnakeGameSettings());
            displayMainMenu();
        }
    }

    public class exitButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            getAudio().play(AudioType.CLICK, false);
            System.exit(0);
        }
    }

    public class gameSpeedHandler implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            MainController.this.getSnakeGameSettings().setFps(MainController.gameSpeedSlider.getValue());
        }
    }

    public class highScoreButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            getAudio().play(AudioType.CLICK, false);
            displayHighScorePanel();
        }
    }

    public class levelEditorButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MainController.this.getAudio().play(AudioType.CLICK, false);
            MainController.this.displayLevelEditorPanel();
        }
    }

    public class gameVolumeHandler implements ChangeListener{
        @Override
        public void stateChanged(ChangeEvent e) {
            MainController.this.getSnakeGameSettings().setVolumeLevel(MainController.gameVolumeSlider.getValue());
            int volumeLevel = MainController.this.getSnakeGameSettings().getVolumeLevel();
            if (!MainController.muteCheckBox.isSelected()) {
                if (volumeLevel == 0) {
                    getAudio().setVolume(AudioType.BACKGROUND, VolumeLevel.MUTE);
                    getAudio().setVolume(AudioType.MAIN_MENU, VolumeLevel.MUTE);
                } else if (volumeLevel > 0 && volumeLevel < 10) {
                    getAudio().setVolume(AudioType.BACKGROUND, VolumeLevel.LOW);
                    getAudio().setVolume(AudioType.MAIN_MENU, VolumeLevel.LOW);
                } else if (volumeLevel >= 10 && volumeLevel < 20) {
                    getAudio().setVolume(AudioType.BACKGROUND, VolumeLevel.MEDIUM);
                    getAudio().setVolume(AudioType.MAIN_MENU, VolumeLevel.MEDIUM);
                } else {
                    getAudio().setVolume(AudioType.BACKGROUND, VolumeLevel.HIGH);
                    getAudio().setVolume(AudioType.MAIN_MENU, VolumeLevel.HIGH);
                }
            }
        }
    }

   public class windowSizeHandler implements ChangeListener{

       @Override
       public void stateChanged(ChangeEvent e) {
           String windowSize = "Small";
           getAudio().play(AudioType.CLICK, false);
           if (MainController.optionSmall.isSelected()){
               MainController.this.getSnakeGameSettings().setRows(24);
               MainController.this.getSnakeGameSettings().setCols(32);
           } else if (MainController.optionMedium.isSelected()){
               MainController.this.getSnakeGameSettings().setRows(32);
               MainController.this.getSnakeGameSettings().setCols(40);
           } else if (MainController.optionLarge.isSelected()){
               MainController.this.getSnakeGameSettings().setRows(48);
               MainController.this.getSnakeGameSettings().setCols(54);
           } else if (MainController.optionHuge.isSelected()){
               MainController.this.getSnakeGameSettings().setRows(54);
               MainController.this.getSnakeGameSettings().setCols(96);
           }
           MainController.this.getSnakeGameSettings().setWindowSize(windowSize);
           MainController.this.handleScreenSize();
       }
   }

    public class muteCheckBoxHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            MainController.this.getSnakeGameSettings().setMuted(MainController.muteCheckBox.isSelected());
            getAudio().play(AudioType.CLICK, false);
            if (MainController.this.getSnakeGameSettings().getMuted()){
                getAudio().setVolume(AudioType.BACKGROUND, VolumeLevel.MUTE);
                getAudio().setVolume(AudioType.MAIN_MENU, VolumeLevel.MUTE);
            } else {
                getAudio().setVolume(AudioType.BACKGROUND, VolumeLevel.LOW);
                getAudio().setVolume(AudioType.MAIN_MENU, VolumeLevel.LOW);
            }
        }
    }
}