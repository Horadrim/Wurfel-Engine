/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Game.Blocks.Player;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *This COntrolelr is the extended Controller from the engine. Put game code here.
 * @author Benedikt
 */
public class GameController extends Controller {
    
    public GameController(GameContainer container, StateBasedGame game) throws SlickException{
        super(container, game);
        player = new Player((int) (Chunk.BlocksX*1.5),(int) (Chunk.BlocksY*1.5),19);
        map.data[(int) (Chunk.BlocksX*1.5)][(int) (Chunk.BlocksY*1.5)][19] = player;
    }
    
}