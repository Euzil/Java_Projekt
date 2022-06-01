package de.uniluebeck.itm.schiffeversenken.game;

import de.uniluebeck.itm.schiffeversenken.engine.AssetRegistry;
import de.uniluebeck.itm.schiffeversenken.engine.Canvas;
import de.uniluebeck.itm.schiffeversenken.engine.Tile;
import de.uniluebeck.itm.schiffeversenken.engine.Vec2;
import de.uniluebeck.itm.schiffeversenken.engine.View;
import de.uniluebeck.itm.schiffeversenken.game.model.GameModel;
/**
 * modified by Gruppe 107 Youran Wang (719511)
 */
public class GameView extends View<GameModel> {

    private final GameFieldRenderer fieldRenderer;
    private final HitMissRenderer opponentFieldRenderer;

    public GameView(GameModel m) {
        super(m);
        this.fieldRenderer = new GameFieldRenderer(this.getModelInstance().getHumanPlayerField());
        // Aufgebe 3.1.c
        // Dann werden wir die Klasse HitmissRenderer einladen. Das Field wird mit dem Status aus HitMissRenderer anzeigen.
        this.opponentFieldRenderer = new HitMissRenderer(this.getModelInstance().getComputerPlayerField());
    }

    @Override
    public void render(Canvas c, Vec2 mouseLocation) {
        final int frameWidth = c.getResolutionWidth(), frameHeight = c.getResolutionHeight();
        final int offsetX = 10, offsetY = 35;

        final GameModel model = this.getModelInstance();
        final Vec2 gameFieldDimensions = model.getHumanPlayerField().getSize(); 

        final int fieldsWidth = gameFieldDimensions.getX() * Constants.TILE_SIZE;
        final int fieldsHeight = gameFieldDimensions.getX() * Constants.TILE_SIZE;
        final int opponentsFieldX = offsetX + fieldsWidth + 10;
        this.fieldRenderer.renderGameField(c, offsetX, offsetY); 
        this.opponentFieldRenderer.renderGameField(c, opponentsFieldX, offsetY);
        // Aufgabe 4.1.c
        this.fieldRenderer.renderMouseOver(c, mouseLocation.getX(), mouseLocation.getY(), opponentsFieldX, offsetY);
        model.updateOpponentsFieldOnScreenData(new Vec2(opponentsFieldX, offsetY),
                new Vec2(fieldsWidth, fieldsHeight));

        c.setColor(0.7, 0.7, 0.7);
        c.drawRoundRect(frameWidth - 280 - offsetX, offsetY, 280, frameHeight - offsetY - 100, 5, 5);

        final int[] numbers = new int[]{model.getRoundCounter(), model.getPlayerPoints(), model.getAiPoints()};
        final String[] labels = new String[]{"Round: ", "Your points", "Computers points"};
        for (int i = 0; i < numbers.length; i++) {
            // Irgendwie muss man da noch dinge mit dem Offset machen...
            draw7segNumberAt(c, frameWidth - offsetX - 45,
                    offsetY + 25 + (i * 50), numbers[i]);
            c.drawString(frameWidth - 270 - offsetX, offsetY + 35 + (i * 50), labels[i]);
        }


        if(model.isRoundChanging()) {
            c.setColor(0.7, 0.7, 0.7, 0.7);
            c.fillRect(0, 0, frameWidth, frameHeight);
            c.setColor(0, 0, 0);
            final String text = "Round changing. Please wait for the AI to destroy you.";
            final Vec2 textDim = c.getTextDimensions(text);
            c.drawString(frameWidth / 2 - textDim.getX() / 2, frameHeight / 2 - textDim.getY() / 2, text);
        }
    }
    // Aufgabe 4.3.a
    private void draw7segNumberAt(Canvas c, int x, int y, int number) {
        // c.drawString(x, y, Integer.toString(number));
        // TODO implement a real (well sort of, it's still inside a computer) 7 segment display
    	 c.setColor(1,1,1);
    	// Wenn number hat vier Stellen.
     	if(number>999) {
     		int [] number_1=new int[4];
     		number_1[0]=number/1000;
     		number_1[1]=(number-number_1[0]*1000)/100;
     		number_1[2]=(number-number_1[0]*1000-number_1[1]*100)/10;
     		number_1[3]=number-number_1[0]*1000-number_1[1]*100-number_1[2]*10;
     	    // 7-Segment-Bild ausgeben
			for(int i=number_1.length-1;i>=0;i--) {				
				Tile tile=AssetRegistry.getTile("7seg."+number_1[i]);				
				c.fillRect((x-(number_1.length-i-1)*26)-3, y-3, 26, 44);			
				tile.renderAt(c, new Vec2(x-(number_1.length-i-1)*26,y));		
			}
     	}
        // Wenn number hat drei Stellen.
     	if(number<1000 && number >99) {
     		int [] number_2=new int[3];
     		number_2[0]=number/100;
     		number_2[1]=(number-number_2[0]*100)/10;
     		number_2[2]=number-number_2[0]*100-number_2[1]*10;
     	    // 7-Segment-Bild ausgeben
            for(int i=number_2.length-1;i>=0;i--) {			
				Tile tile=AssetRegistry.getTile("7seg."+number_2[i]);			
				c.fillRect((x-(number_2.length-i-1)*26)-3, y-3, 26, 44);			
				tile.renderAt(c, new Vec2(x-(number_2.length-i-1)*26,y));				
			}
     	}
        // Wenn number hat zwei Stellen.
     	if(number <100 && number >9) {
     		int [] number_3=new int[2];
     		number_3[0]=number/10;
     		number_3[1]=number-number_3[0]*10;
     		// 7-Segment-Bild ausgeben
            for(int i=number_3.length-1;i>=0;i--) {				
				Tile tile=AssetRegistry.getTile("7seg."+number_3[i]);				
				c.fillRect((x-(number_3.length-i-1)*26)-3, y-3, 26, 44);				
				tile.renderAt(c, new Vec2(x-(number_3.length-i-1)*26,y));			
			}
     	}
        // Wenn number hat nur eine Stelle.
     	if(number<10) {
     		int [] number_4=new int[1];
     		number_4[0]=number;
     	        // 7-Segment-Bild ausgeben   
				Tile tile=AssetRegistry.getTile("7seg."+number_4[0]);	
				c.fillRect(x-3, y-3, 26, 44);			
				tile.renderAt(c, new Vec2(x,y));    		
     	}
   	 
    }

    @Override
    public void prepare() {

    }
    
}
