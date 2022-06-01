package de.uniluebeck.itm.schiffeversenken.game.menues;

import de.uniluebeck.itm.schiffeversenken.engine.*;

/**
 * This scene displays a loading screen while doing shady loading business.
 * After bing done doing so it switches to the main menu scene.
 * 
 * @author leondietrich, modified by Gruppe 107 Youran Wang (719511)
 *
 */
public class LoadingScene extends Scene {

	private final Controller<Object> c;

	public LoadingScene() {
		super();
		this.c = new Controller<Object>(null){
			@Override
			public void clickedAt(Vec2 position) {}

			@Override
			public void keyPressed(int key, boolean shift, boolean alt, boolean ctrl, boolean down, boolean up, boolean left, boolean right) {}

			@Override
			public void performFrequentUpdates() {}

			@Override
			public void prepare() {}
		};
	}

	@Override
	public void draw(Canvas c, Vec2 mouseLocation) {
		c.setColor(0.9, 0.9, 0.9);
		c.drawString(15, 55, "Loading Data...");
	}
    /**
     * tiles: benuetzt fuer die Bezeichnung und Dateiname mit Pfadangabe in einem Array-2D beinhaltet.
     * dispatchWork: benuetzt fuer das Laden, die Datei im Bezeichnung.
     */
	@Override
	public void attach() {
		final String[][] tiles = new String[][]{
				{"water.hiddenshiphit", "assets/32x32/Tile_hidden_hit_32x32_uint8_rgba.png"},
				{"water", "assets/32x32/Tile_Water_32x32_uint8_rgba.png"},
				{"water.hit", "assets/32x32/Tile_Water_hit_32x32_uint8_rgba.png"},
				{"up.ship.bug", "assets/32x32/Tile_Bug_up_32x32_uint8_rgba.png"},
				{"up.ship.middle", "assets/32x32/Tile_Middle_up_32x32_uint8_rgba.png"},
				{"up.ship.aft", "assets/32x32/Tile_aft_up_32x32_uint8_rgba.png"},
				{"up.ship.bug.hit", "assets/32x32/Tile_Bug_up_hit_32x32_uint8_rgba.png"},
				{"up.ship.middle.hit", "assets/32x32/Tile_Middle_up_hit_32x32_uint8_rgba.png"},
				{"up.ship.aft.hit", "assets/32x32/Tile_aft_up_hit_32x32_uint8_rgba.png"},
				{"up.ship.single", "assets/32x32/Tile_singleship_up_32x32_uint8_rgba.png"},
				{"up.ship.single.hit", "assets/32x32/Tile_singleship_up_hit_32x32_uint8_rgba.png"},
				{"right.ship.bug", "assets/32x32/Tile_Bug_right_32x32_uint8_rgba.png"},
				{"right.ship.middle", "assets/32x32/Tile_Middle_right_32x32_uint8_rgba.png"},
				{"right.ship.aft", "assets/32x32/Tile_aft_right_32x32_uint8_rgba.png"},
				{"right.ship.bug.hit", "assets/32x32/Tile_Bug_right_hit_32x32_uint8_rgba.png"},
				{"right.ship.middle.hit", "assets/32x32/Tile_Middle_right_hit_32x32_uint8_rgba.png"},
				{"right.ship.aft.hit", "assets/32x32/Tile_aft_right_hit_32x32_uint8_rgba.png"},
				{"right.ship.single", "assets/32x32/Tile_singleship_right_32x32_uint8_rgba.png"},
				{"right.ship.single.hit", "assets/32x32/Tile_singleship_right_hit_32x32_uint8_rgba.png"},
				{"arrow.down", "assets/32x32/Tile_arrow_down_32x32_uint8_rgba.png"},
				// Aufgabe 2.1.b 
				{"7seg.0", "assets/7seg/0_small.png"},
				{"7seg.1", "assets/7seg/1_small.png"},
				{"7seg.2", "assets/7seg/2_small.png"},
				{"7seg.3", "assets/7seg/3_small.png"},
				{"7seg.4", "assets/7seg/4_small.png"},
				{"7seg.5", "assets/7seg/5_small.png"},
				{"7seg.6", "assets/7seg/6_small.png"},
				{"7seg.7", "assets/7seg/7_small.png"},
				{"7seg.8", "assets/7seg/8_small.png"},
				{"7seg.9", "assets/7seg/9_small.png"},
				};
                
		// Folgend benutzte ich nicht mit For-Schleife, nur einfach fuer eine Versuche. 
				
		/*this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile(tiles[0][0], Application.loadTile(tiles[0][1]));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile(tiles[1][0], Application.loadTile(tiles[1][1]));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile(tiles[2][0], Application.loadTile(tiles[2][1]));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile(tiles[3][0], Application.loadTile(tiles[3][1]));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile(tiles[4][0], Application.loadTile(tiles[4][1]));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile(tiles[5][0], Application.loadTile(tiles[5][1]));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile(tiles[6][0], Application.loadTile(tiles[6][1]));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile(tiles[7][0], Application.loadTile(tiles[7][1]));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile(tiles[8][0], Application.loadTile(tiles[8][1]));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile(tiles[9][0], Application.loadTile(tiles[9][1]));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile(tiles[10][0], Application.loadTile(tiles[10][1]));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile(tiles[11][0], Application.loadTile(tiles[11][1]));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile(tiles[12][0], Application.loadTile(tiles[12][1]));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile(tiles[13][0], Application.loadTile(tiles[13][1]));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile(tiles[14][0], Application.loadTile(tiles[14][1]));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile(tiles[15][0], Application.loadTile(tiles[15][1]));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile(tiles[16][0], Application.loadTile(tiles[16][1]));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile(tiles[17][0], Application.loadTile(tiles[17][1]));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile(tiles[18][0], Application.loadTile(tiles[18][1]));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile(tiles[19][0], Application.loadTile(tiles[19][1]));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile("7seg.0", Application.loadTile("assets/7seg/2_small.png"));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile("7seg.1", Application.loadTile("assets/7seg/1_small.png"));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile("7seg.2", Application.loadTile("assets/7seg/2_small.png"));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile("7seg.3", Application.loadTile("assets/7seg/3_small.png"));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile("7seg.4", Application.loadTile("assets/7seg/4_small.png"));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile("7seg.5", Application.loadTile("assets/7seg/5_small.png"));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile("7seg.6", Application.loadTile("assets/7seg/6_small.png"));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile("7seg.7", Application.loadTile("assets/7seg/7_small.png"));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile("7seg.8", Application.loadTile("assets/7seg/8_small.png"));
			}
		});
		this.c.dispatchWork(new Runnable() {
			@Override
			public void run() {
				AssetRegistry.registerTile("7seg.9", Application.loadTile("assets/7seg/9_small.png"));
			}
		});*/
				
				
		// Aufgabe 2.1.a
		// Daten laden					
		for(int i=0;i<tiles.length;i++) {       // Mit For-Schleife wird alle Bilde im Keys geladet
			final String keys=tiles[i][0];      // Im dispatchWork kann nicht die Variablen verarbeiten. 
			final String Bilds=tiles[i][1];     // Dann brauchen wir die final Variablen keys und Bilds zuerst zu arbeiten, und dann im dispatchWork zu laden. 
			this.c.dispatchWork(new Runnable() {
				@Override
				public void run() {
				   AssetRegistry.registerTile(keys, Application.loadTile(Bilds)); // Bilds zum Keys laden.
				}
			});	
		}
		this.c.startWorkStack();
	}
	/*
	 * Nach Bearbeitung der Aufgabe 2.1:
	 * Beim Versuch, ein Schiff zu platzieren, wird jetzt an der Maus angeheftet das Schiff angezeigt.
	 * 
	 * 
	 */
	
	@Override
	public void update(long milis) {
		if (!this.c.hasWork()) {
			MainMenu m = new MainMenu();
			Application.switchToScene(m.getScene());
		}
	}

	@Override
	public void detach() {}

	@Override
	public void clickedAt(Vec2 position) {}

	@Override
	public void keyPressed(char key, boolean shift, boolean alt, boolean ctrl, boolean down, boolean up, boolean left, boolean right) {}

}
