package main;

import java.awt.Color;
import java.awt.LayoutManager;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Main {
	
	static GamePanel gamePanel = new GamePanel();
	static UIPanel uiPanel = new UIPanel(gamePanel.screenWidth);
	
public static void main(String[] args) {
		
		JFrame gameWindow = new JFrame();
		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameWindow.setBackground(Color.black);
		gameWindow.setResizable(false);
		gameWindow.setTitle("YCOM");
		
		gameWindow.setLayout( new BoxLayout(gameWindow.getContentPane(), BoxLayout.Y_AXIS));
		
		gamePanel.initialize();
		gamePanel.startGameThread();
		
		gameWindow.add(gamePanel);
		
		uiPanel.setPrompt("Shoot!");
		gameWindow.add(uiPanel);
		
		
		gameWindow.pack();
		
		gameWindow.setLocationRelativeTo(null);
		gameWindow.setVisible(true);
	}
	
}

