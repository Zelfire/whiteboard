import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.*;
public class SwingTest
{
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		JButton test = new JButton("Test");
		panel.add(test);
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
}
