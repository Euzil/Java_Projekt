package de.uniluebeck.itm.schiffeversenken.game;

import de.uniluebeck.itm.schiffeversenken.engine.*;
import de.uniluebeck.itm.schiffeversenken.game.model.*;

/**
 * Game field rendering methods.
 *
 * It only contains methods designated to rendering a game field. In future it will also provide scroll bars in case the
 * field doesn't fit on the canvas.
 * modified by Gruppe 107 Youran Wang (719511)
 */
public class GameFieldRenderer {

    private final GameField field;

    public GameFieldRenderer(GameField field) {
        this.field = field;
    }

    /**
     * Use this method in order to render a game field.
     *
     * @param c The canvas to render on.
     * @param x The x coordinate where to render the field.
     * @param y The y coordinate where to render the field.
     */
    public void renderGameField(Canvas c, int x, int y) {
        final int tileSize = Constants.TILE_SIZE;

        // Look up the default water tiles
        final Tile waterTile = AssetRegistry.getTile("water");
        final Tile waterMissed = AssetRegistry.getTile("water.hit");

        // draw the fields tiles
        for (int tileX = 0; tileX < this.field.getSize().getX(); tileX++) {
            for (int tileY = 0; tileY < this.field.getSize().getY(); tileY++) {
                final Vec2 tilePosition = new Vec2(x + tileX * tileSize, y + tileY * tileSize);
                getTileAt(tileX, tileY, waterTile, waterMissed).renderAt(c, tilePosition);
            }
        }
        
        final int width = tileSize * this.field.getSize().getX();
        final int height = tileSize * this.field.getSize().getY();

        // TODO: Draw a border around the field
        c.setColor(1, 1, 1);                                                       // Ersetzt die Farbe zum Weisse
        c.drawLine(new Vec2(x, y), new Vec2(x + width, y));                        // Zeichnen die oben Linien
        c.drawLine(new Vec2(x, y), new Vec2(x, y + height));                       // Zeichnen die Links Linien
        c.drawLine(new Vec2(x + width, y), new Vec2(x + width, y + height));       // Zeichnen die rechts Linien
        c.drawLine(new Vec2(x, y + height), new Vec2(x + width, y + height));      // Zeichnen die unter Linien
        
        // TODO: draw lines as border between cell
        c.setColor(0, 0, 0);                                                       // Ersetzt die Farbe zum schwarz
        for (int j = 1; j < this.field.getSize().getX(); j++) {                    // Zeichnen die Vertikal Linien mit Bestand tileSize.
            c.drawLine(new Vec2(x + j*tileSize, y), new Vec2(x + j*tileSize, y + this.field.getSize().getY()*tileSize));
                }
        for (int k = 1; k < this.field.getSize().getY(); k++) {                    // Zeichnen die horizontal Linien mit Bestand tileSize.
            c.drawLine(new Vec2(x, y + k*tileSize), new Vec2(x + this.field.getSize().getY()*tileSize, y + k*tileSize));
                }     
    }

    /**
     * This method gets called by the rendering method in order to look up the correct tile at a given position.
     * @param x The x coordinate of the tile to look up
     * @param y The y coordinate of the tile to look up
     * @param waterTile A cached version for a water tile (will be the most returned one)
     * @param waterHitTile A cached version of the water_missed tile (Will be the second most returned one)
     * @return The correct tile to render
     */
    protected Tile getTileAt(int x, int y, Tile waterTile, Tile waterHitTile) {
        switch(this.field.getTileAt(x, y).getTilestate()) {
            default:
                return AssetRegistry.getTile("unknown");
            case STATE_WATER:
                return waterTile;
            case STATE_MISSED:
                return waterHitTile;
            case STATE_SHIP:
                return lookupShipTile(x, y, false);
            case STATE_SHIP_HIT:
                return lookupShipTile(x, y, true);
        }
    }

    /**
     * This method looks up the correct tile if the position happens to be a ship.
     * @param x The x coordinate of the ships tile
     * @param y The y coordinate of the ships tile
     * @param alreadyHit A Flag that indicates whether nor not the tile has been hit yet.
     * @return The composed ships tile
     */
    private Tile lookupShipTile(int x, int y, boolean alreadyHit) {
    	Tile tileKeys=null;
    	// Aufgabe 2.3.b
    	Ship ship=this.field.getTileAt(x, y).getCorrespondingShip(); // Die Position einer Schife geben. 
    	// Aufgabe 2.3.c
    	boolean oben=false;    // Ob eine Schife oben zu der Position (x,y), und mit false initialisieren
    	boolean unter=false;  // Ob eine Schife unter zu der Position (x,y), und mit false initialisieren
    	boolean recht=false; // Ob eine Schife rechts zu der Position (x,y), und mit false initialisieren
    	boolean links=false;  // Ob eine Schife links zu der  Position (x,y), und mit false initialisieren
    	
    	// Ueberpruefen die umliegenden Felder(x,y)
    	
    	if(y-1>=0&&this.field.getTileAt(x, y-1).getCorrespondingShip()==ship) {
    		oben=true;     // Wenn eine Schife oben zu der Position (x,y) liegt
    	} 
    	
    	if(y+1<this.field.getSize().getY()&&this.field.getTileAt(x, y+1).getCorrespondingShip()==ship) {
    		unter=true;   // Wenn eine Schife unter zu der Position (x,y) liegt
    	}
    	
    	if(x+1<this.field.getSize().getX()&&this.field.getTileAt(x+1, y).getCorrespondingShip()==ship) {
    		recht=true; // Wenn eine Schife recht zu der Position (x,y) liegt
    	}
    	
    	if(x-1>=0&&this.field.getTileAt(x-1, y).getCorrespondingShip()==ship) {
    		links=true;   // Wenn eine Schife links zu der Position (x,y) liegt
    	}
    	// Aufgabe 2.3.d
    	if(alreadyHit==false) {  // Wenn die Schife noch nicht getroffen		
    		tileKeys=ship.isUp()?oben?unter? 
    		//(oben=true && unter=true )
    		AssetRegistry.getTile("up.ship.middle"):                  // Nachbar darueber und darunter
    		//(up=true && down=false)
    		AssetRegistry.getTile("up.ship.aft"):unter?               // Nur Nachbarn darueber
    		//(oben=false && unter=true )  
    		AssetRegistry.getTile("up.ship.bug"):                     // Nur Nachbarn darunter
    		//(oben=false && unter=false )
			AssetRegistry.getTile("up.ship.single"):recht?links?      // Keine Nachbarn im vertical Richtung
			//(recht=true && links=true )
			AssetRegistry.getTile("right.ship.middle"):               // Nachbarn links und rechts
			//(recht=true && links=false )	
			AssetRegistry.getTile("right.ship.aft"):links?            // Nur rechts Nachbarn
			//(recht=false && links=true )
			AssetRegistry.getTile("right.ship.bug"):                  // Nur links Nachbarn
			//(recht=false && links=false )
			AssetRegistry.getTile("right.ship.single");      	      // Keine Nachbarn im horizontal Richtung
    		
    	// Aufgabe 2.3.e
    	}else {                // Wenn die Schife bereits getroffen
    		tileKeys=ship.isUp()?oben?unter? 
    	    //(oben=true && unter=true )
    	    AssetRegistry.getTile("up.ship.middle.hit"):             // Nachbar darueber und darunter
    	    //(up=true && down=false)
    	    AssetRegistry.getTile("up.ship.aft.hit"):unter?          // Nur Nachbarn darueber
    	  	//(oben=false && unter=true )
    	    AssetRegistry.getTile("up.ship.bug.hit"):                // Nur Nachbarn darunter
    	    //(oben=false && unter=false )
    		AssetRegistry.getTile("up.ship.single.hit"):recht?links? // Keine Nachbarn im vertical Richtung
    		//(recht=true && links=true )
    		AssetRegistry.getTile("right.ship.middle.hit"):          // Nachbarn links und rechts
    		//(recht=true && links=false )	
    		AssetRegistry.getTile("right.ship.aft.hit"):links?       // Nur rechts Nachbarn
    		//(recht=false && links=true )
    		AssetRegistry.getTile("right.ship.bug.hit"):             // Nur links Nachbarn
    		//(recht=false && links=false )
    		AssetRegistry.getTile("right.ship.single.hit");      	 // Keine Nachbarn im horizontal Richtung
    	} 
        return tileKeys;
    }
 
    /**
     * The purpose of this method is to enable the usage of the game field on expanding classes.
     * @return The game field
     */
    protected GameField getField() {
        return this.field;
    }
        //Aufgabe 4.1
        public void renderMouseOver(Canvas c , int x ,  int y , int fieldX , int fieldY){
            // Die Gross des gegnerischen Felde. 
      	    final int width =  Constants.TILE_SIZE* this.field.getSize().getX();
            final int height = Constants.TILE_SIZE * this.field.getSize().getY();                
             // Aufgabe 4.1.a.ii
             // Der Maus muss nur auf gegnerischen Felde. 
             if(x>=fieldX&&x<fieldX+width&&y>=fieldY&&y<fieldY+height) {
             // Aufgabe 4.1.a.i
             // Die der Spalten-und Zeilenanzahl des Maus auf gegnerischen Felde. 
          	 int tileX = (x-fieldX)/ Constants.TILE_SIZE;          	 
          	 int tileY = (y-fieldY)/ Constants.TILE_SIZE; 
          	 // Aufgabe 4.1.a.iii
          	 // Es koennte die alle Punkt eines Tile auf eine Punkt diffiniert werden. (auf dem ObenLinksEcke diffinieren)
          	 int posX=tileX*Constants.TILE_SIZE+fieldX;         	 
          	 int posY=tileY*Constants.TILE_SIZE+fieldY;
          	 //Aufgabe 4.1.a.iv
             c.setColor(0.3, 0.3 ,0.3 , 0.5);
             // fillRect(PositionX , PositionY , Hoehe , Bereit)
             c.fillRect(posX, posY, Constants.TILE_SIZE, Constants.TILE_SIZE);                         	  
             final Tile arrowTile = AssetRegistry.getTile("arrow.down");                          
             arrowTile.renderAt(c, new Vec2(posX+10 , posY-10));                
            }            
            else {         	  
          	  return;
            }
    }

}
