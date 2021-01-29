package de.unikl.seda.snake.gui.tools;

import de.unikl.seda.snake.gui.snake.constants.SnakeUIConstants;
import de.unikl.seda.snake.gui.snake.engine.StatisticsListener;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.*;

public class StatusPanel extends JPanel implements StatisticsListener {
    private JTextField jtfScore;
    private JTextField jtfTime;

    public StatusPanel() {
        this.initialize();
    }

    private void initialize() {
        this.setLayout(new BoxLayout(this, 0));
        this.jtfScore = new JTextField("Score: 0");
        this.jtfScore.setOpaque(false);
        this.jtfScore.setEditable(false);
        this.jtfScore.setForeground(SnakeUIConstants.menuFontColor);
        this.jtfScore.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 15));
        this.add(this.jtfScore);

        this.jtfTime = new JTextField("Time Spent: 0 secs");
        this.jtfTime.setEditable(false);
        this.jtfTime.setOpaque(false);
        this.jtfTime.setForeground(SnakeUIConstants.menuFontColor);
        this.jtfTime.setFont(new Font(SnakeUIConstants.font, Font.BOLD, 15));
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

