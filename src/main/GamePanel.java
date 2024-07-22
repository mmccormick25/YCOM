package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class GamePanel extends JPanel implements Runnable{

	// Screen Settings
	final static int textureSize = 48;
	final static int scale = 1;
		
	final static int tileSize = textureSize * scale;
	final static int numCols = 24;
	final static int numRows = 15;
	final static int screenWidth = tileSize * numCols;
	final static int screenHeight = tileSize * numRows;
	
	// User input bools
	boolean updateMouse = false;
	boolean moveClicked = false;
	boolean shootClicked = false;
	
	// Holds mouse position
	int mouseRow;
	int mouseCol;
	
	// Board grid
	private Tile[][] grid = new Tile[numCols][numRows];
	
	// Player and enemy soldiers
	Set<Character> playerSoldiers = new HashSet<>();
	Set<Character> enemySoldiers = new HashSet<>();
	
	// Player selected soldier
	PlayerSoldier selectedSoldier;
	
	// Draws accuracy over enemies when shooting
	boolean drawAccuracy = false;
	
	// Wall and tile object sets
	Set<TileObject> tileObjects = new HashSet<>();
	Set<Wall> walls = new HashSet<>();
	
	// Game thread
	int FPS = 30;
	Thread gameThread;
	
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.setFocusable(true);
		
		// Mouse listener
		addMouseListener(new MouseAdapter() {

	        @Override
	        public void mousePressed(MouseEvent e) {
	        	int mouseX = e.getX();
	        	int mouseY = e.getY();
	            
	            mouseRow = mouseX / tileSize;
	            mouseCol = mouseY / tileSize;		        
	            
	            updateMouse = true;
	            
	            repaint();
	        }
	    });
	}
	
	public void initialize() {
		
		// Creating board
		for (int r = 0; r < numRows; r++) {
			for (int c = 0; c < numCols; c++) {
				grid[c][r] = new Tile(c, r);
			}
		}
		
		// Holds place in read file
		int readRow = 0;
		int readCol = 0;
		try {
			
			// Setting up board from txt file
			Scanner scanner = new Scanner(new File("src/maps/map1.txt"));
			String line;
			while (scanner.hasNextLine()) {
				line = scanner.nextLine();
				for (char c : line.toCharArray()) {
					if (c == ' ') {
						readRow++;
						continue;
					} else if (c == 'R') {
						LargeRock r = new LargeRock(readRow, readCol);
						tileObjects.add(r);
						grid[readRow][readCol].tileObject = r;
					} else if (c == 'r') {
						SmallRock r = new SmallRock(readRow, readCol);
						tileObjects.add(r);
						grid[readRow][readCol].tileObject = r;
					} else if (c == '-') {
						Wall w = new Wall(readRow, readCol, "horizontal");
						walls.add(w);
						grid[readRow][readCol].addWall(w);
						grid[readRow][readCol - 1].addWall(w);
					} else if (c == '|') {
						Wall w = new Wall(readRow, readCol, "vertical");
						walls.add(w);
						grid[readRow][readCol].addWall(w);
						grid[readRow-1][readCol].addWall(w);
					} else if (c == '/') {
						Wall w1 = new Wall(readRow, readCol, "vertical");
						walls.add(w1);
						grid[readRow][readCol].addWall(w1);
						grid[readRow-1][readCol].addWall(w1);
						Wall w2 = new Wall(readRow, readCol, "horizontal");
						walls.add(w2);
						grid[readRow][readCol].addWall(w2);
						grid[readRow][readCol - 1].addWall(w2);
					} else if (c == 'A') {
						PlayerSoldier s = new PlayerSoldier(readRow, readCol, 4, new Rifle(), 4, 2);
						playerSoldiers.add(s);
					} else if (c == 'S') {
						PlayerSoldier s = new PlayerSoldier(readRow, readCol, 4, new SniperRifle(), 4, 2);
						playerSoldiers.add(s);
					} else if (c == 'T') {
						PlayerSoldier s = new PlayerSoldier(readRow, readCol, 4, new Shotgun(), 4, 2);
						playerSoldiers.add(s);
					} else if (c == 'a') {
						EnemySoldier e = new EnemySoldier(readRow, readCol, 4, new Rifle(), 4, 2);
						enemySoldiers.add(e);
					} else if (c == 's') {
						EnemySoldier e = new EnemySoldier(readRow, readCol, 4, new SniperRifle(), 4, 2);
						enemySoldiers.add(e);
					}
				}
				readRow = 0;
				readCol++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		repaint();
		
	}
	
	public void startGameThread() {
		
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	// Checks if player is at clicked tile
	public Character checkForPlayer() {
		for(Character s : playerSoldiers) {
			if(s.getRow() == mouseRow && s.getCol() == mouseCol) {
				return s;
			}
		}
		return null;
	}
	
	// Checks if enemy is at clicked tile
	public Character checkForEnemy() {
		for(Character e : enemySoldiers) {
			if(e.getRow() == mouseRow && e.getCol() == mouseCol) {
				return e;
			}
		}
		return null;
	}
	
	// Returns tile clicked on
	public Tile checkTileSelect(Set<Tile> s) {
		for(Tile t : s) {
			if(t.getRow() == mouseRow && t.getCol() == mouseCol) {
				return t;
			}
		}
		return null;
	}
	
	// Checks if tile object is on tile
	public TileObject checkTileObject(int row, int col) {
		return grid[row][col].tileObject;
	}
	
	// Returns set of tiles that can be moved to
	public Set<Tile> getMoves(Tile startTile, int dist) {
		boolean blocked = false;
		Set<Tile> moves = new HashSet<>();
		moves.add(startTile);
		Tile t = startTile;
		if (dist > 0) {
			// Getting row and col of start tile
			int row = t.getRow();
			int col = t.getCol();
			
			// If tile has full cover, it is impassable
			TileObject o = checkTileObject(row, col);
			if (o != null){
				if (o.cover == "full") {
					return moves;
				}
			}
			
			// Checking for move to right tile
			if (row + 1 < numCols) {
				Tile t1 = grid[row + 1][col];
				if(t1.adjWalls.size() > 0) {
					for (Wall w : t1.adjWalls) {
						if(w.row == t1.row && w.col == t1.col) {	
							if (w.orientation == "vertical") {
								blocked = true;
							}
						}
					}
				}
				if (!blocked && t1.tileObject == null) {
					moves.add(t1);
					moves.addAll(getMoves(t1, dist - 1));
				} else {
					blocked = false;
				}
			}
			// Checking for move to down tile
			if (col + 1 < numRows) {
				Tile t2 = grid[row][col + 1];
				if(t2.adjWalls.size() > 0) {
					for (Wall w : t2.adjWalls) {
						if(w.row == t2.row && w.col == t2.col) {	
							if (w.orientation == "horizontal") {
								blocked = true;
							}
						}
					}
				}
				if (!blocked && t2.tileObject == null) {
					moves.add(t2);
					moves.addAll(getMoves(t2, dist - 1));
				} else {
					blocked = false;
				}
			}
			// Checking for move to left tile
			if (row - 1 >= 0) {
				Tile t3 = grid[row - 1][col];
				if(startTile.adjWalls.size() > 0) {
					for (Wall w : startTile.adjWalls) {
						if(w.row == startTile.row && w.col == startTile.col) {	
							if (w.orientation == "vertical") {
								blocked = true;
							}
						}
					}
				}
				if (!blocked && t3.tileObject == null) {
					moves.add(t3);
					moves.addAll(getMoves(t3, dist - 1));
				} else {
					blocked = false;
				}
			}
			// Checking for move to up tile
			if (col - 1 >= 0 ) {
				Tile t4 = grid[row][col - 1];
				if(startTile.adjWalls.size() > 0) {
					for (Wall w : startTile.adjWalls) {
						if(w.row == startTile.row && w.col == startTile.col) {	
							if (w.orientation == "horizontal") {
								blocked = true;
							}
						}
					}
				}
				if (!blocked && t4.tileObject == null) {
					moves.add(t4);
					moves.addAll(getMoves(t4, dist - 1));
				} else {
					blocked = false;
				}
			}
		}
		
		return moves;
		
	}
	
	public void playerMove(Character s) {
		Set<Tile> playerMoves = new HashSet<>();
		
		// Getting and displaying possible move tiles
		playerMoves = getMoves(grid[s.row][s.col], selectedSoldier.movement);
		for (Tile t : playerMoves) {
			if (checkTileObject(t.row, t.col) == null) {
				t.setHighlight(true);
			}
		}
		repaint();
		updateMouse = false;
		
		// Repeat until move tile selected
		while(true) {
			if(updateMouse) {
				// Checking if player or enemy is on square
				if(checkForPlayer() != null || checkForEnemy() != null || checkTileObject(mouseRow, mouseCol) != null) {
					updateMouse = false;
					continue;
				}
				// Checking what move tile player clicked
				Tile t = checkTileSelect(playerMoves);
				if (t != null) {
					// Moving soldier
					s.setRow(t.getRow());
					s.setCol(t.getCol());
					// Unhighlighting move tiles
					for (Tile oldMove : playerMoves) {
						oldMove.setHighlight(false);
					}
					updateMouse = false;
					repaint();
					break;
				}
			}
			try {
				 Thread.sleep(10);
			} catch (InterruptedException e) {
				 Thread.currentThread().interrupt();
			}
		}
	}
	
	public void playerSelect() {
		Main.uiPanel.setPrompt("Move");
		boolean playerInAction = false;
		// Currently selected soldier
		Character s;
		// Old selected soldier
		Character oldS;
		int unmovedSoldiers = playerSoldiers.size();
		// Repeat until char selected
		while(true) {
			// If mouse updated and no player selected
			if(updateMouse && !playerInAction) {
				s = checkForPlayer();
				
				// If mouse clicked on player
				if (s != null) {
					
					if (s.actions > 0) {
					
						// Highlighting player
						s.setSelected(true);
						repaint();
						
						// Setting soldier as selected
						selectedSoldier = (PlayerSoldier) s;
						
						while (true) {
							// If move button clicked, call player move
							if (moveClicked) {
								playerInAction = true;
								playerMove(s);
								playerInAction = false;
								moveClicked = false;
								s.actions--;
							}
							
							if (shootClicked) {
								playerInAction = true;
								playerShoot();
								playerInAction = false;
								shootClicked = false;
								s.actions = 0;
							}
							
							if (s.actions <= 0) {
								s.setSelected(false);
								s.setExhausted(true);
								moveClicked = false;
								shootClicked = false;
								unmovedSoldiers--;
								break;
							}
							
							try {
								 Thread.sleep(10);
							} catch (InterruptedException e) {
								 Thread.currentThread().interrupt();
							}
						
						}
						
					}
				}
			}
			
			// Ending turn if all soldiers moved
			if (unmovedSoldiers <= 0) {
				break;
			}
			
			try {
				 Thread.sleep(10);
			} catch (InterruptedException e) {
				 Thread.currentThread().interrupt();
			}
			
		}
		
		// Refreshing actions
		for (Character c : playerSoldiers) {
			c.actions = c.maxActions;
			c.setExhausted(false);
		}
		
		
	}
	
	// Returns adj tile objects
	public Map<String, TileObject> getCover(Tile t) {
		Map<String, TileObject> adjObjects = new HashMap();
		int playerCol = t.getCol();
		int playerRow = t.getRow();
		if (playerRow + 1 < numCols && grid[playerRow + 1][playerCol].tileObject != null) {
			adjObjects.put("right", grid[playerRow + 1][playerCol].tileObject);
		}
		if (playerRow - 1 > 0 && grid[playerRow - 1][playerCol].tileObject != null) {
			adjObjects.put("left", grid[playerRow - 1][playerCol].tileObject);
		}
		if (playerCol + 1 < numRows && grid[playerRow][playerCol + 1].tileObject != null) {
			adjObjects.put("down", grid[playerRow][playerCol + 1].tileObject);
		}
		if (playerCol - 1 > 0 && grid[playerRow][playerCol - 1].tileObject != null) {
			adjObjects.put("up", grid[playerRow][playerCol - 1].tileObject);
		}
		return adjObjects;
	}
	
	// Returns y value for supplied line function
	public double lineFunc(double playerRow, double playerCol, double x, Double slope) {
		return ((slope*(x - playerRow)) + playerCol);
	}
	
	// Returns tiles in bullet path
	public List<Tile> BulletPath(double startRow, double startCol, double endRow, double endCol, Double slope) {
		List<Tile> path = new ArrayList<>();
		double x = startRow + 0.5;
		// Logic to see if path is vertical
		if (x < endRow + 0.5) {
			// Calling line function for x vals in 0.1 increments
			while (x < endRow + 0.5) {
				double y = lineFunc(startRow + 0.5, startCol + 0.5, x, slope);
				int xInt = (int)x;
				int yInt = (int)y;
				// Adding tile line is in
				if (!path.contains(grid[xInt][yInt])) {
					path.add(grid[xInt][yInt]);
				}
				x += 0.1;
			}
		} else if (x > endRow + 0.5) {
			while (x > endRow + 0.5) {
				double y = lineFunc(startRow + 0.5, startCol + 0.5, x, slope);
				int xInt = (int)x;
				int yInt = (int)y;
				if (!path.contains(grid[xInt][yInt])) {
					path.add(grid[xInt][yInt]);
				}
				x -= 0.1;
			}
		} else {
			// Adding path in col for vertical lines
			int y = (int) startCol;
			if (startCol > endCol) {
				while (y >= endCol) {
					path.add(grid[(int) startRow][y]);
					y--;
				}
			} else {
				while (y <= endCol) {
					path.add(grid[(int) startRow][y]);
					y++;
				}
			}
		}
		return path;
	}
	
	// Getting cover bonus for tile
	public double getCoverEffectiveness(String coverDirection, String coverType, Double slope, double startCol, double startRow, double endCol, double endRow) {
		double fullCover = 0;
		double calcCover = 0;
		switch(coverType) {
			case "half":
				fullCover = 15;
				break;
			case "full":
				fullCover = 30;
				break;
		}
		
		switch(coverDirection) {
			case "up":
				if (startCol >= endCol) {
					return 0;
				} else {
					if (slope == null) {
						calcCover = fullCover;
					} else {
						calcCover = ((Math.abs(slope)/5) * fullCover);
					}
				}
					
				break;
			case "down":
				if (startCol <= endCol) {
					return 0;
				} else {
					if (slope == null) {
						calcCover = fullCover;
					} else {
						calcCover = ((Math.abs(slope)/5) * fullCover);
					}
				}
				break;
			case "left":
				if (startRow >= endRow) {
					return 0;
				} else {
					if (startCol == endCol) {
						return fullCover;
					} else {
						calcCover = (fullCover / (Math.abs(slope)*5));
					}
				}
				break;
			case "right":
				if (startRow <= endRow) {
					return 0;
				} else {
					if (startCol == endCol) {
						return fullCover;
					} else {
						calcCover = (fullCover / (Math.abs(slope)*5));
					}
				}
				break;
		}
		
		if (calcCover > fullCover) {
			return fullCover;
		} else {
			return calcCover;
		}
		
	}
	
	// Returns walls adj to char
	public List<String> getAdjWalls(Character c) {
		int cCol = c.getCol();
		int cRow = c.getRow();
		Tile t = grid[cRow][cCol];
		List<String> walls = new ArrayList<>();
		for (Wall w : t.adjWalls) {
			if (w.row == cRow && w.col == cCol) {
				if (w.orientation == "vertical") {
					walls.add("left");
				} else {
					walls.add("up");
				}
			} else {
				if (w.orientation == "vertical") {
					walls.add("right");
				} else {
					walls.add("down");
				}
			}
		}
		return walls;
	}
	
	// Creates map of chars and chance to hit
	public Map<Character, Integer> getTargets(Character c, Set<Character> enemySet) {
		// Map to hold enemys and chance of hitting
		Map<Character, Integer> enemyMap = new HashMap();
		
		double playerCol = c.getCol();
		double playerRow = c.getRow();
		
		// Set with paths for bullets
		//List<Set<Tile>> bulletPaths = new ArrayList<>();
		
		// For each enemy soldier
		for (Character e : enemySet) {
			double enemyCol = e.getCol();
			double enemyRow = e.getRow();
			
			// Getting slope of line to enemy
			Double slope = null;
			if (playerCol == enemyCol) {
				slope = 0.0;
			} else if (playerRow == enemyRow) {
				slope = null;
			} else {
				slope = ((enemyCol - playerCol)/(enemyRow - playerRow));
			}
			
			// Getting adj cover
			Map<String, TileObject> adjObjects = getCover(grid[c.getRow()][c.getCol()]);
			
			// Adding path with no position change
			List<Tile> path = BulletPath(playerRow, playerCol, enemyRow, enemyCol, slope);
			
			// Setting up accuracy variables
			double accuracyOverDistance = c.gun.accuracyOverDistance;
			double distance = Math.sqrt(Math.pow(playerRow - enemyRow, 2) + Math.pow(playerCol - enemyCol, 2));
			double accuracy = 100 - ((1 - accuracyOverDistance) * distance * 20);
			//System.out.println("Distance: " + distance);
			//System.out.println("Accuracy: " + accuracy);
			
			// Inverting slope
			Double realSlope = null;
			if (slope != null) {
				realSlope = -slope;
			}
			//System.out.println("Slope: " + realSlope);
			
			// List to hold cover options
			List<Double> coverOptions = new ArrayList<>();
			
			// Applying cover bonuses for enemies
			Map<String, TileObject> enemyAdjObjects =  getCover(grid[e.getRow()][e.getCol()]);
			for (Map.Entry<String, TileObject> o : enemyAdjObjects.entrySet()) {
				double coverEffectiveness = getCoverEffectiveness(o.getKey(), o.getValue().cover, realSlope, playerCol, playerRow, enemyCol, enemyRow);
				coverOptions.add(coverEffectiveness);
			}
			
			// Gettig adj walls to enemy
			List<String> adjWalls = getAdjWalls(e);
			for (String w : adjWalls) {
				double coverEffectiveness = getCoverEffectiveness(w, "full", realSlope, playerCol, playerRow, enemyCol, enemyRow);
				coverOptions.add(coverEffectiveness);
			}
			
			// Giving enemy the cover that gives the highest bonus
			Double bestCover = 0.0;
			for (Double d : coverOptions) {
				if (d > bestCover) {
					bestCover = d;
				}
			}
			accuracy -= bestCover;
			//System.out.println("Best Cover: " + bestCover);
			
			double bulletAngle = Math.atan2(c.getCol() - e.getCol(), e.getRow() - c.getRow());
			System.out.println(bulletAngle);
			
			Set<Wall> seenWalls = new HashSet<>();
			
			// Reducing accuracy for full cover objects in way
			for (Tile t: path) {
				if (t.tileObject != null) {
					if (t.tileObject.cover == "full" && path.indexOf(t) != 1) {
						accuracy -= 20;
					}
				}
				for (Wall w : t.adjWalls) {
					if (!seenWalls.contains(w) && path.indexOf(t) > 1 && path.indexOf(t) < path.size() - 2) {
						if (w.orientation == "vertical") {
							if ((bulletAngle > -Math.PI/4 && bulletAngle < Math.PI/4) || bulletAngle > 3*Math.PI/4 || bulletAngle < -3*Math.PI/4) {
								accuracy = 0;
							}
						}
						if (w.orientation == "horizontal") {
							if ((bulletAngle > Math.PI/4 && bulletAngle < 3*Math.PI/4) || (bulletAngle < -Math.PI/4 && bulletAngle > -3*Math.PI/4)) {
								accuracy = 0;
							}
						}
						seenWalls.add(w);
					}
				}
			}
			
			if (accuracy < 0) {
				accuracy = 0;
			}
			
			e.chanceToHit = (int) accuracy;
			
			enemyMap.put(e, e.chanceToHit);
			
			System.out.println("Final Accuracy: " + accuracy);
			
			
		}
		repaint();
		
		return enemyMap;
	}
	
	// Returns damage number after targeting enemy
	public int getDamage(int hitChance, Character c) {
		int hitRoll = new Random().nextInt(100) + 1;
		System.out.println("Hit Roll: " + hitRoll);
		if (hitRoll <= hitChance) {
			System.out.println("Hit");
			int damageRoll = new Random().nextInt(c.gun.damage.length);
			return c.gun.damage[damageRoll];
		} else {
			System.out.println("Miss");
			return 0;
		}
	}
	
	public void playerShoot() {
		Main.uiPanel.setPrompt("Shoot");
		Map<Character, Integer> targets = getTargets(selectedSoldier, enemySoldiers);
		drawAccuracy = true;
		while(true) {
			if(updateMouse) {
				Character e = checkForEnemy();
				if(e == null) {
					updateMouse = false;
					continue;
				}

				Map<String, TileObject> enemyAdjObjects =  getCover(grid[e.getRow()][e.getCol()]);
				for (Map.Entry<Character, Integer> t : targets.entrySet()) {
					if (t.getKey() == e) {
						e.health -= getDamage(t.getValue(), selectedSoldier);
						if (e.health <= 0) {
							enemySoldiers.remove(e);
						}
					}
				}
				
				if (enemySoldiers.size() == 0) {
					EndLevel end = new EndLevel(true);
				}
				
				updateMouse = false;
				break;
			}
			
			try {
				 Thread.sleep(10);
			} catch (InterruptedException e) {
				 Thread.currentThread().interrupt();
			}	
		}
		
		drawAccuracy = false;
		
		repaint();
	}
	
	
	public void enemyMove() {
		Main.uiPanel.setPrompt("Enemy Move");
		Set<Tile> enemyMoves = new HashSet<>();
		for(Character e : enemySoldiers) {
			enemyMoves = getMoves(grid[e.getRow()][e.getCol()], e.movement);
			double closestDist = 100;
			Tile targetMove = null;
			for(Tile t : enemyMoves) {
				for (Character s : playerSoldiers) {
					double xDist = Math.pow(s.getRow() - t.getRow(), 2);
					double yDist = Math.pow(s.getCol() - t.getCol(), 2);
					double dist = Math.sqrt(xDist + yDist);
					if (dist < closestDist && (t.adjWalls.size() > 0 || getCover(t).size() > 0)) {
						targetMove = t;
						closestDist = dist;
					}
				}
			}
			
			e.setCol(targetMove.getCol());
			e.setRow(targetMove.getRow());
		}
		repaint();
	}
	
	public void enemyShoot(Character e) {
		Main.uiPanel.setPrompt("Enemy Shooting");
		Map<Character, Integer> targets = getTargets(e, playerSoldiers);
		Character bestTarget = new Character();
		int bestAccuracy = 0;
		int n = 0;
		for (Map.Entry<Character, Integer> t : targets.entrySet()) {
			if (n == 0) {
				bestTarget = t.getKey();
				bestAccuracy = t.getValue();
			} else {
				if (t.getValue() > bestAccuracy) {
					bestTarget = t.getKey();
					bestAccuracy = t.getValue();
				}
			}
			n++;
		}
		
		bestTarget.health -= getDamage(bestAccuracy, bestTarget);
		if (bestTarget.health <= 0) {
			playerSoldiers.remove(bestTarget);
		}
		
		if (playerSoldiers.size() == 0) {
			EndLevel end = new EndLevel(false);
		}
		
	}
	
	public void enemyTurn() {
		enemyMove();
		for (Character e : enemySoldiers) {
			enemyShoot(e);
		}
	}
	
	public void update() {
	}
	

	int n = 0;
	
	@Override
	public void run() {
		
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;
		
		while(gameThread != null) {

			playerSelect();
			enemyTurn();
			
			currentTime = System.nanoTime();
			
			delta += (currentTime - lastTime) / drawInterval;
			timer += currentTime - lastTime;
			
			lastTime = currentTime;
			
			if(delta >= 1) {
				//update();
				//repaint();
				delta--;
				drawCount++;
			}
			
			if (timer >= 1000000000) {
				//System.out.println("FPS: " + drawCount);
				drawCount = 0;
				timer = 0;
			}
			
		}
		
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		
		Graphics2D g2 = (Graphics2D) g;
		
		for (int r = 0; r < numRows; r++) {
			for (int c = 0; c < numCols; c++) {
				grid[c][r].draw(g2);
			}
		}
		
		for (Character s : playerSoldiers) {
			s.draw(g2);
			s.drawHealth(g2, Color.blue, tileSize);
		}
		
		for (TileObject t : tileObjects) {
			t.draw(g2);
		}
		
		for (Wall w : walls) {
			w.draw(g2);
		}
		
		for (Character e : enemySoldiers) {
			e.draw(g2);
			e.drawHealth(g2, Color.red, tileSize);
		}
		
		if (drawAccuracy) {
			for (Character e : enemySoldiers ) {
				g2.setColor(Color.black);
				g2.fillRect(e.x + 9, e.y - 33, 25, 20);
				
				g2.setColor(Color.red);
				g2.setFont(new Font("Serif", Font.PLAIN, 20)); 
				if(e.chanceToHit > 0) {
					g2.drawString(String.valueOf(e.chanceToHit), e.x + 12, e.y - 16);
				} else {
					g2.drawString(String.valueOf("00"), e.x + 12, e.y - 16);
				}
			}
		}
		
		g2.dispose();
	}

		
}


