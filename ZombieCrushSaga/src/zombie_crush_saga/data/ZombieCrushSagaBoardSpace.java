/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombie_crush_saga.data;
import java.io.IOException;
import static zombie_crush_saga.ZombieCrushSagaConstants.*;

/**manages what space is used and not in the grid.
 *
 * @author Luigi Keh
 */
public class ZombieCrushSagaBoardSpace {
    
    
    //this will represent the board. 0 = not usable at all, 1 = usable by pieces.
    private boolean[][] theBoard;
    
    public ZombieCrushSagaBoardSpace() {
        
        theBoard = new boolean[BOARD_ROWS][BOARD_COLUMNS];
        
    }
    
    //
    public void initBoard(boolean[][] initBoard){
        
        try
        {
            if(initBoard.length != BOARD_ROWS)
                throw new IOException();
            if(initBoard[0].length != BOARD_COLUMNS)
                throw new IOException();
        }
        catch(IOException ex)
        {
            System.out.println(ex + "bad matrix size, bad!" );
        }
        
        //theboard = initBoard
        for(int i = 0; i < BOARD_ROWS; i++){
            for(int j = 0; j < BOARD_COLUMNS; j++){
                theBoard[i][j] = initBoard[i][j];
            }
        }
    }
    
    
    public int getBoardSize(){
        return BOARD_ROWS * BOARD_COLUMNS;
    }
    
    public int getRowSize(){
        return BOARD_ROWS;
    }
    
    public int getColumnSize(){
        return BOARD_COLUMNS;
    }
    
    
    public boolean getBoardSpace(int row, int column){
        return theBoard[row][column];
    }
    
}
