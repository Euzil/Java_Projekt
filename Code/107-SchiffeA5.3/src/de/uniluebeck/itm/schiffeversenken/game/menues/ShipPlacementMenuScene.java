package de.uniluebeck.itm.schiffeversenken.game.menues;

import de.uniluebeck.itm.schiffeversenken.engine.*;
import de.uniluebeck.itm.schiffeversenken.game.*;
import de.uniluebeck.itm.schiffeversenken.game.ai.*;
import de.uniluebeck.itm.schiffeversenken.game.model.*;

/**
 * This class enables the player to place ships on the field based on the provided ruleset.
 * @author leondietrich, modified by Gruppe 107 Youran Wang (719511)
 *
 */
public class ShipPlacementMenuScene extends Scene {

	private final Ruleset ruleset;
	private final GameField field;
	private final String[] playerNames;
	private final int aiHardness;
	private final Scene backButtonScene;

	private final GameFieldRenderer renderer;

	private boolean currentOrientationIsUp;
	private boolean showGameStartingHint;
	private int currentSelectedShipsLength;
	private final int[] placedShips;
	private int drawnWidth, drawnHeight;
	
	public ShipPlacementMenuScene(Ruleset r, String[] playerNames, int aiHardness, Scene backButtonScene) {
		this.ruleset = r;
		this.field = new GameField(r.getGameFieldSize());
		this.playerNames = playerNames;
		this.renderer = new GameFieldRenderer(this.field);
		this.showGameStartingHint = false;
		this.currentOrientationIsUp = true;
		this.currentSelectedShipsLength = 0;
		this.placedShips = new int[]{0, 0, 0, 0, 0};
		this.drawnWidth = 0;
		this.drawnHeight = 0;
		this.aiHardness = aiHardness;
		this.backButtonScene = backButtonScene;
	}

	@Override
	public void draw(Canvas c, Vec2 mousePosition) {
		final int width = c.getResolutionWidth(), height = c.getResolutionHeight();

		this.drawnHeight = height;
		this.drawnWidth = width;

		this.renderer.renderGameField(c, 15, 50);
		c.setColor(0.7, 0.7, 0.7);
		c.drawString(c.getResolutionWidth() - 200, 50, "Press R to rotate the ship");

		if (this.currentSelectedShipsLength > 0) {
			// We're currently positioning a ship
			final String shipPrefix = this.currentOrientationIsUp ? "up.ship." : "right.ship.";
			for (int i = 0, x = mousePosition.getX(), y = mousePosition.getY(); i < this.currentSelectedShipsLength; i++) {
				Tile selectedTile = null;
				if (this.currentSelectedShipsLength == 1) {
					selectedTile = AssetRegistry.getTile(shipPrefix + "single");
				} else if((i == 0 && this.currentOrientationIsUp) ||
						(i + 1 == this.currentSelectedShipsLength && !this.currentOrientationIsUp)) {
					selectedTile = AssetRegistry.getTile(shipPrefix + "bug");
				} else if((i + 1 == this.currentSelectedShipsLength && this.currentOrientationIsUp) ||
						(i  == 0 && !this.currentOrientationIsUp)) {
					selectedTile = AssetRegistry.getTile(shipPrefix + "aft");
				} else {
					selectedTile = AssetRegistry.getTile(shipPrefix + "middle");
				}
				selectedTile.renderAt(c,new Vec2(x, y));
				if (this.currentOrientationIsUp) {
					y += Constants.TILE_SIZE;
				} else {
					x += Constants.TILE_SIZE;
				}
			}
		}

		c.drawRect(width - 220, 60, 210, height - 105);
		
		if(this.backButtonScene != null) {
			c.setColor(0.7, 0.7, 0.7);
			c.fillRoundRect(width - 215, height - 75, 200, 20, 3, 3);
			c.setColor(0, 0, 0);
			c.drawString(width - 210, height - 75 + 15, "Zurück");
		}
		
		final int[] amountOfShips = new int[] {
				this.ruleset.getNumberOf1Ships(),
				this.ruleset.getNumberOf2Ships(),
				this.ruleset.getNumberOf3Ships(),
				this.ruleset.getNumberOf4Ships(),
				this.ruleset.getNumberOf5Ships()};
		int shipsToPlace = 0, y = 80;
		for (int i = 0; i < 5; i++) {
			if (amountOfShips[i] - this.placedShips[i] > 0) {
				final int shipsToPlaceInThisCategory = amountOfShips[i] - this.placedShips[i];
				c.setColor(0.7, 0.7, 0.7);
				c.fillRoundRect(width - 215, y, 200, 20, 3, 3);
				c.setColor(0, 0, 0);
				c.drawString(width - 210, y + 15, "Place a " + Integer.toString(i + 1) +
						" field long ship. (" + Integer.toString(shipsToPlaceInThisCategory) + ")");
				y += 25;
				shipsToPlace += shipsToPlaceInThisCategory;
			}
		}
		if (shipsToPlace == 0) {
			c.setColor(0.7, 0.7, 0.7);
			c.fillRoundRect(width - 215, y, 200, 20, 3, 3);
			c.setColor(0, 0, 0);
			c.drawString(width - 210, y + 15, "Continue");
		}

		if(this.showGameStartingHint) {
			c.setColor(0.7, 0.7, 0.7, 0.7);
			c.fillRect(0, 0, width, height);
			c.setColor(0, 0, 0);
			final String text = "Starting game. Please wait for the AI to place her ships.";
			final Vec2 textDim = c.getTextDimensions(text);
			c.drawString(width / 2 - textDim.getX() / 2, height / 2 - textDim.getY() / 2, text);
		}
	}

	@Override
	public void attach() {}

	@Override
	public void update(long milis) {}

	@Override
	public void detach() {}

	@Override
	public void clickedAt(Vec2 position) {
		//Abort if in game starting mode
		if(this.showGameStartingHint) {
			return;
		}

		final int x = position.getX(), y = position.getY();
		final int tileSize = Constants.TILE_SIZE;
		final int fieldWidth = this.ruleset.getGameFieldSize().getX();
		final int fieldHeight = this.ruleset.getGameFieldSize().getY();
		
		// Check for back button clicked
		if (this.backButtonScene != null && x >= this.drawnWidth - 215
				&& x <= this.drawnWidth - 215 + 200
				&& y >= this.drawnHeight - 75
				&& y <= this.drawnHeight - 75 + 20) {
			// We clicked where the back button is supposed to be
			Application.switchToScene(backButtonScene);
			Application.log("Back button clicked.");
			return;
		}

		// Check if we're positioning a ship
		if (this.currentSelectedShipsLength == 0) {
			// Now we need to check if we clicked on a ship button as we don't have a ship selected
			if (x < this.drawnWidth - 210 || x > this.drawnWidth - 10) {
				// We clicked on nothing and can skip the checks
				return;
			}
			final int[] amountOfShips = new int[] {
					this.ruleset.getNumberOf1Ships(),
					this.ruleset.getNumberOf2Ships(),
					this.ruleset.getNumberOf3Ships(),
					this.ruleset.getNumberOf4Ships(),
					this.ruleset.getNumberOf5Ships()};
			boolean continueButtonDoesNotExists = false;
			for (int i = 0, btny = 80; i < 5; i++) {
				if (amountOfShips[i] - this.placedShips[i] > 0) {
					continueButtonDoesNotExists = true;
					if (y >= btny && y <= btny + 25) {
						// We clicked on this button
						this.currentSelectedShipsLength = i + 1;
						return;
					}
					btny += 25;
				}
			}
			if (!continueButtonDoesNotExists) {
				if (y >= 80 && y <= 80 + 25) {
					startGame();
				}
			}
		} else {
			final int endOfFieldX = Math.min(this.drawnWidth - 250 + 15, 15 + fieldWidth * tileSize);
			final int endOfFieldY = Math.min(this.drawnHeight - 105 + 50, fieldHeight * tileSize + 50);
			if (x >= 15 && x <= endOfFieldX && y >= 50 && y <= endOfFieldY) {
				
				// We clicked on the field
				final int fieldX = (x - 15) / tileSize;
				final int fieldY = (y - 50) / tileSize;
				placeShipOnField(fieldX, fieldY);
			} else {
				this.currentSelectedShipsLength = 0;
			}
		}
	}

	/**
	 * Use this method in order to check and place a ship on the field
	 * @param x The initial X coordinate of the ship
	 * @param y The initial Y coordinate of the ship
	 */
	// Aufgabe 5.1.b.ii
	private void placeShipOnField(int x, int y) {

		// After doing so we need to check that the ship would fit
		if ((this.currentOrientationIsUp && y + this.currentSelectedShipsLength > this.ruleset.getGameFieldSize().getY())
		|| (!this.currentOrientationIsUp && x + this.currentSelectedShipsLength > this.ruleset.getGameFieldSize().getX())) {
			// The ship won't fit. Simply do nothing.
			return;
		}
		// Hire will use the "official_version_1" 
		if((this.ruleset.getDistance()&&!checkKeepDistance(x,y))||!this.ruleset.getDistance()) {
			
		
		// Check if the tiles are occupied
		for(int currentShipsX = x, currentShipsY = y, i = 0; i < this.currentSelectedShipsLength; i++) {
			final FieldTile t = this.field.getTileAt(currentShipsX, currentShipsY);
			if (t.getTilestate() != FieldTile.FieldTileState.STATE_WATER || t.getCorrespondingShip() != null) {
				// place already occupied
				return;
			}
			if (this.currentOrientationIsUp) {
				currentShipsY++;
			} else {
				currentShipsX++;
			}
		}

		// Then we need to create a ship and place it
		final Ship shipToPlace = new Ship(this.currentSelectedShipsLength, this.currentOrientationIsUp);
		this.field.placeShip(x, y, this.currentSelectedShipsLength, this.currentOrientationIsUp, shipToPlace);
		this.placedShips[this.currentSelectedShipsLength - 1]++;
		this.currentSelectedShipsLength = 0; 
		
		}
		// Here will use the "official_version_2"
		/**if(!this.ruleset.getDistance()) {
			
			
			// Check if the tiles are occupied
			for(int currentShipsX = x, currentShipsY = y, i = 0; i < this.currentSelectedShipsLength; i++) {
				final FieldTile t = this.field.getTileAt(currentShipsX, currentShipsY);
				if (t.getTilestate() != FieldTile.FieldTileState.STATE_WATER || t.getCorrespondingShip() != null) {
					// place already occupied
					return;
				}
				if (this.currentOrientationIsUp) {
					currentShipsY++;
				} else {
					currentShipsX++;
				}
			}

			// Then we need to create a ship and place it
			final Ship shipToPlace = new Ship(this.currentSelectedShipsLength, this.currentOrientationIsUp);
			this.field.placeShip(x, y, this.currentSelectedShipsLength, this.currentOrientationIsUp, shipToPlace);
			this.placedShips[this.currentSelectedShipsLength - 1]++;
			this.currentSelectedShipsLength = 0; 
			*/
	}
	/*
	 * 
	 * 
	 */
	private boolean checkKeepDistance(int x, int y) {
		
		boolean reasonable=false;
		
		
		//Tiles, which are direct 1 distance above and  under the current ships
		if(this.currentOrientationIsUp) {
			// Ueberprueft den Tile , der ober dem Schiffe.
			if(y-1>=0&&(this.field.getTileAt(x, y-1).getTilestate() != FieldTile.FieldTileState.STATE_WATER 
					|| this.field.getTileAt(x, y-1).getCorrespondingShip() != null)) {
				System.out.println("Cant placed at up");
				reasonable=true;
			// Ueberprueft den Tile , der auf obenrecht der Schiffe.	
			}
			if(x<15) {
			if(y-1>=0&&x<15&&(this.field.getTileAt(x+1, y-1).getTilestate() != FieldTile.FieldTileState.STATE_WATER 
					|| this.field.getTileAt(x+1, y-1).getCorrespondingShip() != null)) {
				System.out.println("Cant placed at up_right");
				reasonable=true;
				
			}
			}
			// Ueberprueft den Tile , der auf obenlinks der Schiffe.
			if(x>0) {
			if(y-1>=0&&x-1>=0&&(this.field.getTileAt(x-1, y-1).getTilestate() != FieldTile.FieldTileState.STATE_WATER 
					|| this.field.getTileAt(x-1, y-1).getCorrespondingShip() != null)) {
				System.out.println("Cant placed at up_left");
				reasonable=true;
				
			}
			}
			// Ueberprueft den Tile , der unter dem Schiffe.
			if(y+this.currentSelectedShipsLength<this.ruleset.getGameFieldSize().getY()&&
					(this.field.getTileAt(x, y+this.currentSelectedShipsLength).getTilestate() != FieldTile.FieldTileState.STATE_WATER 
					|| this.field.getTileAt(x, y+this.currentSelectedShipsLength).getCorrespondingShip() != null)) {
				System.out.println("Cant placed at under");
				reasonable=true;
				
			}
			// Ueberprueft den Tile , der auf unterrecht der Schiffe.
			if(x<15) {
			if(y+this.currentSelectedShipsLength<this.ruleset.getGameFieldSize().getY()&&
					(this.field.getTileAt(x+1, y+this.currentSelectedShipsLength).getTilestate() != FieldTile.FieldTileState.STATE_WATER 
					|| this.field.getTileAt(x+1, y+this.currentSelectedShipsLength).getCorrespondingShip() != null)) {
				System.out.println("Cant placed at under_right");
				reasonable=true;
				
			}
			}
			// Ueberprueft den Tile , der auf unterlinks der Schiffe.
			if(x>0) {
			if(y+this.currentSelectedShipsLength<this.ruleset.getGameFieldSize().getY()&&
					(this.field.getTileAt(x-1, y+this.currentSelectedShipsLength).getTilestate() != FieldTile.FieldTileState.STATE_WATER 
					|| this.field.getTileAt(x-1, y+this.currentSelectedShipsLength).getCorrespondingShip() != null)) {
				System.out.println("Cant placed at under_left");
				reasonable=true;
				
			}
			}
				
				
		}
		else {
			// Ueberprueft den Tile , der auf obenlinks der Schiffe.
			if(y>0) {
			if(x-1>=0&&(this.field.getTileAt(x-1, y-1).getTilestate() != FieldTile.FieldTileState.STATE_WATER 
					|| this.field.getTileAt(x-1, y-1).getCorrespondingShip() != null)) {
				System.out.println("Cant placed at left_up");
				reasonable=true;
			}
			}
			// Ueberprueft den Tile , der auf unterlinks der Schiffe.
			if(y<15) {
			if(x-1>=0&&(this.field.getTileAt(x-1, y+1).getTilestate() != FieldTile.FieldTileState.STATE_WATER 
					|| this.field.getTileAt(x-1, y+1).getCorrespondingShip() != null)) {
				System.out.println("Cant placed at left_under");
				reasonable=true;
				
			}
			}
			// Ueberprueft den Tile , der auf links der Schiffe.
			if(x-1>=0&&(this.field.getTileAt(x-1, y).getTilestate() != FieldTile.FieldTileState.STATE_WATER 
					|| this.field.getTileAt(x-1, y).getCorrespondingShip() != null)) {
				System.out.println("Cant placed at left");
				reasonable=true;
				
			}
			// Ueberprueft den Tile , der auf obenrecht der Schiffe.
			if(y>0) {
			if(x+this.currentSelectedShipsLength<this.ruleset.getGameFieldSize().getX()&&(this.field.getTileAt(x+this.currentSelectedShipsLength, y-1).getTilestate() != FieldTile.FieldTileState.STATE_WATER 
					|| this.field.getTileAt(x+this.currentSelectedShipsLength, y-1).getCorrespondingShip() != null)) {
				System.out.println("Cant placed at right_up");
				reasonable=true;
				
			}
			}
			// Ueberprueft den Tile , der auf unterrecht der Schiffe.
			if(y<15) {
			if(x+this.currentSelectedShipsLength<this.ruleset.getGameFieldSize().getX()&&(this.field.getTileAt(x+this.currentSelectedShipsLength, y+1).getTilestate() != FieldTile.FieldTileState.STATE_WATER 
					|| this.field.getTileAt(x+this.currentSelectedShipsLength, y+1).getCorrespondingShip() != null)) {
				System.out.println("Cant placed at right_under");
				reasonable=true;
				
			}
			}
			// Ueberprueft den Tile , der auf recht der Schiffe.
			if(x+this.currentSelectedShipsLength<this.ruleset.getGameFieldSize().getX()&&(this.field.getTileAt(x+this.currentSelectedShipsLength, y).getTilestate() != FieldTile.FieldTileState.STATE_WATER 
					|| this.field.getTileAt(x+this.currentSelectedShipsLength, y).getCorrespondingShip() != null)) {
				System.out.println("Cant placed at right");
				reasonable=true;
				
			}
		}
			
		
		
		
		//Other tiles, which are within 1 distance near by the current ship
		for(int currentShipsX = x, currentShipsY = y, i = 0; i < this.currentSelectedShipsLength; i++) {
			
			
			if(this.currentOrientationIsUp&&currentShipsX-1>=0&&currentShipsX+1<this.ruleset.getGameFieldSize().getX()&&currentShipsY-1>=0) {
				
				// Ueberprueft den Tile , der auf links der Schiffe.
				final FieldTile t1 = this.field.getTileAt(currentShipsX-1, currentShipsY);
				
				// Ueberprueft den Tile , der auf recht der Schiffe.
				final FieldTile t2 = this.field.getTileAt(currentShipsX+1, currentShipsY);
				
				
				
				
					if (t1.getTilestate() != FieldTile.FieldTileState.STATE_WATER || t1.getCorrespondingShip() != null
					  ||t2.getTilestate() != FieldTile.FieldTileState.STATE_WATER || t2.getCorrespondingShip() != null) {
						System.out.println("Cant placed at left or right");
						reasonable=true;
						break;
					}
				
				currentShipsY++;
			
			}
			
			else if(!this.currentOrientationIsUp&&currentShipsY-1>=0&&currentShipsY+1<this.ruleset.getGameFieldSize().getY()&&currentShipsX-1>=0) {
				
				// Ueberprueft den Tile , der auf oben der Schiffe.
				final FieldTile t1 = this.field.getTileAt(currentShipsX, currentShipsY-1);
				
				// Ueberprueft den Tile , der auf unter der Schiffe.
				final FieldTile t2 = this.field.getTileAt(currentShipsX, currentShipsY+1);
				
				if (t1.getTilestate() != FieldTile.FieldTileState.STATE_WATER || t1.getCorrespondingShip() != null
				  ||t2.getTilestate() != FieldTile.FieldTileState.STATE_WATER || t2.getCorrespondingShip() != null) {
					System.out.println("Cant placed at up or under");
					reasonable=true;
					break;
				}
				
				currentShipsX++;
				
			}
			
		}
		
		
		
		return reasonable;
	}

	private void startGame() {
		if(this.showGameStartingHint)
			return;
		this.showGameStartingHint = true;
		final long startTimer = System.currentTimeMillis();
		Application.log("Starting game...");
		final Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				final AIAgent ag;
				switch (aiHardness) {
					case 0:
					default:
						ag = new EasyAIAgent(0);
						break;
					case 1:
					case 2:
						ag=new BetterAIAgent(0);
						//throw new RuntimeException("Dieser Schwierigkeitsgrad wurde von dir noch nicht implementiert.");
				}
				final GameField agentsField = new GameField(ruleset.getGameFieldSize());
				ag.setup(ruleset, agentsField);
				final GameModel m = new GameModel(field, agentsField, ruleset, ag, playerNames[0], playerNames[1]);
				final GameScene s = new GameScene(m);
				Application.switchToScene(s);
				Application.log("Game setup occurred within " + (System.currentTimeMillis() - startTimer) + "ms.");
			}
		});
		t.setName("Game_Creation_Thread");
		t.start();
	}

	@Override
	public void keyPressed(char key, boolean shift, boolean alt, boolean ctrl, boolean down, boolean up, boolean left,
			boolean right) {
		if (key == 'R' || key == 'r') {
			this.currentOrientationIsUp = !this.currentOrientationIsUp;
		}
	}

}