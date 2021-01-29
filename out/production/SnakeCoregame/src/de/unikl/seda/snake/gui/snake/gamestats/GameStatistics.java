package de.unikl.seda.snake.gui.snake.gamestats;

import de.unikl.seda.snake.gui.snake.constants.SnakeUIConstants;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class GameStatistics implements Comparable<GameStatistics>, Serializable{
    private String playerName;
    private int totalPoints;
    private int totalTimeSpent;
    private int playerRank;
    private int level;
    private String speed;
    private String playDate;
    private static final Path highScoreFile = Paths.get(GameStatistics.getSerPath().toString(), "highScoreData.ser");

    public String getPlayerName(){
        return this.playerName;
    }

    public void setPlayerName(String playerName){
        this.playerName = playerName;
    }

    public int getTotalPoints(){
        return this.totalPoints;
    }

    public void setTotalPoints(int points){
        this.totalPoints = points;
    }

    public int getTotalTimeSpent(){
        return this.totalTimeSpent;
    }

    public void setTotalTimeSpent(int totalTimeSpent){
        this.totalTimeSpent = totalTimeSpent;
    }

    public int getPlayerRank(){
        return this.playerRank;
    }

    public void setPlayerRank(int rank){
        this.playerRank = rank;
    }

    public String getPlayDate(){
        return this.playDate;
    }

    public void setPlayDate(String date){
        this.playDate = date;
    }

    public int getLevel(){
        return this.level;
    }

    public void setLevel(int level){
        this.level = level;
    }

    public String getSpeed(){
        return this.speed;
    }

    public void setSpeed(int speed){
        if (speed <= 10){
            this.speed = "Slow";
        }
        else if (speed > 10 && speed <= 15){
            this.speed = "Medium";
        }
        else if (speed >=15 && speed < 20) {
            this.speed = "Fast";
        }
        else{
            this.speed = "Very Fast";
        }
    }

    @Override
    public int compareTo(GameStatistics o) {
        if (this.getTotalPoints() == o.getTotalPoints())
            return 0;
        else if (this.getTotalPoints() < o.getTotalPoints())
            return 1;
        else
            return -1;
    }

    public static ArrayList<GameStatistics> readGameStats(){
        ArrayList<GameStatistics> gameStats;
        try {
            FileInputStream fileInputStream = new FileInputStream(highScoreFile.toString());
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            gameStats = (ArrayList<GameStatistics>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception exp){
            gameStats = null;
        }
        return gameStats;
    }

    public static File getSerPath(){
        String highScoreFilePath = System.getenv("APPDATA");
        if (highScoreFilePath == null) {
            highScoreFilePath = System.getProperty("user.home");
        }
        File CONFIG_HOME = new File(highScoreFilePath, ".snakegame").getAbsoluteFile();
        CONFIG_HOME.mkdirs();
        return CONFIG_HOME;
    }
    public static ArrayList<GameStatistics> writeGameStats(ArrayList<GameStatistics> gameStats){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(highScoreFile.toString());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(gameStats);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception exp){
            exp.printStackTrace();
        }
        return gameStats;
    }

    public static ArrayList<GameStatistics> assignRanks(ArrayList<GameStatistics> gameStatistics){
        Collections.sort(gameStatistics);
        int rank=1;
        for (GameStatistics game : gameStatistics){
            game.setPlayerRank(rank);
            rank++;
        }
        return gameStatistics;
    }
}
