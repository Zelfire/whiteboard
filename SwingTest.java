import java.awt.*;
import javax.swing.*;
public class SwingTest
{
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.add(new JButton("Set Color"));
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
}
