package net.sf.jcommon.ui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author Ionut BARCAN
 * @author Adrian BER
 */
public class CountdownButton extends JButton {

    private Timer timer;
    private int secondsToShow;
    private int secondsElapsed;

    public CountdownButton(String text, int secondsToShow) {
        super(text);
        this.secondsToShow = secondsToShow;
        init();
    }

    public CountdownButton(Action a, int secondsToShow) {
        super(a);
        this.secondsToShow = secondsToShow;
        init();
    }

    public CountdownButton(Icon icon, int secondsToShow) {
        super(icon);
        this.secondsToShow = secondsToShow;
        init();
    }

    public CountdownButton(String text, Icon icon, int secondsToShow) {
        super(text, icon);
        this.secondsToShow = secondsToShow;
        init();
    }

    private void init() {
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                secondsElapsed++;
                CountdownButton.this.repaint();
                if (secondsElapsed >= secondsToShow)
                    CountdownButton.this.doClick();
            }
        });
        timer.setRepeats(true);
        timer.setInitialDelay(1000);
        timer.setDelay(1000);
    }

    public void start() {
        secondsElapsed = 0;
        timer.start();
    }

    public void restart() {
        secondsElapsed = 0;
        timer.restart();
    }

    public void stop() {
        timer.stop();
    }

    public String getText() {
        return super.getText() + " (" + (secondsToShow - secondsElapsed) + ")";
    }
}
