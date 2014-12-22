/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombie_crush_saga.data;
import java.io.IOException;
import static zombie_crush_saga.ZombieCrushSagaConstants.*;
/**
 *
 * @author Luigi Keh
 */
public class ZombieCrushSagaBoardJelly {
    
    //this will represent the board. 0 = not usable at all, 1 = usable by pieces.
    private boolean[][] theBoard;
    
    public ZombieCrushSagaBoardJelly() {
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
    
    public int countJelly(){
        int count = 0;
        for(int i = 0; i < BOARD_ROWS; i++){
            for(int j = 0; j < BOARD_COLUMNS; j++){
                //if true, count++
                if(theBoard[i][j])
                    count++;
            }
        }
        return count;
    }
    
    /**
     * 
     * @param row
     * @param column
     * @return boolean active or not
     */
    public boolean getBoardJelly(int row, int column){
        return theBoard[row][column];
    }
    
    public void putBoardJelly(int row, int column, boolean value){
        theBoard[row][column] = value;
    }

    @Override
    public String toString(){
        
        String temp = "";
        for(int i = 0; i < BOARD_ROWS; i++){
            for(int j = 0; j < BOARD_COLUMNS; j++){
                if(theBoard[i][j])
                    temp += "J ";
                else
                    temp += "0 ";
            }
            temp += "\n";
        }
        
        return temp;
    }
    
}
