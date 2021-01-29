package de.unikl.seda.snake.gui.snake.engine;

import de.unikl.seda.snake.gui.snake.variable.SnakeUIVariables;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class StatisticsManager {
    private int FPS;
    private long period;
    private long prevStatsTime;
    private long gameStartTime;
    private long statsInterval;
    private long totalElapsedTime;
    private int timeSpentInGame;
    private long statsCount;
    private long frameCount;
    private double[] fpsStore;
    private double averageFPS;
    private long framesSkipped;
    private long totalFramesSkipped;
    private double[] upsStore;
    private double averageUPS;
    private static final DecimalFormat df = new DecimalFormat("0.##");
    private static final DecimalFormat timedf = new DecimalFormat("0.####");
    private List<StatisticsListener> statisticsListeners;

    public StatisticsManager(int FPS, long period) {
        this.statsInterval = 0L;
        this.totalElapsedTime = 0L;
        this.timeSpentInGame = 0;
        this.statsCount = 0L;
        this.frameCount = 0L;
        this.averageFPS = 0.0D;
        this.framesSkipped = 0L;
        this.totalFramesSkipped = 0L;
        this.averageUPS = 0.0D;
        this.statisticsListeners = new LinkedList();
        this.setFPS(FPS);
        this.setPeriod(period);
        this.fpsStore = new double[this.getFPS()];
        this.upsStore = new double[this.getFPS()];

        for(int i = 0; i < this.getFPS(); ++i) {
            this.fpsStore[i] = 0.0D;
            this.upsStore[i] = 0.0D;
        }

    }

    public int getFPS() {
        return this.FPS;
    }

    private void setFPS(int request) {
        this.FPS = request;
    }

    public long getPeriod() {
        return this.period;
    }

    private void setPeriod(long request) {
        this.period = request;
    }

    public void setPrevStatsTime(long request) {
        this.prevStatsTime = request;
    }

    public long getGameStartTime() {
        return this.gameStartTime;
    }

    public void setGameStartTime(long request) {
        this.gameStartTime = request;
    }

    public long getFramesSkipped() {
        return this.framesSkipped;
    }

    public void storeStats() {
        ++this.frameCount;
        this.statsInterval += this.getPeriod();
        if (this.statsInterval >= 1000000000L) {
            long timeNow = System.nanoTime();
            this.timeSpentInGame = (int)((timeNow - this.gameStartTime) / 1000000000L);
            long realElapsedTime = timeNow - this.prevStatsTime;
            this.totalElapsedTime += realElapsedTime;
            double timingError = (double)(realElapsedTime - this.statsInterval) / (double)this.statsInterval * 100.0D;
            this.totalFramesSkipped += this.getFramesSkipped();
            double actualFPS = 0.0D;
            double actualUPS = 0.0D;
            if (this.totalElapsedTime > 0L) {
                actualFPS = (double)this.frameCount / (double)this.totalElapsedTime * 1.0E9D;
                actualUPS = (double)(this.frameCount + this.totalFramesSkipped) / (double)this.totalElapsedTime * 1.0E9D;
            }

            this.fpsStore[(int)this.statsCount % this.getFPS()] = actualFPS;
            this.upsStore[(int)this.statsCount % this.getFPS()] = actualUPS;
            ++this.statsCount;
            double totalFPS = 0.0D;
            double totalUPS = 0.0D;

            for(int i = 0; i < this.getFPS(); ++i) {
                totalFPS += this.fpsStore[i];
                totalUPS += this.upsStore[i];
            }

            if (this.statsCount < (long)this.getFPS()) {
                this.averageFPS = totalFPS / (double)this.statsCount;
                this.averageUPS = totalUPS / (double)this.statsCount;
            } else {
                this.averageFPS = totalFPS / (double)this.getFPS();
                this.averageUPS = totalUPS / (double)this.getFPS();
            }

            String var10000 = timedf.format((double)this.statsInterval / 1.0E9D);
            String performanceInfo = var10000 + " " + timedf.format((double)realElapsedTime / 1.0E9D) + "s " + df.format(timingError) + "% " + this.frameCount + "c " + this.framesSkipped + "/" + this.totalFramesSkipped + " skip; " + df.format(actualFPS) + " " + df.format(this.averageFPS) + " afps; " + df.format(actualUPS) + " " + df.format(this.averageUPS) + " aups";
            Iterator var16 = this.statisticsListeners.iterator();

            while(var16.hasNext()) {
                StatisticsListener sl = (StatisticsListener)var16.next();
                sl.updateTimeSpentIngame(this.timeSpentInGame);
                sl.updatePerformanceInfo(performanceInfo);
            }

            this.framesSkipped = 0L;
            this.prevStatsTime = timeNow;
            this.statsInterval = 0L;
        }

    }

    public int getTimeSpentInGame(){
        return this.timeSpentInGame;
    }

    public void printStats() {
    }

    public void addStatisticsListener(StatisticsListener statisticsListener) {
        this.statisticsListeners.add(statisticsListener);
    }
}

