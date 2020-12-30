package de.unikl.seda.snake.gui.tools.engine;

public class Animator implements Runnable {
    private final GameController gameController;
    private final StatisticsManager statMngr;
    private final AnimationLoop animationLoop;
    private int FPS;
    private long period;
    private boolean suspended;

    public Animator(GameController gameController, AnimationLoop animationLoop, int FPS) {
        this.setFPS(FPS);
        this.setPeriod(calculatePeriodNS(this.getFPS()));
        this.gameController = gameController;
        this.statMngr = new StatisticsManager(this.getFPS(), this.getPeriod());
        this.animationLoop = animationLoop;
    }

    public GameController getGameController() {
        return this.gameController;
    }

    public StatisticsManager getStatisticsManager() {
        return this.statMngr;
    }

    public AnimationLoop getAnimationLoop() {
        return this.animationLoop;
    }

    public int getFPS() {
        return this.FPS;
    }

    private void setFPS(int request) {
        if (request > 0 && request <= 100) {
            this.FPS = request;
        } else {
            this.FPS = 10;
        }

    }

    public long getPeriod() {
        return this.period;
    }

    public static long calculatePeriodNS(int FPS) {
        return 1000000000L / (long)FPS;
    }

    private void setPeriod(long request) {
        this.period = request;
    }

    public void run() {
        long overSleepTime = 0L;
        int numberOfDelays = 0;
        long excess = 0L;
        this.getStatisticsManager().setGameStartTime(System.nanoTime());
        this.getStatisticsManager().setPrevStatsTime(this.getStatisticsManager().getGameStartTime());
        long beforeTime = this.getStatisticsManager().getGameStartTime();
        this.getGameController().setRunning(true);

        while(this.getGameController().isRunning()) {
            try {
                synchronized (this) {
                    while (suspended) {
                        wait();
                    }
                }
            } catch (InterruptedException var15) {
                var15.printStackTrace();
            }
            if (!this.getGameController().isPaused() && !this.getGameController().isGameOver()) {
                this.gameUpdate();
                this.getAnimationLoop().gameRender();
                this.getAnimationLoop().paintScreen();
                long afterTime = System.nanoTime();
                long timeDiff = afterTime - beforeTime;
                long sleepTime = this.getPeriod() - timeDiff - overSleepTime;
                if (sleepTime > 0L) {
                    try {
                        Thread.sleep(sleepTime / 1000000L);
                    } catch (InterruptedException var15) {
                        var15.printStackTrace();
                    }
                    overSleepTime = System.nanoTime() - afterTime - sleepTime;
                } else {
                    excess -= sleepTime;
                    overSleepTime = 0L;
                    ++numberOfDelays;
                    if (numberOfDelays >= 16) {
                        Thread.yield();
                        numberOfDelays = 0;
                    }
                }
                beforeTime = System.nanoTime();
                this.getStatisticsManager().storeStats();
            }
        }
        this.finishGame();
    }

    void suspend(){
        this.suspended = true;
    }

    synchronized void resume(){
        this.suspended = false;
        notify();
    }

    private void gameUpdate() {
        this.getAnimationLoop().gameUpdate();
    }

    private void finishGame() {
        this.getStatisticsManager().printStats();
        System.exit(0);
    }
}
