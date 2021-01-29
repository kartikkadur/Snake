package de.unikl.seda.snake.gui.snake.variable;

import java.io.*;
import java.nio.file.Paths;

public class SnakeUIVariables implements Serializable {
    private int rows = 24;
    private int cols = 32;
    private int fps = 10;
    private int level = 1;
    private int volumeLevel = 15;

    private boolean includeSpecialFood = false;

    private String playerName = "Player";
    private  String windowSize = "Small";

    private boolean isMuted;

    public void setRows(int rows){
        this.rows=rows;
    }

    public void setCols(int cols){
        this.cols=cols;
    }

    public void setFps(int fps){
        this.fps=fps;
    }

    public void setLevel(int level){
        this.level=level;
    }

    public void setPlayerName(String name){
        this.playerName=name;
    }

    public void setVolumeLevel(int level){
        this.volumeLevel = level;
    }

    public void setWindowSize(String size){
        this.windowSize = size;
    }

    public void setMuted(boolean muted){
        this.isMuted = muted;
    }

    public boolean getMuted(){
        return this.isMuted;
    }

    public String getWindowSize(){
        return this.windowSize;
    }

    public int getVolumeLevel(){
        return this.volumeLevel;
    }

    public int getRows(){
        return this.rows;
    }

    public int getCols(){
        return this.cols;
    }

    public int getFps(){
        return this.fps;
    }

    public int getLevel(){
        return this.level;
    }

    public String getPlayerName(){
        return this.playerName;
    }

    public boolean getIncludeSpecialFood(){
        return this.includeSpecialFood;
    }

    public void setIncludeSpecialFood(boolean value){
        this.includeSpecialFood = value;
    }

    public static File getSerialPath(){
        String levelSettingsFilePath = System.getenv("APPDATA");
        if (levelSettingsFilePath == null) {
            levelSettingsFilePath = System.getProperty("user.home");
        }
        File CONFIG_HOME = new File(levelSettingsFilePath, ".snakegame").getAbsoluteFile();
        CONFIG_HOME.mkdirs();
        return CONFIG_HOME;
    }


    public static SnakeUIVariables writeLevelSettings(SnakeUIVariables settings){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(
                    Paths.get(SnakeUIVariables.getSerialPath().toString(), "gameSettings.ser").toString());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(settings);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception exp){
            exp.printStackTrace();
        }
        return settings;
    }

    public static SnakeUIVariables readLevelSettings(){
        SnakeUIVariables settings;
        try {
            FileInputStream fileInputStream = new FileInputStream(
                    Paths.get(SnakeUIVariables.getSerialPath().toString(), "gameSettings.ser").toString());
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            settings = (SnakeUIVariables) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception exp){
            settings = null;
        }
        return settings;
    }
}
