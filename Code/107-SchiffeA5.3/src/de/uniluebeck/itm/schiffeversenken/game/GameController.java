package de.uniluebeck.itm.schiffeversenken.game;

import de.uniluebeck.itm.schiffeversenken.engine.Application;
import de.uniluebeck.itm.schiffeversenken.engine.Controller;
import de.uniluebeck.itm.schiffeversenken.engine.Vec2;
import de.uniluebeck.itm.schiffeversenken.game.menues.EndOfGameMenu;
import de.uniluebeck.itm.schiffeversenken.game.model.FieldTile;
import de.uniluebeck.itm.schiffeversenken.game.model.GameModel;
import de.uniluebeck.itm.schiffeversenken.game.model.Ship;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * modified by Gruppe 107 Youran Wang (719511)
 */
public class GameController extends Controller<GameModel> {

    public GameController(GameModel m) {
        super(m);
    }

    @Override
    public void clickedAt(Vec2 mousePosition) {
        final GameModel model = this.getModelInstance();
        final Vec2 positionOnOpponentsField = mousePosition.add(model.getOpponentsFieldPosition().multiply(-1));
        final Vec2 opponentsFieldDimensions = model.getOpponentsFieldDimensions();

        // First of all we need to test if the player clicked within the field
        if(positionOnOpponentsField.getX() >= 0 && positionOnOpponentsField.getY() >= 0
                && positionOnOpponentsField.getX() < opponentsFieldDimensions.getX()
                && positionOnOpponentsField.getY() < opponentsFieldDimensions.getY()) {
            final int res = Constants.TILE_SIZE;
            
            // TODO implement a way to get the field coordinates 
            // Aufgabe 3.2.a
            // positionOnOpponentsField() / Tileslaenge = Teilsposition(tileX, tileY)
            final int tileX = positionOnOpponentsField.getX()/res;
            final int tileY = positionOnOpponentsField.getY()/res;
            Application.log("Bombarding position " + tileX + ", " + tileY); // Es wird im Konsole ausgedurkt.
            final FieldTile fieldTile = model.getComputerPlayerField().getTileAt(tileX, tileY); // FieldTile auf gegenesfield marken.                      
            if (!fieldTile.wasAlreadyBombarded()) {
                if(fieldTile.bombard()) {               	
                 // TODO: the player hit something: do something with this information 
                 // Aufgabe 3.2.b
                 //Wenn man etwas getroffen hat, kann er Punkte = 50 bekommen
               	 model.addPlayerPoints(Constants.POINTS_FOR_HIT);               	                	 
               	 if(fieldTile.getCorrespondingShip()!=null && fieldTile.getCorrespondingShip().isSunken()) {
               	 // Wenn das Schiff gesunken ist, dann kann man Punkte =150 hinzukommen
               	 model.addPlayerPoints(Constants.POINTS_FOR_SHIP_SUNK); 
               	 }               	 
               	 // die Methode handlePossibleGameEnd() aufgerufen werden. Es geht um die Pruefen, ob das Spiel enden.
               	 handlePossibleGameEnd();  
               	 
                 } else {
                    model.setRoundChangingFlag(true);
                    this.dispatchWork(new Runnable() {
                        @Override
                        public void run() {
                            while (model.getAgent().performMove(model.getHumanPlayerField())) {
                                rewardAgentForDestroyingPlayer();
                            }
                        }
                        private void rewardAgentForDestroyingPlayer() {
                        	// Aufgabe 3.2.c
                        	//Wenn Computer etwas getroffen hat, kann es Punkte = 50 bekommen
                            model.addAiPoints(Constants.POINTS_FOR_HIT);
                            final Ship s = model.getAgent().getLastAttackedTile().getCorrespondingShip();
                            if(s != null && s.isSunken()) {                           	
                            // TODO perform the same checks for the computer player.
                            // Wenn das Schiff gesunken ist, dann kann es Punkte =150 hinzukommen
                            model.addAiPoints(Constants.POINTS_FOR_SHIP_SUNK);
                            }
                         // die Methode handlePossibleGameEnd() aufgerufen werden. Es geht um die Pruefen, ob das Spiel enden.
                            handlePossibleGameEnd();
                        }
                    });
                    this.startWorkStack();
                    
                  
                 }
            }
        }
    }
    
    private void handlePossibleGameEnd() {
    	// Aufgabe 3.2.d
        // TODO check if the game ended
    	// Speichen wir alle Schiffe im einem Array Ship[]
    	// Aufgabe 3.2.d.i
        Ship[] humanShip=this.getModelInstance().getHumanPlayerField().getCopyOfShipListAsArray();    	
    	Ship[] computerShip=this.getModelInstance().getComputerPlayerField().getCopyOfShipListAsArray();  
    	// Aufgabe 3.2.d.iii
    	// Computer won   	 
    	if(CheckAllShipsSunk(humanShip)) {   		
    		// Mit Methode PrueftAllShip kann man pruefen, ob die alle Schiffe gesunkt.
    		// Wenn alle Schiffe der Spieler gesunkt hat, dann wird Computer gewonnen. Dann false vom endGame ausgeben.
    		endGame(false);
    	}   	
    	// Spieler won
    	if(CheckAllShipsSunk(computerShip)) {  
    		// Mit Methode PrueftAllShip kann man pruefen, ob die alle Schiffe gesunkt.
    		// Wenn alle Schiffe der Computer gesunkt hat, dann wird Spieler gewonnen. Dann true vom endGame ausgeben.
    		endGame(true);
    	}    	
    }
        // Aufgabe 3.2.d.ii
        private boolean CheckAllShipsSunk(Ship[] ship) {   	
    	        boolean checkAllShipsSunk=true;    	
    	              for(int i=0;i<ship.length;i++) {
    	            	  // Wenn diese Methode geruft wird, wird die Array Ship[] iterieren. Es prueft, ob jede Schiffe gesunkt hat. 
    		              if(!ship[i].isSunken()) {
    		            	 // Wenn eine davon nicht gesunkt wird, dann wird das false ausgeben
    		            	 checkAllShipsSunk=false;
    			             break;
    		          }
    	        }    	
		return checkAllShipsSunk;    	
    }

    /**
     * Use this function in order to end the game and display the EndOfGameMenu.
     * @param playerWon Pass true if the player won and false if the computer won.
     */
    private void endGame(boolean playerWon) {
        Application.log("Ending game.");
        Application.switchToScene(new EndOfGameMenu(this.getModelInstance(), playerWon).getScene());
    }

    @Override
    public void keyPressed(int key, boolean shift, boolean alt, boolean ctrl, boolean down, boolean up, boolean left, boolean right) {
        // Doesn't do anything anymore since we've dropped multiple resolutions
    }

    @Override
    public void performFrequentUpdates() {
        if (!this.hasWork() && this.getModelInstance().isRoundChanging()) {
            // We need to assume that the round changing work has finished.
            this.getModelInstance().increaseRoundCounter();
            this.getModelInstance().setRoundChangingFlag(false);
        }
    }

    @Override
    public void prepare() {

    }
}


