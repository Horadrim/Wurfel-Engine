/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.Blocks;

import Game.Chunk;
import Game.Controller;
import Game.Gameplay;
import org.newdawn.slick.util.Log;

/**
 *A Block that knows his own position
 * @author Benedikt
 */
public class SelfAwareBlock extends Block{
  /**
    * CoordZ is always absolute and relative at the same time.
    */
   public int coordZ;
   
   private int absCoordX;

   private int absCoordY;   
   private int relCoordX;
   private int relCoordY;
    
    SelfAwareBlock(){
        super();
    }
    
   SelfAwareBlock(int id){
       super(id);
   }
   
   SelfAwareBlock(int id,int value){
       super(id, value);
   }
   
   public int getAbsCoordX() {
        return absCoordX;
    }

    public int getAbsCoordY() {
        return absCoordY;
    }

     public int getCoordZ() {
        return coordZ;
    }

    public void setCoordZ(int coordZ) {
        this.coordZ = coordZ;
    }

    public int getRelCoordX() {
        return relCoordX;
    }

     /**
     * Set the relative X Coordinate.
     * @param X
     */
    public void setRelCoordX(int X){
        if (X < Chunk.BlocksX*3){
            relCoordX = X;
        } else {
            this.relCoordX = 3*Chunk.BlocksX-1;
            Gameplay.msgSystem.add("RelativeCoordX ist too high:"+X);
            Log.warn("RelativeCoordX ist too high:"+X);
        }
        
        if (X >= 0) {
            relCoordX = X;
        } else {
            relCoordX = 0;
            Gameplay.msgSystem.add("RelativeCoordX ist too low:"+X);
            Log.warn("RelativeCoordX ist too low:"+X);
        }
    }

    public int getRelCoordY() {
        return relCoordY;
    }

        /**
     * Set the relative Y Coordinate
     * @param Y
     */
    public void setRelCoordY(int Y){
        if (Y < Chunk.BlocksY*3){
            relCoordY = Y;
        }else {
            relCoordY = 3*Chunk.BlocksY-1;
            Gameplay.msgSystem.add("RelativeCoordY ist too high: "+Y);
            Log.warn("RelativeCoordY ist too high: "+Y);
        }
        
        if (Y >= 0) {
            relCoordY = Y;
        }else {
            relCoordY = 0;
            Gameplay.msgSystem.add("RelativeCoordY ist too low: "+Y);
            Log.warn("RelativeCoordY ist too low: "+Y);
        }
    }
    
     /**
     * Sets the absolute X and relative X coord.
     * @param X 
     */
    public void setAbsCoordX(int X){
        absCoordX = X;
        setRelCoordX(X - Controller.map.getCoordlistX(4)  * Chunk.BlocksX);
    }
    

    public void setAbsCoordY(int Y){
        absCoordY = Y;
        //da das Map Coordinatensystem in y-Richtung in zwei unterschiedliche Richtungen geht (hier "+" ???)
        setRelCoordY(Y + Controller.map.getCoordlistY(4) *Chunk.BlocksY);
    }    
   
    public void setAbsCoords(int X, int Y, int Z){
        setAbsCoordX(X);
        setAbsCoordY(Y);
        coordZ = Z;
        //if Z is too high set to highes possible position
        if (coordZ > Chunk.BlocksZ-2) coordZ = Chunk.BlocksZ -2;
    }
    
    //how can you make that this methods are only avilable to extending classes?
    protected void selfDestroy(){
        Controller.map.data[getRelCoordX()][getRelCoordY()][coordZ] = new Block(0,0);
        Controller.map.data[getRelCoordX()][getRelCoordY()][coordZ+1] = new Block(0,0);
    }
    
    protected void selfRebuild(){
        Controller.map.data[getRelCoordX()][getRelCoordY()][coordZ] = this;
        Controller.map.data[getRelCoordX()][getRelCoordY()][coordZ+1] = new Block(40,1);
    }
}