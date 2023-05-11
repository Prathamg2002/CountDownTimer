import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.Duration;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class CountDown_Timer extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	DecimalFormat formatter = new DecimalFormat("00");
	long hours, minutes, seconds;
	long inputTime, lastTickTime, runningTime, timeLeft;

	JLabel labelTime, h, min, sec;
	JComboBox<String> hourComboBox, minutesComboBox, secondsComboBox;
	JButton reset, start, pause;

	Timer timer; // importing javax.swing.Timer class

	public CountDown_Timer() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (UnsupportedLookAndFeelException e) {
		} // Refines the look of the ui

		hours = minutes = seconds = 0;

		Image icon = Toolkit.getDefaultToolkit().getImage("clock-icon.png").getScaledInstance(60, 60,
				Image.SCALE_SMOOTH);
		setIconImage(icon);
		setTitle("CountDown Timer");
		setLayout(null);
		setSize(250, 300);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Frame exit when close button is pressed

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}

	public static void main(String main[]) {
		new CountDown_Timer();
	}

	public void createAndShowGUI() {
		labelTime = new JLabel(); // label that displays the time
		changeLabelTimer(); // initially displays 00:00:00
		labelTime.setFont(new Font("Arial", Font.PLAIN, 30));
		labelTime.setBounds(40, 5, 200, 80);
		add(labelTime);

		hourComboBox = new JComboBox<String>(); // combobox to choose hour, taken only 0-24 hours here, can be extended
		for (int i = 0; i <= 24; i++) { // filling values
			hourComboBox.addItem(formatter.format(i));
		}
		hourComboBox.setBounds(25, 90, 60, 50);
		hourComboBox.addActionListener(this);
		hourComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
		((JLabel) hourComboBox.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		add(hourComboBox);

		minutesComboBox = new JComboBox<String>(); // combobox to choose minutes
		for (int i = 0; i < 60; i++) { // filling values
			minutesComboBox.addItem(formatter.format(i));
		}
		minutesComboBox.setBounds(85, 90, 60, 50);
		minutesComboBox.addActionListener(this);
		minutesComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
		((JLabel) minutesComboBox.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		add(minutesComboBox);

		secondsComboBox = new JComboBox<String>(); // combobox to choose seconds
		for (int i = 0; i < 60; i++) { // filling values
			secondsComboBox.addItem(formatter.format(i));
		}
		secondsComboBox.setBounds(145, 90, 60, 50);
		secondsComboBox.addActionListener(this);
		secondsComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
		((JLabel) secondsComboBox.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		add(secondsComboBox);

		h = new JLabel("h");
		h.setBounds(50, 140, 20, 20);
		h.setFont(new Font("Arial", Font.ITALIC, 13));
		add(h);

		min = new JLabel("min");
		min.setBounds(105, 140, 40, 20);
		min.setFont(new Font("Arial", Font.ITALIC, 13));
		add(min);

		sec = new JLabel("sec");
		sec.setBounds(165, 140, 40, 20);
		sec.setFont(new Font("Arial", Font.ITALIC, 13));
		add(sec);

		Image resetImage = Toolkit.getDefaultToolkit().getImage("reset-icon.png").getScaledInstance(50, 50,
				Image.SCALE_SMOOTH); // Scaled Instance sets the image width and height
		reset = new JButton(new ImageIcon(resetImage));
		reset.setBounds(33, 190, resetImage.getWidth(getParent()), resetImage.getHeight(getParent()));
		reset.setBorder(BorderFactory.createEmptyBorder()); // creates empty border
		reset.setContentAreaFilled(false); // empty area around the image is not filled
		reset.addActionListener(this);
		reset.setEnabled(false); // disabled
		add(reset);

		Image startImage = Toolkit.getDefaultToolkit().getImage("play-icon.png").getScaledInstance(50, 50,
				Image.SCALE_SMOOTH); // Scaled Instance sets the image width and height
		start = new JButton(new ImageIcon(startImage));
		start.setBounds(92, 190, startImage.getWidth(getParent()), startImage.getHeight(getParent()));
		start.setBorder(BorderFactory.createEmptyBorder()); // creates empty border
		start.setContentAreaFilled(false); // empty area around the image is not filled
		start.addActionListener(this);
		add(start);

		Image pauseImage = Toolkit.getDefaultToolkit().getImage("pause-icon.jpg").getScaledInstance(50, 50,
				Image.SCALE_SMOOTH); // Scaled Instance sets the image width and height
		pause = new JButton(new ImageIcon(pauseImage));
		pause.setBounds(150, 190, pauseImage.getWidth(getParent()), pauseImage.getHeight(getParent()));
		pause.setBorder(BorderFactory.createEmptyBorder()); // creates empty border
		pause.setContentAreaFilled(false); // empty area around the image is not filled
		pause.addActionListener(this);
		pause.setEnabled(false); // disabled
		add(pause);
	}

	public long inputTimeInMilliseconds() {

		return (hours * 60 * 60 * 1000) + (minutes * 60 * 1000) + (seconds * 1000) + 1000;
	}

	public void update() {
		Duration duration = Duration.ofMillis(timeLeft); // Duration holds the amount of time

		hours = duration.toHours(); // gets the amount of hours from duration
		duration = duration.minusHours(hours); // subtracts the hour value from duration
		minutes = duration.toMinutes(); // gets the amount of minutes from duration
		duration = duration.minusMinutes(minutes); // subtracts the minutes value from duration
		seconds = duration.toMillis() / 1000; // gets the amount of seconds from duration
	}

	public void changeLabelTimer() {
		// labelTime.setForeground(Color.BLACK);
		labelTime.setText(
				formatter.format(hours) + " : " + formatter.format(minutes) + " : " + formatter.format(seconds));
	}

	public void reset() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}

		hours = minutes = seconds = 0; // to reset the label to 00:00:00
		changeLabelTimer();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == hourComboBox) {
			hours = Integer.parseInt(hourComboBox.getItemAt(hourComboBox.getSelectedIndex()));
			changeLabelTimer();
		}

		if (e.getSource() == minutesComboBox) {
			minutes = Integer.parseInt(minutesComboBox.getItemAt(minutesComboBox.getSelectedIndex()));
			changeLabelTimer();
		}

		if (e.getSource() == secondsComboBox) {
			seconds = Integer.parseInt(secondsComboBox.getItemAt(secondsComboBox.getSelectedIndex()));
			changeLabelTimer();
		}

		if (e.getSource() == start) {
			reset.setEnabled(true);
			pause.setEnabled(true); // enabling the disabled buttons
			start.setEnabled(false); // disabling as to prevent running two timers for the same task
			hourComboBox.setEnabled(false);
			minutesComboBox.setEnabled(false);
			secondsComboBox.setEnabled(false);
			inputTime = inputTimeInMilliseconds(); // time selected from the comboboxes are converted into milliseconds
			lastTickTime = System.currentTimeMillis();

			timer = new Timer(1000, new ActionListener() { // timer to countdown the set time, 1000 ms (1 second) delay

				@Override
				public void actionPerformed(ActionEvent e) {
					runningTime = System.currentTimeMillis() - lastTickTime;
					timeLeft = inputTime - runningTime;

					update();
					changeLabelTimer();

					if (hours <= 0 && seconds <= 0 && minutes <= 0) {
						Toolkit.getDefaultToolkit().beep(); // for the beep noise when time label gets to 00:00:00
						timer.stop(); // timer is stopped
						start.setEnabled(true);
						pause.setEnabled(false);
						reset.setEnabled(false);
						hourComboBox.setSelectedIndex(0);
						minutesComboBox.setSelectedIndex(0);
						secondsComboBox.setSelectedIndex(0); // resetting comboboxes to their initial values
						hourComboBox.setEnabled(true);
						minutesComboBox.setEnabled(true);
						secondsComboBox.setEnabled(true); // enabling comboboxes after timer is completed
					}
				}
			});

			timer.start(); // starting timer
		}

		if (e.getSource() == pause) {
			timer.stop(); // stopping timer
			pause.setEnabled(false);
			start.setEnabled(true);
		}

		if (e.getSource() == reset) {
			timer.stop(); // stopping timer
			reset();
			reset.setEnabled(false);
			pause.setEnabled(false);
			start.setEnabled(true);
			hourComboBox.setSelectedIndex(0);
			minutesComboBox.setSelectedIndex(0);
			secondsComboBox.setSelectedIndex(0); // resetting comboboxes to their initial values
			hourComboBox.setEnabled(true);
			minutesComboBox.setEnabled(true);
			secondsComboBox.setEnabled(true); // enabling comboboxes after timer is completed
		}
	}

}
