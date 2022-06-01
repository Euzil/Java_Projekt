package de.uniluebeck.itm.schiffeversenken.game.model;

/**
 * This class represents a ship.
 * @author leondietrich, modified by I. Schumacher, modified by Gruppe 107 Youran Wang (719511)
 *
 */
/**
 * Aufgabe1.1 a)
 * Hier brauchen wir 3 privaten Variablen
 */
public class Ship {                    
	private int length;               
	private int hits;
	private boolean orientation;
	
	/**
	 * This constructor constructs a new ship.
	 * lenght The length of the ship to create.
	 * @param orientation The orientation of the ship: true for vertical, false for horizontal
	 * Aufgabe1.1 b)
	 */
	public Ship(int length, boolean orientation) {   
	 this.length=length;             
	 this.orientation=orientation;
	 this.hits=0;
	}
	
	/**
	 *Aufgabe1.1 d)
	 *Jeder Treffe macht Anzahl der Hits plus 1
	 */
	void hit() {                     
		if(!isSunken()) {             
		this.hits=this.hits+1;
		}
	}

	/**
	 * Aufgabe1.1 c)
	 * Wenn die Anzahl des Hits gleich wie die Laenge des Schiff ist, bedeutet das Schiff bereits gesunken sein. 
	 * @return ob das gesunken
	 */
	public boolean isSunken() {       
		if(this.length==this.hits) {  
			return true;
		}else 
		    return false;  
	}
	/**
	 * Aufgabe1.1 e)
	 * Es ist eine get()Methode vom Orientation
	 * @return boolean des schiff ist vertical oder nicht
	 */
	public boolean isUp() {         
	return this.orientation;         
			
	}

}
