package de.unikl.seda.snake.gui.controller;

import de.unikl.seda.snake.gui.snake.Food;
import de.unikl.seda.snake.gui.snake.Snake;
import de.unikl.seda.snake.gui.snake.gamestats.GameStatistics;
import de.unikl.seda.snake.gui.snake.variable.SnakeUIVariables;
import de.unikl.seda.snake.gui.tools.SnakePanel;
import de.unikl.seda.snake.gui.tools.audio.Audio;
import de.unikl.seda.snake.gui.tools.audio.AudioType;
import de.unikl.seda.snake.gui.tools.cache.image.ImageCache;
import de.unikl.seda.snake.gui.snake.engine.AnimationLoop;
import de.unikl.seda.snake.gui.snake.constants.SnakeUIConstants;
import de.unikl.seda.snake.gui.tools.foodType.FoodType;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class SnakeAnimationLoop implements AnimationLoop {
    int level;
    private final MainController mainController;
    private Snake snake;
    private int score;
    private Food food;
    private final Direction direction;
    private final ImageCache imageCache;
    private final Audio audio;
    private int updateFreq;
    private ArrayList<GameStatistics> gameStatistics;

    public SnakeAnimationLoop(MainController mainController){
        this.mainController = mainController;
        this.level = this.mainController.getLevel();
        this.direction = this.mainController.getDirection();
        this.imageCache = this.mainController.getImageCache();
        this.audio = this.mainController.getAudio();
        this.snake = new Snake(this.getMainController());
        this.food = new Food(new Point(this.getMainController().getSnakeGameSettings().getRows() / 2,
                this.getMainController().getSnakeGameSettings().getCols() / 4),
                this.mainController);
        this.updateFreq = 0;
        this.score=0;
        //this.createModels();
    }

    public MainController getMainController() {
        return this.mainController;
    }

    public SnakePanel getSnakePanel() {
        return this.getMainController().getSnakePanel();
    }

    public void createModels(){
        this.audio.stop(AudioType.MAIN_MENU);
        this.getMainController().getSnakePanel().addDrawable(this.snake);
        this.getMainController().getSnakePanel().addDrawable(this.food);
    }

    public Snake getSnake() {
        return this.snake;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ArrayList<GameStatistics> getGameStatistics(){
        this.gameStatistics = GameStatistics.readGameStats();
        return this.gameStatistics;
    }

    private void addScore(int request) {
        this.score += request;
        this.getMainController().getStatusPanel().setScore(this.score);
    }

    public void gameUpdate() {
        // start playing background audio
        if (!this.mainController.getSnakeGameSettings().getMuted()) {
            this.audio.play(AudioType.BACKGROUND, true);
        }
        // if a particular score is reached increase snake speed
        if (this.getScore() > SnakeUIConstants.SPEED_CHANGE_LIMIT ){
            this.getMainController().getSnakeGameSettings().setFps(this.getMainController().getSnakeGameSettings().getFps() + SnakeUIConstants.FPS_INCREASE);
        }

        this.getSnake().move(this.direction.getCurrentDirection());
        // update the food animation only after FOOD_ANIMATION_FREQ snake steps
        if(this.updateFreq % SnakeUIConstants.FOOD_ANIMATION_FREQ == 0) {
            this.updateFreq = 0;
            this.food.setFoodType(FoodType.randomFoodType());
        }
        ++this.updateFreq;
        if (this.food.isHit(this.getSnake().getHead())) {
            // play food eat audio
            this.audio.play(AudioType.EAT_FOOD, false);

            this.getSnake().addPart(true);
            this.addScore(this.food.getScore());
            this.food.setRectangle(this.getSnake().foodPlacement());
        } else {
            this.getSnake().addPart(false);
        }

        if (this.getSnake().isOverlapping()) {
            this.setGameStats();
            // stop playing background music
            this.audio.stop(AudioType.BACKGROUND);
            // play game over audio
            this.audio.play(AudioType.GAME_OVER, false);
            // write data to file in decreasing order of score
            this.updateHighScore();
            // set game to be over
            this.getMainController().getGameController().setGameOver(true);
        }
    }

    public void setGameStats(){
        // get current date and time
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
        Date date = new Date();
        this.getMainController().getGameStats().setPlayDate(formatter.format(date));
        this.getMainController().getGameStats().setPlayerName(this.getMainController().getSnakeGameSettings().getPlayerName());
        this.getMainController().getGameStats().setTotalPoints(this.score);
        this.getMainController().getGameStats().setLevel(this.getMainController().getSnakeGameSettings().getLevel()+1);
        this.getMainController().getGameStats().setSpeed(this.getMainController().getSnakeGameSettings().getFps());
    }

    public void resetStats(){
        this.getMainController().getGameStats().setPlayDate(null);
        this.getMainController().getGameStats().setPlayerName(SnakeUIConstants.DEFAULT_PLAYER_NAME);
        this.getMainController().getGameStats().setTotalPoints(SnakeUIConstants.DEFAULT_POINTS);
        this.getMainController().getGameStats().setLevel(SnakeUIConstants.DEFAULT_LEVEL);
        this.getMainController().getGameStats().setSpeed(SnakeUIConstants.DEFAULT_FPS);
    }

    public void updateHighScore(){
        this.gameStatistics = GameStatistics.readGameStats();
        if (this.gameStatistics == null) {
            String playerName = JOptionPane.showInputDialog("Your Name : ");
            this.mainController.getSnakeGameSettings().setPlayerName(playerName);
            this.mainController.getGameStats().setPlayerName(playerName);
            this.gameStatistics = new ArrayList<>();
            this.gameStatistics.add(this.getMainController().getGameStats());
        }
        else {
            if(this.score > this.gameStatistics.get(this.gameStatistics.size()-1).getTotalPoints()){
                String playerName = JOptionPane.showInputDialog("Your Name : ");
                this.mainController.getSnakeGameSettings().setPlayerName(playerName);
                this.mainController.getGameStats().setPlayerName(playerName);
            }
            this.gameStatistics.add(this.getMainController().getGameStats());
        }
        this.gameStatistics = GameStatistics.assignRanks(this.gameStatistics);
        if (this.gameStatistics.size() > 10) {
            this.gameStatistics = new ArrayList<GameStatistics>(this.gameStatistics.subList(0, 10));
        }
        this.gameStatistics = GameStatistics.writeGameStats(this.gameStatistics);
    }

    public void gameRender() {
        this.getSnakePanel().gameRender();
    }

    public void paintScreen() {
        this.getSnakePanel().paintScreen();
    }
}

