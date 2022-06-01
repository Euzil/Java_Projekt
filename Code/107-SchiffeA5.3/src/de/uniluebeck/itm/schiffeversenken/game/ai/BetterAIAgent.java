package de.uniluebeck.itm.schiffeversenken.game.ai;

import java.util.Random;

import de.uniluebeck.itm.schiffeversenken.game.model.FieldTile;
import de.uniluebeck.itm.schiffeversenken.game.model.GameField;
import de.uniluebeck.itm.schiffeversenken.game.model.Ruleset;
import de.uniluebeck.itm.schiffeversenken.game.model.Ship;
import de.uniluebeck.itm.schiffeversenken.game.model.FieldTile.FieldTileState;


/**
 
 * @author Gruppe 107 Youran Wang (719511)
 *
 */
public class BetterAIAgent extends AIAgent {

	
	private FieldTile lastTile;
	private int lastX;
	private int lastY;
	
	public BetterAIAgent(int hardness) {
		super(hardness);
		
	}

	 @Override
	    public void setup(Ruleset r, GameField agentsField) {
	        this.placeShipsAccordingToRules(r, agentsField);
	    }

	    @Override
	    public boolean performMove(GameField playersField) {
	    	
	    	FieldTile tile;
	        final int fieldWidth = playersField.getSize().getX();
	        final int fieldHeight = playersField.getSize().getY();
	        // If the last Attack was a Hit.
			if(getLastAttackedTile()!=null && getLastAttackedTile().getTilestate()==FieldTileState.STATE_SHIP_HIT && lastX-1>0 && lastX+1<fieldWidth && lastY-1>0 && lastY<fieldHeight) {
				//it will set the underTile as the next attack	
	        	tile=playersField.getTileAt(lastX, lastY+1);
	        	this.lastY=lastY+1;
	        	this.lastTile=tile;
	        		        	
	        }
			// otherweise will do a random attack 
	        else {
	        	final Random rnd = new Random(System.currentTimeMillis());
	        	
	        do {
	        	int x=rnd.nextInt(fieldWidth);
	        	int y=rnd.nextInt(fieldHeight);
	     
	        	lastX=x;
	        	lastY=y;
	        	
	            tile = playersField.getTileAt(x,y);
	        } while (tile.wasAlreadyBombarded());
	        
	        this.lastTile = tile;
	        
	        }
	        return tile.bombard();
	    }
	    

	    @Override
	    public FieldTile getLastAttackedTile() {
	        return this.lastTile;
	    }

	/**
     * Call this method from within a setup method in order to conviniently place your ships.
     * @param r The rule set to obey
     * @param f The field to place the ships on
     */
	    /**
	    protected void placeShipsAccordingToRules (Ruleset r, GameField f) {
	        
	        final int width = r.getGameFieldSize().getX();
	        final int height = r.getGameFieldSize().getY();

	        final int[] shipsToBePlaced = new int[] {
	                r.getNumberOf1Ships(),
	                r.getNumberOf2Ships(),
	                r.getNumberOf3Ships(),
	                r.getNumberOf4Ships(),
	                r.getNumberOf5Ships()};

	        final Random rnd = new Random(System.currentTimeMillis());
	        
	        final boolean distance=r.getDistance();
	  
	        for (int shipsLenghtIndex = 0; shipsLenghtIndex < shipsToBePlaced.length; shipsLenghtIndex++) {
	            for (int ship = 0; ship < shipsToBePlaced[shipsLenghtIndex]; ship++) {
	            	// Here will use the "official_version_1"
	            	if(distance) {
	            	// Hier suchen Wir eine neue Position und durch Methode checkAndPlace geprueft wird.
	                while (!checkAndPlace(f, rnd.nextBoolean(), rnd.nextInt(width),
	                        rnd.nextInt(height), shipsLenghtIndex + 1, width, height,distance));
	                System.out.println("new Position");
	                
                    // Here will use the "official_version_2"
	                }else {
	                // Hier suchen Wir eine neue Position und durch Methode checkAndPlace geprueft wird.
	                while (!checkAndPlace2(f, rnd.nextBoolean(), rnd.nextInt(width),
	                         rnd.nextInt(height), shipsLenghtIndex + 1, width, height,distance));
	                 System.out.println("new Position");
	            }
	            }
	        }
	    }

	    private boolean checkAndPlace(GameField f, boolean up, int x, int y, int length, int width, int height, boolean distance) {
	    	// Die Positon beschribt als (x,y)
	    	System.out.println(x+" "+y);
	    	// Ob die Position ausser dem Field
	        if ((up && y + length > height) || (!up && x + length > width)) {
	        	
	            return false;
	        }
	        // Jeder Tile der Schiffe wird geprueft
	        for(int currentShipsX = x, currentShipsY = y, i = 0; i < length; i++) {
	          
	        		
	        	
	        	// Ob diese Tile wird  beim andere Schiffe besitzt.
	        	 final FieldTile t = f.getTileAt(currentShipsX, currentShipsY);
	        	 if (t.getTilestate() != FieldTile.FieldTileState.STATE_WATER || t.getCorrespondingShip() != null) {
	        		 System.out.println("because of Tile");
	                 return false;
	             }
	        	 
	        	 // Ein Tile , die nicht am die erste Spalte ist, hat eine links Tile im Field.
	             if(currentShipsX!=0 ) {         
	            	System.out.println("at left");
	             	final FieldTile t_left = f.getTileAt(currentShipsX-1, currentShipsY);
	               // Ob links Tile wird  beim andere Schiffe besitzt.
	             	if (t_left.getTilestate() != FieldTile.FieldTileState.STATE_WATER || t.getCorrespondingShip() != null) { 
	             		System.out.println("because of left");
	                     return false;
	                 }
	                 // Ein Tile , die nicht am die erst Zeile und auch nicht am erst Spalte ist, hat eine obenelinks Tile im Field.
	             	 if(currentShipsY!=0) {
	                    System.out.println("at up_left");
	             		final FieldTile t_up_left = f.getTileAt(currentShipsX-1, currentShipsY-1); 
	             		if (t_up_left.getTilestate() != FieldTile.FieldTileState.STATE_WATER || t.getCorrespondingShip() != null) { 
	                 		System.out.println("because of up_left");
	                         return false;
	             	 } 
	             	 }
	             		
	             	 // Ein Tile , die nicht am die erst Zeile und auch nicht am letzte Spalte ist, hat eine obenerechts Tile im Field.
	             	 if(currentShipsY!=15) {
	             		System.out.println("at down_left");
	             		final FieldTile t_down_left = f.getTileAt(currentShipsX-1, currentShipsY+1); 
	             		if (t_down_left.getTilestate() != FieldTile.FieldTileState.STATE_WATER || t.getCorrespondingShip() != null) { 
	                 		System.out.println("because of down_left");
	                         return false;
	             	 } 
	             	 }
	             	 
	             	 
	             // Ein Tile , die nicht am die letzte Spalte ist, hat eine rechte Tile im Field.
	             if(currentShipsX!=15) {
	            	 System.out.println("at right");
	            	 final FieldTile t_right = f.getTileAt(currentShipsX+1, currentShipsY);
	            	// Ob recht Tile wird  beim andere Schiffe besitzt.
	              	 if (t_right.getTilestate() != FieldTile.FieldTileState.STATE_WATER || t.getCorrespondingShip() != null) {
	              		System.out.println("because of right");
	                      return false;
	                  }
	                 // Ein Tile , die nicht am die erst Zeile und auch nicht am erst Spalte ist, hat eine obenelinks Tile im Field.
	             	 if(currentShipsY!=0) {
	                    System.out.println("at up_right");
	             		final FieldTile t_up_right = f.getTileAt(currentShipsX+1, currentShipsY-1); 
	             		if (t_up_right.getTilestate() != FieldTile.FieldTileState.STATE_WATER || t.getCorrespondingShip() != null) { 
	                 		System.out.println("because of up_right");
	                         return false;
	             	 } 
	             	 }
	             		
	             	 // Ein Tile , die nicht am die erst Zeile und auch nicht am letzte Spalte ist, hat eine obenerechts Tile im Field.
	             	 if(currentShipsY!=15) {
	             		System.out.println("at down_right");
	             		final FieldTile t_down_right = f.getTileAt(currentShipsX+1, currentShipsY+1); 
	             		if (t_down_right.getTilestate() != FieldTile.FieldTileState.STATE_WATER || t.getCorrespondingShip() != null) { 
	                 		System.out.println("because of down_right");
	                         return false;
	             	 } 
	             	 }
		 
	             }
	             // Ein Tile , die nicht am die erst Zeile ist, hat eine obene Tile im Field.
	             if(currentShipsY!=0) {
	            	 System.out.println("at top");
	            	 final FieldTile t_under = f.getTileAt(currentShipsX, currentShipsY-1);
	            	// Ob obene Tile wird  beim andere Schiffe besitzt.
	              	if (t_under.getTilestate() != FieldTile.FieldTileState.STATE_WATER || t.getCorrespondingShip() != null) { 
	              		System.out.println("because of top");
	                      return false;
	                  }        
	             }
	             // Ein Tile , die nicht am die letzte Zeile ist, hat eine unter Tile im Field
	             if(currentShipsY!=15) {
	            	System.out.println("at under");
	            	final FieldTile t_up = f.getTileAt(currentShipsX, currentShipsY+1);
	            	// Ob unter Tile wird  beim andere Schiffe besitzt.
	              	if (t_up.getTilestate() != FieldTile.FieldTileState.STATE_WATER || t.getCorrespondingShip() != null) {
	              		System.out.println("because of under");
	                      return false;
	                  }        
	             }
	            
	            if(i+1<length) {
	            // zum naechste Tile geprueft wird
	            if (up) {
	                currentShipsY++;
	                System.out.println("next Tile");
	            } else {
	                currentShipsX++;
	                System.out.println("next Tile");
	            }
	          }

	        }
	        }

	        final Ship shipToPlace = new Ship(length, up);
	        f.placeShip(x, y, length, up, shipToPlace);
	        System.out.println("place");
	        return true;
	    
			
	    }
	    
	    private boolean checkAndPlace2(GameField f, boolean up, int x, int y, int length, int width, int height, boolean distance) {
	    	// Die Positon beschribt als (x,y)
	    	System.out.println(x+" "+y);
	    	// Ob die Position ausser dem Field
	        if ((up && y + length > height) || (!up && x + length > width)) {
	        	
	            return false;
	        }
	        // Jeder Tile der Schiffe wird geprueft
	        for(int currentShipsX = x, currentShipsY = y, i = 0; i < length; i++) {
	          
	        		
	        	
	        	// Ob diese Tile wird  beim andere Schiffe besitzt.
	        	 final FieldTile t = f.getTileAt(currentShipsX, currentShipsY);
	        	 if (t.getTilestate() != FieldTile.FieldTileState.STATE_WATER || t.getCorrespondingShip() != null) {
	        		 System.out.println("because of Tile");
	                 return false;
	             }
	    }

	        final Ship shipToPlace = new Ship(length, up);
	        f.placeShip(x, y, length, up, shipToPlace);
	        System.out.println("place");
	        return true;
	}
	*/
	}

