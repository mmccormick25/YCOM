package main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EndLevel extends JFrame implements ActionListener {
	
	JPanel levelEndPanel = new JPanel();
	JLabel endLabel = new JLabel();
	
	private JButton endLevelButton = new JButton("Ok");
	
	boolean won;
	
	EndLevel(boolean won) {
		
		this.won = won;
		this.setResizable(false);
		
		levelEndPanel.setLayout( new BoxLayout(levelEndPanel, BoxLayout.Y_AXIS));
		levelEndPanel.setSize(new Dimension(300, 200));
		this.setSize(levelEndPanel.getSize());
		
		endLabel.setFont(new Font("Serif", Font.PLAIN, 30));
		endLabel.setAlignmentX(this.CENTER_ALIGNMENT);
		if (won) {
			endLabel.setText("You Won!");
		} else {
			endLabel.setText("You Lost.");
		}
		
		levelEndPanel.add(Box.createRigidArea(new Dimension(0,30)));
		
		levelEndPanel.add(endLabel);
		
		levelEndPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		endLevelButton.setAlignmentX(this.CENTER_ALIGNMENT);
		endLevelButton.setFont(new Font("Serif", Font.PLAIN, 20));
		endLevelButton.addActionListener(this);
		levelEndPanel.add(endLevelButton);
		
		this.add(levelEndPanel);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (won) {
			GamePanel.nextLevelClicked = true;
			this.dispose();
		} else {
			System.exit(0);
		}
		
	}
	
}
