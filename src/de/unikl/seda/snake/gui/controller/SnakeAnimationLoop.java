package de.unikl.seda.snake.gui.controller;

import de.unikl.seda.snake.gui.snake.Food;
import de.unikl.seda.snake.gui.snake.Snake;
import de.unikl.seda.snake.gui.tools.SnakePanel;
import de.unikl.seda.snake.gui.tools.audio.Audio;
import de.unikl.seda.snake.gui.tools.audio.AudioType;
import de.unikl.seda.snake.gui.tools.cache.image.ImageCache;
import de.unikl.seda.snake.gui.tools.engine.AnimationLoop;
import de.unikl.seda.snake.gui.tools.constants.SnakeUIConstants;
import de.unikl.seda.snake.gui.tools.foodType.FoodType;

import java.awt.Point;

public class SnakeAnimationLoop implements AnimationLoop {
    int level;
    private final MainController mainController;
    private Snake snake;
    private int score;
    private Food food;
    private final Direction direction;
    private static ImageCache imageCache;
    private static Audio audio;
    private static int updateFreq;

    public SnakeAnimationLoop(MainController mainController,
                              Direction direction,
                              ImageCache imageCache,
                              Audio audio){
        this.mainController = mainController;
        this.level = mainController.getLevel();
        this.direction = direction;
        SnakeAnimationLoop.imageCache = imageCache;
        SnakeAnimationLoop.audio = audio;
        SnakeAnimationLoop.updateFreq = 0;
        this.createModels();
    }

    public MainController getMainController() {
        return this.mainController;
    }

    public SnakePanel getSnakePanel() {
        return this.getMainController().getSnakeFrame().getSnakePanel();
    }

    private void createModels(){
        this.snake = new Snake(mainController.getRows(), mainController.getColumns(), mainController.getLevel(), this.direction, this.imageCache);
        this.food = new Food(new Point(this.getSnake().getRows() / 2, this.getSnake().getColumns() / 4), this.imageCache);
        this.getMainController().getSnakeFrame().getSnakePanel().addDrawable(this.snake);
        this.getMainController().getSnakeFrame().getSnakePanel().addDrawable(this.food);
    }

    public Snake getSnake() {
        return this.snake;
    }

    public int getScore() {
        return this.score;
    }

    private void addScore(int request) {
        this.score += request;
        this.getMainController().getSnakeFrame().getStatusPanel().setScore(this.score);
    }

    public void gameUpdate() {
        // start playing background audio
        if (!this.getMainController().getGameController().isPaused()){
            SnakeAnimationLoop.audio.play(AudioType.BACKGROUND, true);
        } else{
            SnakeAnimationLoop.audio.pause(AudioType.BACKGROUND);
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
            SnakeAnimationLoop.audio.play(AudioType.EAT_FOOD, false);

            this.getSnake().addPart(true);
            this.addScore(this.food.getScore());
            this.food.setRectangle(this.getSnake().foodPlacement());
        } else {
            this.getSnake().addPart(false);
        }

        if (this.getSnake().isOverlapping()) {

            SnakeAnimationLoop.audio.stop(AudioType.BACKGROUND);
            // set game to be over
            this.getMainController().getGameController().setGameOver(true);
            // play game over audio
            SnakeAnimationLoop.audio.play(AudioType.GAME_OVER, false);
        }
    }

    public void gameRender() {
        this.getSnakePanel().gameRender();
    }

    public void paintScreen() {
        this.getSnakePanel().paintScreen();
    }
}

