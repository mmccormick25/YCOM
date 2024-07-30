package main;

import java.awt.Color;
import javax.swing.Box;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UIPanel extends JPanel implements ActionListener{
	int screenWidth;
	JLabel promptLabel = new JLabel();
	private JButton shootButton;
	private JButton moveButton;
	private JButton overwatchButton;
	private JButton coverButton;
	private JButton reloadButton;
	private JButton grenadeButton;
	private JButton passButton;
	
	UIPanel(int screenWidth) {
		this.screenWidth = screenWidth;
		this.setPreferredSize(new Dimension(screenWidth, 64));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.setFocusable(true);
		this.setLayout( new BoxLayout(this, BoxLayout.X_AXIS));
		
		promptLabel.setFont(new Font("Serif", Font.PLAIN, 20));
		promptLabel.setForeground(Color.white);
		this.add(promptLabel);
		
		this.add(Box.createRigidArea(new Dimension(30, 0)));
		
		
		this.shootButton = new JButton("Shoot");
		this.shootButton.addActionListener(this);
		this.moveButton = new JButton("Move");
		this.moveButton.addActionListener(this);
		this.overwatchButton = new JButton("OW");
		this.coverButton = new JButton("Cover");
		this.reloadButton = new JButton("Reload");
		this.grenadeButton = new JButton("Grenade");
		this.passButton = new JButton("Pass");
		
		this.add(moveButton);
		this.add(shootButton);
		this.add(overwatchButton);
		this.add(coverButton);
		this.add(reloadButton);
		this.add(grenadeButton);
		this.add(passButton);
	}
	
	public void setPrompt(String p) {
		promptLabel.setText(p);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == shootButton) {
			Main.gamePanel.shootClicked = true;
		} else if (e.getSource() == moveButton) {
			Main.gamePanel.moveClicked = true;
		}
		
	}
}

