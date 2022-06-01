package de.uniluebeck.itm.schiffeversenken.main;
/**
 *@author Gruppe 107 Youran Wang (719511)
 */
import java.util.Arrays;
import java.util.Scanner;
public class MatrixTest {                   // Aufgabe1.2 a)
	 
	 int Spalten[]= new int [10];
	 int Zeilen[]= new int [10];             // Aufgabe1.2 b)
	 int field[][]=new int [10][10];
	
	public static void main(String[] args) {   // Aufgabe1.2 h)
		// TODO Auto-generated method stub
		/**
		 * Hier wird alle Methoden aufgeruft
		 */
		 MatrixTest Objekt=new MatrixTest();
		 Objekt.intialisieren();
		 Objekt.print();	
		 Objekt.transponieren();
		 Objekt.print();		 
		 Objekt.Hauptdiagonalen();
		 Objekt.print();
		 Objekt.Spaltensummen();
		 Objekt.ErstZeile();
		 Objekt.print();
}
	/**
	 * Aufgabe1.2 d)
	 * Zeichnen der Matrix
	 */
	 public void print() {                                                     
		 for (int k = 0; k < field.length;k++) {
	            for (int j = 0; j < field[k].length; j++){
	          	  System.out.print(String.format("%02d", field[k][j]) + " ");  // Die Zahl wird wie eine String ausgegeben. Dann koennet 0 auf 00 auswerfen
	            }
	            System.out.println();
	    	}
		System.out.println();
	 }
	 //Hier habe ich noch eine Weg Um 00 sondern nicht einfach 0 auszugeben.
	 /**
	 if(field[k][j]<10) {
		System.out.print("0"+field[k][j]);
	} else {
	System.out.print(field[k][j]);
	}
	**/
	 
	 /**
	  * Aufgabe1.2 c)	
	  * Intialisieren eine 2D-Array
	  */
	 public void intialisieren() {                 		
	  for(int k=0;k<10;k++) {                    
		    Spalten[k]=k;
	  for(int j=0;j<10;j++) {
			Zeilen[j]=j;
			field[k][j]=10*Zeilen[j]+Spalten[k];	
		}	
	}
    }
	 /**
	  * Aufgabe1.2 d)
	  * Zeilen und Spalten vertauschen
	  */
	 public void transponieren() {                 
		int trans[][] = new int [10][10];		   //Hier benutzte ich ein Mask. Dass die Matrix Trans wird zuerst transponieren, dann wird es wieder zurueck auf Field laden.
		for(int j=0;j<10;j++) {
			for(int k=0;k<10;k++) {
				trans[j][k]=field[k][j];
			}			
		}	
		  for(int k=0;k<10;k++) {
			for(int j=0;j<10;j++) {
				field[k][j]=trans[k][j];
			}
		}
	//Hier kann ich nicht bestimmen, dass ich der Marix Field veraenderen soll, oder ein neuen Matrix erstellen.
		   /*** for(int j=0;j<10;j++) {
				for(int k=0;k<10;k++) {
					trans[k][j]=field[k][j];
					if(field[k][j]<10) {
						System.out.print("0"+trans[k][j]);
					} else {
					System.out.print(trans[k][j]);
					}
					System.out.print(" ");
				}
				System.out.println();
			} ***/
	 }
	 
	 /**
	  * Aufgabe1.2 e)
	  * Die Zahl auf Hauptdiagonalen wird aif 00 aufgesetzt
	  */
	 public void Hauptdiagonalen() {          	
		 int Haupt[][] = new int [10][10];	
		 int Hauptdiag=0;                         //Hier ersetzen wir zuerst auf 00
		 for(int k=0;k<10;k++) {
			for(int j=0;j<10;j++) {
				if(j==k) {                        // Die Zahl auf Hauptdiagonalen wird aif 00 aufgesetzt
					Haupt[k][j]=Hauptdiag;
					field[k][j]=Haupt[k][j];
				} else {
					Haupt[k][j]=field[k][j];
					field[k][j]=Haupt[k][j];
				}	
			}
		}
	 }
	 /**
	  * Aufgabe1.2 f)
	  * Jeder Zeile eines Spalt wird addieret
	  */
	 public void Spaltensummen() {           
		 for(int j=0;j<10;j++) {
			 int summe=0;                   //Jeder neue Spalten wird vom 0 addiert.
			for(int k=0;k<10;k++) {
			summe=summe+field[k][j];        //Jeder Zeile eines Spalt wird addieret
			}
		System.out.print(summe);
		System.out.print(" ");
		}
		System.out.println();
		System.out.println();
	}
	/**
	 * Aufgabe1.2 g)
	 * Jedes Zahl des erste Zeile wird auf 00 gesetzt.
	 */
	 public void ErstZeile() {              
		 for(int k=0;k<10;k++) {
				for(int j=0;j<10;j++) {
					if(k==0) {
						field[k][j]=0;    // Jedes Zahl des erste Zeile wird auf 00 gesetzt.
					}else {
						field[k][j]=field[k][j];
					}
					/**
					if(field[k][j]<10) {
						  System.out.print("0"+field[k][j]);
						} else {
						System.out.print(field[k][j]);
						}
						**/
				}
			}
	 }
	 
	 
}
