package com.BombingGames.Game;

import com.BombingGames.Game.Blocks.Block;
import com.BombingGames.Game.Blocks.Blockpointer;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

/**
 * The View manages everything what should be drawn.
 * @author Benedikt
 */
public class View {
    /**
     * Contains a font
     */
    public static AngelCodeFont baseFont;

    private Camera camera;
    private float equalizationScale;    
    
    /**
     * Creates a View
     * @param gc
     * @throws SlickException
     */
    public View(GameContainer gc) throws SlickException {
        // initialise the ttF font which CAUSES LONG LOADING TIME!!!
        //TrueTypeFont trueTypeFont;

        //startFont = Font.createFont(Font.TRUETYPE_FONT,new BufferedInputStream(this.getClass().getResourceAsStream("Blox2.ttf")));
        //UnicodeFont startFont = new UnicodeFont("com/BombingGames/Game/Blox2.ttf", 20, false, false);
        baseFont = new AngelCodeFont("com/BombingGames/Game/arial.fnt","com/BombingGames/Game/arial.png");
        //baseFont = startFont.deriveFont(Font.PLAIN, 12);
        //baseFont = startFont.getFont().deriveFont(Font.PLAIN, 18);
        //tTFont = new TrueTypeFont(baseFont, true);
        
        //default rendering size is FullHD
        equalizationScale = gc.getWidth()/1920f;
        Log.debug("Scale is:" + Float.toString(equalizationScale));
        
        camera = new Camera(
            new Blockpointer(Map.getBlocksX()/2, Map.getBlocksY()/2, 0),
            //new Blockpointer(Gameplay.getController().getPlayer(), 0, 0, 0),
            0, //top
            0, //left
            gc.getWidth(), //full width
            gc.getHeight(), //full height
            equalizationScale
        );
        
        //camera.focusOnBlock(new Blockpointer(Chunk.getBlocksX()*3/2,Map.getBlocksY()/2,Chunk.getBlocksZ()/2));
        
        if (camera.getTotalHeight() > Chunk.getBlocksY()*Block.DIM2/2) {
            Gameplay.MSGSYSTEM.add("The chunks are maybe too small for this camera height/resolution to grant a stable experience", "Warning");
            Log.warn("The chunks are maybe too small for this camera height/resolution to grant a stable experience");
        }
        
     /*font = new java.awt.Font("Verdana", java.awt.Font.BOLD, 12);
        tTFont = new TrueTypeFont(font, true);
        font = new java.awt.Font("Verdana", java.awt.Font.BOLD, 8);
        tTFont_small = new TrueTypeFont(font, true);*/
     }
    
    /**
     * Main method which is called every time
     * @param g 
     * @throws SlickException
     */
    public void render(Graphics g) throws SlickException{
        g.scale(equalizationScale, equalizationScale);
        camera.render();
        Gameplay.MSGSYSTEM.draw(); 
    }
       

    /**
     * The equalizationScale is a factor which the image is scaled by to have the same size on different resolutions.
     * @return
     */
    public float getEqualizationScale() {
        return equalizationScale;
    }

    /**
     * Returns the camera
     * @return
     */
    public Camera getCamera() {
        return camera;
    }
    
   /**
     * Reverts the perspective and transforms it into a coordiante which can be used in the game logic.
     * @param x the x position on the screen
     * @return game coordinate
     */
    public int ScreenXtoGame(int x){
        Log.debug("ScreenXtoGame("+x+")");
        return (int) ((x + Gameplay.getView().getCamera().getX()) / Gameplay.getView().getCamera().getAbsZoom());
    }
    
   /**
     * Reverts the perspective and transforms it into a coordiante which can be used in the game logic.
     * @param y the y position on the screen
     * @return game coordinate
     */
    public int ScreenYtoGame(int y){
        Log.debug("ScreenYtoGame("+y+")");
        return (int) ((y + Gameplay.getView().getCamera().getY()) / Gameplay.getView().getCamera().getAbsZoom() * 2);
    }
    
    /**
     * Returns the coordinates belonging to a point on the screen. ATTENTION! this is just 2d and the topmost layer is taken. no depth-check.
     * @param x the x position on the screen
     * @param y the y position on the screen
     * @return map coordinates
     */
    public int[] ScreenToGameCoords(int x, int y){
        int[] coords = new int[3];  
        
        //reverse y to game niveau, first the zoom:
        Log.debug("ScreenToGameCoords("+x+","+y+")");
        y = ScreenYtoGame(y);
        x = ScreenXtoGame(x);
              
        coords[0] = x / Block.DIMENSION-1;
        
        coords[1] = (int) (y / Block.DIMENSION)*2-1;
            
        coords[2] = Map.getBlocksZ()-1;
        
        int[] tmpcoords = Block.posToNeighbourCoords(coords, x % Block.DIMENSION, y % Block.DIMENSION);
        coords[0] = tmpcoords[0];
        coords[1] = tmpcoords[1] + coords[2]*2;
        
        return coords;
    }
}