package Game;

import Game.Blocks.Block;
import Game.Blocks.Player;
import MainMenu.MainMenuState;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

/**
 *
 * @author Benedikt
 */
public class Controller {
    /**
     *The list which has all current nine chunks in it.
     */
    public static Map map;
    /**
     * Contains the player
     */
    public Player player;   
   
   /**
     * Should the graphic be a bit slower but better? Must be in Controller because is needed for e.g. the Block and there used as data
     */
    public boolean goodgraphics = false;
    public boolean rendermethod = true;
    
    public GameContainer gc;
    private StateBasedGame sbg;
    private int oldx;
    private int oldy;
    private float zoomx = 1;
    
    /**
     * Constructor is called when entering the gamemode.
     * @param container
     * @param game
     * @throws SlickException
     */
    public Controller(GameContainer container, StateBasedGame game) throws SlickException{
        gc = container;
        sbg = game;
          
        gc.getInput().addMouseListener(new MouseDraggedListener());
        map = new Map(MainMenuState.loadmap);
    }

    

    
    /**
     * Main method which is called every time
     * @param delta
     * @throws SlickException
     */
    public void update(int delta) throws SlickException{  
        if (delta>200) Log.warn("delta is too high to stay stable. d: "+delta);
            
        Input input = gc.getInput();
         
        if (input.isKeyDown(Input.KEY_Q)) gc.exit();
       
        //open menu
        if (input.isKeyPressed(Input.KEY_ESCAPE)) openmenu();
       
        //toggle fullscreen
        if (input.isKeyPressed(Input.KEY_F)) gc.setFullscreen(!gc.isFullscreen()); 
       
        //pause
        if (input.isKeyDown(Input.KEY_P)) gc.setPaused(true);

        //good graphics
        if (input.isKeyPressed(Input.KEY_G)) {
            goodgraphics = !goodgraphics;
            Gameplay.iglog.add("Good Graphics is now "+goodgraphics);
        }
        
        //render method
        if (input.isKeyPressed(Input.KEY_R)) {
            rendermethod = !rendermethod;
            Gameplay.iglog.add("Rendermethod changes "+rendermethod);
            Block.reloadSprites(Gameplay.view.camera.getZoom());
        }
        
        
        //toggle camera
        if (input.isKeyPressed(Input.KEY_C)) Gameplay.view.camera.focus = !Gameplay.view.camera.focus;
        
        //restart
        if (input.isKeyPressed(Input.KEY_N)) map = new Map(false);
                
        //reset zoom
        if (input.isKeyPressed(Input.KEY_Z)) {
            Gameplay.view.camera.setZoom(1);
            Gameplay.iglog.add("Zoom reset");
        }
        
        
        //walk
        if ("WASD".equals(player.getControls())){
            player.walk(
                input.isKeyDown(Input.KEY_W),
                input.isKeyDown(Input.KEY_S),
                input.isKeyDown(Input.KEY_A),
                input.isKeyDown(Input.KEY_D),
                .25f+(input.isKeyDown(Input.KEY_LSHIFT)? 0.75f: 0),
                delta
            );
            if (input.isKeyPressed(Input.KEY_SPACE)) player.jump();
        }
       
       

        //earth to right
        if (Gameplay.view.camera.x < Chunk.SizeX/3)
           map.setCenter(3);
        else {       
            //earth to the left
            if (Gameplay.view.camera.x + Gameplay.view.camera.width > 8*Chunk.SizeX/3) 
                map.setCenter(5); 
        }
        
       //scroll up, earth down            
        if (Gameplay.view.camera.y  <= 0) {
            map.setCenter(1);
        } else {
            //scroll down, earth up
            if (Gameplay.view.camera.y+Gameplay.view.camera.height > Chunk.SizeY*3)
                map.setCenter(7);
        }
        
        //camera
        oldx = Gameplay.view.camera.x;
        oldy = Gameplay.view.camera.y;
        
        player.update(delta);
        
        if (Gameplay.view.camera.focus == false) {
            Gameplay.view.camera.x = player.getRelCoordX() * Block.width + Block.width / 2 *(player.getRelCoordY() % 2) + player.getOffsetX() - Gameplay.view.camera.width / 2;
            Gameplay.view.camera.y = (int) ((player.getRelCoordY() - player.coordZ) * Block.height/2
                                           + player.getOffsetY()* 2* (1/Block.aspectRatio)//why does it have to be multiplied by two?
                                           - Gameplay.view.camera.height);
        } 
        
        map.data[player.getRelCoordX()][player.getRelCoordY()][player.coordZ] = player;
        map.changePosX(- Gameplay.view.camera.x + oldx);
        map.changePosY(-Gameplay.view.camera.y + oldy);
            


        //recalc if requested
        map.recalcIfRequested();      
       
        //update the log
        Gameplay.iglog.update(delta);
    }
    
    
  
    class MouseDraggedListener implements MouseListener{
        @Override
        public void mouseWheelMoved(int change) {
            gc.getInput().consumeEvent();
            
            zoomx = zoomx + change/1000f;
            Gameplay.view.camera.setZoom((float) (3f*Math.sin(zoomx-1.5f)+3.5f));
            
            Gameplay.view.camera.width = (int) (gc.getWidth() / Gameplay.view.camera.getZoom());
            Gameplay.view.camera.height= (int) (gc.getHeight() / Gameplay.view.camera.getZoom());
            
           /* Block.width =(int) (gc.getWidth() *zoom / Chunk.BlocksX);
            Block.height = (int) (4*gc.getHeight()*zoom / Chunk.BlocksY);
            Chunk.SizeX = (int) (Chunk.BlocksX*Block.width*zoom);
            Chunk.SizeY = (int) (Chunk.BlocksY*Block.height*zoom/2);*/
            
            Gameplay.iglog.add("Zoom: "+Gameplay.view.camera.getZoom()+" Chunk.SizeX: "+Chunk.SizeX+" Chunk.SizeY: "+Chunk.SizeY);   
        }

        @Override
        public void mouseClicked(int button, int x, int y, int clickCount) {
        }

        @Override
        public void mousePressed(int button, int x, int y) {
        }

        @Override
        public void mouseReleased(int button, int x, int y) {   
        }

        @Override
        public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        }

        @Override
        public void mouseDragged(int oldx, int oldy, int newx, int newy) {
            //workaround for the bug, because the event is called multiple times
            gc.getInput().consumeEvent();
            
            if (Gameplay.view.camera.focus) {
                Gameplay.view.camera.x += newx-oldx;
                Gameplay.view.camera.y += newy-oldy;
            }
            /*for (int i=0;i<9;i++){
                chunklist[i].posX += newx - oldx;
                chunklist[i].posY += newy - oldy ;
            }*/
            
            //if the middle chunk is scrolled down over the middle line then 
            //GameplayState.iglog.add("Chunk.SizeX: "+String.valueOf(Chunk.SizeX));
            //GameplayState.iglog.add("chunk: "+String.valueOf(chunklist[4].posX));
            
    
        }

        @Override
        public void setInput(Input input) {
        }

        @Override
        public boolean isAcceptingInput() {
            return true;
        }

        @Override
        public void inputEnded() {
          
        }

        @Override
        public void inputStarted() {
        }
    }
    
    private void openmenu(){
        boolean openmenu = true;
    }
}