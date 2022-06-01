/**
 * @author Gruppe 107 Youran Wang (719511)
 */
//Aufgabe3.1.a
package de.uniluebeck.itm.schiffeversenken.game;

import de.uniluebeck.itm.schiffeversenken.engine.AssetRegistry;
import de.uniluebeck.itm.schiffeversenken.engine.Tile;
import de.uniluebeck.itm.schiffeversenken.game.model.GameField;

public class HitMissRenderer extends GameFieldRenderer{

	public HitMissRenderer(GameField field) {
		super(field);
		
	}
	 /**
     * This method gets called by the rendering method in order to look up the correct tile at a given position.
     * @param x The x coordinate of the tile to look up
     * @param y The y coordinate of the tile to look up
     * @param waterTile A cached version for a water tile (will be the most returned one)
     * @param waterHitTile A cached version of the water_missed tile (Will be the second most returned one)
     * @return The correct tile to render
     */
	// Aufgabe3.1.b
    protected Tile getTileAt(int x, int y, Tile waterTile, Tile waterHitTile) {
        // Hier bekmmen wir die Status des Field. Das Status kommt aus StatusListe und im klasse GameFieldRenderer geprueft.
        switch(getField().getTileAt(x, y).getTilestate()) {
            default:
            	// keine gegebene Status 
                return AssetRegistry.getTile("unknown");
            // Der Field hat noch nicht angegriffen werden.
            case STATE_WATER:
                return waterTile;
            // Der Field hat angegriffen werden, aber keine Schiffe hier.
            case STATE_MISSED:
                return waterHitTile;
            // Der Field hat noch nicht angegriffen werden, aber hier hat eine Schiffe.
            case STATE_SHIP:
                return waterTile;
            // Der Field hat angegriffen werden, und eine Schiffe getroffen wird.
            case STATE_SHIP_HIT:
            	return AssetRegistry.getTile("water.hiddenshiphit");
        }
    }
}
