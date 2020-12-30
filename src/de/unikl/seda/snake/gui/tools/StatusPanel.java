package de.unikl.seda.snake.gui.tools;

import de.unikl.seda.snake.gui.tools.engine.StatisticsListener;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StatusPanel extends JPanel implements StatisticsListener {
    private JTextField jtfScore;
    private JTextField jtfTime;

    public StatusPanel() {
        this.initialize();
    }

    private void initialize() {
        this.setLayout(new BoxLayout(this, 0));
        this.jtfScore = new JTextField("Score: 0");
        this.jtfScore.setEditable(false);
        this.add(this.jtfScore);
        this.jtfTime = new JTextField("Time Spent: 0 secs");
        this.jtfTime.setEditable(false);
        this.add(this.jtfTime);
    }

    public void setScore(int no) {
        this.jtfScore.setText("Score: " + no);
    }

    public void setTimeSpent(long t) {
        this.jtfTime.setText("Time Spent: " + t + " secs");
    }

    public void updatePerformanceInfo(String msg) {
    }

    public void updateTimeSpentIngame(int time) {
        this.setTimeSpent((long)time);
    }
}

