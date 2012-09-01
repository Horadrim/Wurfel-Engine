/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import MainMenu.MainMenuState;

/**
 *
 * @author Benedikt
 */
public class Map {
    Block data[][][] = new Block[Chunk.BlocksX*3][Chunk.BlocksY*3][Chunk.BlocksZ];
    public int posX;
    public int posY;
    public boolean changes;
    /**
     *A list of the X coordinates.
     */
    public int[] coordlistX = new int[9];
    /**
     *A list for the y coordinates of the current chunks.
     */
    public int coordlistY[] = new int[9];;

    Map(boolean loadmap) {
        //create the map
        Chunk tempchunk;
        int pos = 0;
        
        for (int y=1; y > - 2; y--)
            for (int x=-1; x < 2; x++){
                coordlistX[pos] = x;
                coordlistY[pos] = y;
                tempchunk = new Chunk(
                        coordlistX[pos],
                        coordlistY[pos],
                        coordlistX[pos]*Chunk.SizeX,
                        -coordlistY[pos]*Chunk.SizeY,
                        loadmap
                );
                setChunk(pos, tempchunk);
                pos++;               
            }
        changes = true;
    }
    
    /**
     * Reorgnanises the map and sets the center to newcenter.
     * Move all chunks when loading or creating a new piece of the map
     * @param center 
     */
    public void setCenter(int center){
        //newcenter is 1,3,5 or 7
        /*
          |0|1|2|
          -------
          |3|4|5|
          -------
          |6|7|8|
         */
        
        GameplayState.iglog.add("New Center: "+center);
        
        Block data_copy[][][] = data;
        
        for (int pos=0; pos<9; pos++){
            if (check_chunk_movement(pos,center)){
                //System.out.println("chunkliste[" + i + "] bekommt den alten von chunkliste["+ (i + newcenter - 4) +"]");
                setChunk(
                    pos,
                    getChunk(data_copy,pos -4 + center)
                );
            } else {
                setChunk(
                        pos,
                        new Chunk(
                            coordlistX[pos] + (center == 3 ? -1 : (center == 5 ? 1 : 0)),
                            coordlistY[pos] + (center == 1 ? 1 : (center == 7 ? -1 : 0)),
                            coordlistX[pos] + (int) ((center == 3 ? -Chunk.SizeX : (center == 5 ?  Chunk.SizeX: 0))),
                            coordlistY[pos] + (int) ((center == 1 ? -Chunk.SizeY : (center == 7 ? Chunk.SizeY : 0))),
                            MainMenuState.loadmap
                        )
                );
                //System.out.println("chunkliste["+i+"] bekommt einen neuen Chunk: "+ chunklist[i].coordX +","+chunklist[i].coordY);
                //System.out.println("Pos: "+ chunklist[i].posX +","+ chunklist[i].posY);
            }
        }
        changes = true;
    }
    
     private boolean check_chunk_movement(int pos, int movement){
        //checks if the number can be reached by moving the net in a direction, very complicated
        boolean result = true; 
        switch (movement){
            case 1: if ((pos==0) || (pos==1) || (pos==2)) result = false;
            break;
            
            case 3: if ((pos==0) || (pos==3) || (pos==6)) result = false;
            break;  
                
            case 5: if ((pos==2) || (pos==5) || (pos==8)) result = false;
            break;
                
            case 7: if ((pos==6) || (pos==7) || (pos==8)) result = false;
            break;
        } 
        return result;
    }
     
    /*
     * To fill a chunk by copying a chunk of the map 
     * @param pos
     */ 
    private Chunk getChunk(Block[][][] data, int pos) {
        Chunk tmpChunk = new Chunk();
        for (int x = Chunk.BlocksX*(pos % 3);
                x <Chunk.BlocksX*(pos % 3+1);
                x++
            )
                for (int y=Chunk.BlocksY*Math.abs(pos / 3);
                        y < Chunk.BlocksY*Math.abs(pos / 3+1);
                        y++
                    ) {
                    System.arraycopy(
                        data[x]
                            [y],                
                        0,
                        data[x][y],
                        0,
                        Chunk.BlocksZ
                    );
                }
        return tmpChunk;
    }

    private void setChunk(int pos, Chunk oldchunk) {
        for (int x=0;x <Chunk.BlocksX;x++)
                for (int y=0;y < Chunk.BlocksY;y++) {
                    System.arraycopy(
                        oldchunk.data[x][y],
                        0,
                        data[x+ Chunk.BlocksX*(pos%3)][y+ Chunk.BlocksY*Math.abs(pos/3)],
                        0,
                        Chunk.BlocksZ
                    );
                }
    }
}