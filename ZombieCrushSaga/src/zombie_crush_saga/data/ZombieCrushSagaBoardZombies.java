

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombie_crush_saga.data;
import java.util.ArrayList;
import static zombie_crush_saga.ZombieCrushSagaConstants.*;
import zombie_crush_saga.ui.ZombieCrushSagaZombie;

/**
 *
 * @author Luigi Keh
 */
public class ZombieCrushSagaBoardZombies {
    
    private int numMatches;                         //counts how many matches there are for combo scoring purposes.
    private int multiplier;
    private boolean hasMatches;                     //true if board has matches. default false whenever the board isn't checked for matches fully again.
    private int currentBoardScoreInt;                  //only gets the score for the current matches
    private int[][] boardScore;     //the score for each space's zombie explosion.
    private ZombieCrushSagaBoardSpace boardSpace;   //keeps track of the board of spaces.
    private ZombieCrushSagaZombie[][] boardZombies;
    private ZombieCrushSagaBoardJelly boardJelly;
    
    private ZombieCrushSagaZombie swappedZombie;    //the zombie the user initially clicks to swap.
    
    //to prevent repeated scoring
    //-1 = not used yet, default
    //0 = not used for matches at all
    //1 = 1 zombie
    //3 = part of a 3-of-a-kind
    //4 = part of a 4-of-a-kind
    //5 = part of a 5-of-a-kind
    //6 = part of a L
    //7 = part of a T
    private int[][] boardMatches;
    private int[][] lastBoardMatches;
    private boolean fiveBombSwapped;
    private boolean specialActionExecuted;
    
    ZombieCrushSagaZombie zombieAffected;   //for zombie crusher
    ZombieCrushSagaZombie zombie1FiveBomb;
    ZombieCrushSagaZombie zombie2FiveBomb;
    
    /*
     * first digit = special Type; second digit = direction (for stripped) 0 = default; last digit = color;
     * 4 - location of a 4-of-a-kind special, striped
     * 5 - location of a 5-of-a-kind special, five color bomb
     * 6 - location of a L-of-a-kind or T-of-a-kind special, wrapped
     */
    private int[][] boardSpecialZombies;            //keeps track where we will put future special zombies.
    
    private ArrayList<ZombieCrushSagaZombie> unprocessedZombies;    //zombies that have been blown up.
    private ArrayList<Integer> stripedRowsAffected;     //row that striped zombies will blow up
    private ArrayList<Integer> stripedColumnsAffected;  //columns that striped zombies will blow up
    //areas that striped zombies will blow up. pop twice, one for row, twice for column.
    private ArrayList<Integer> wrappedAffected;
    private ArrayList<ZombieCrushSagaZombie> fiveBombAffected;
    
    public ZombieCrushSagaBoardZombies() {
        boardZombies = new ZombieCrushSagaZombie[BOARD_ROWS][BOARD_COLUMNS];
        boardMatches = new int[BOARD_ROWS][BOARD_COLUMNS];
        lastBoardMatches = new int[BOARD_ROWS][BOARD_COLUMNS];
        boardSpecialZombies = new int[BOARD_ROWS][BOARD_COLUMNS];
        boardScore = new int[BOARD_ROWS][BOARD_COLUMNS];
        
        unprocessedZombies = new ArrayList<>();
        
        stripedRowsAffected = new ArrayList<>();
        stripedColumnsAffected = new ArrayList<>();
        wrappedAffected = new ArrayList<>();
        fiveBombAffected = new ArrayList<>();
        fiveBombSwapped = false;
        specialActionExecuted = false;
        multiplier = 0;
    }
    
    
    public void resetCurrentBoardScore(){
        currentBoardScoreInt = 0;
    }
    
    public void resetNumMatches(){
        numMatches = 0;
    }
    
    public void resetBoardMatches() {
        for(int row = 0; row < BOARD_ROWS; row++){
            for(int column = 0; column < BOARD_COLUMNS; column++){
                boardMatches[row][column] = -1;
            }
        }
        
        hasMatches = false;
    }
    
    /**
     * sets all the boardSpacialZombies to 0.
     */
    public void resetBoardSpecialZombies(){
        for(int row = 0; row < BOARD_ROWS; row++){
            for(int column = 0; column < BOARD_COLUMNS; column++){
                boardSpecialZombies[row][column] = 0;
            }
        }
    }
    
    /**
     * sets all the boardScore to 0.
     */
    public void resetBoardScore(){
        for(int row = 0; row < BOARD_ROWS; row++){
            for(int column = 0; column < BOARD_COLUMNS; column++){
                boardScore[row][column] = 0;
            }
        }
    }
    
    public void setSwappedZombie(ZombieCrushSagaZombie theZombie){
        swappedZombie = theZombie;
    }
    
    public void setMultiplier(int num){
        multiplier = num;
    }
    
    public void putZombie(int row, int column, ZombieCrushSagaZombie zombie){
        boardZombies[row][column] = zombie;
        
        if(zombie != null)
            zombie.setGridCell(row, column);
    }
    
    /**
     * returns a zombie in the grid
     *
     * @param row position
     * @param column position
     */
    public ZombieCrushSagaZombie getZombie(int row, int column){
        return boardZombies[row][column];
    }
    
    public int[][] getLastBoardMatches(){
        return lastBoardMatches;
    }
    
    public int[][] getBoardScore(){
        return boardScore;
    }
    
    public boolean getSpecialActionExecuted(){
        return specialActionExecuted;
    }
    
    public int getNumMatches(){
        return numMatches;
    }
    
    public int getMultipler(){
        return multiplier;
    }
    
    public int getFirstActiveEmptyTopRow(int col){
        for(int row = 0; row < BOARD_ROWS; row++){
            if(boardSpace.getBoardSpace(row, col))
                return row;
        }
        return -1;
    }
    
    public boolean fiveBombSwapped(){
        return fiveBombSwapped;
    }
    
    /**
     *
     *  adds a special Zombie's special ability to the list of matches.
     *
     * @param Zombie the special striped zombie
     */
    public void addSpecialZombieAffected(ZombieCrushSagaZombie zombie){
        
        if(!zombie.isSpecialZombie())
            return;
        
        if(zombie.getDirection().equals("VERTICAL"))
            stripedColumnsAffected.add(zombie.getGridColumn());
        else if(zombie.getDirection().equals("HORIZONTAL"))
            stripedRowsAffected.add(zombie.getGridRow());
        else if(zombie.getZombieState().contains("WRAPPED")){
            wrappedAffected.add(zombie.getGridRow());
            wrappedAffected.add(zombie.getGridColumn());
        }
        else if(zombie.getZombieState().contains("FIVE"))
            fiveBombAffected.add(zombie);
        else
            System.out.println("ZombieCrushSagaZombie addSpecialZombieAffected: No bueno!");
        
    }
    
    /**
     *
     *
     * @param matchType
     * @return int the score added. use if you want.
     */
    public int addBoardScore(int matchType){
        
        switch(matchType){//(currentBoardScoreInt * zombies) * multiplier
            case(1): //1 zombie
                currentBoardScoreInt += 20 * numMatches;
                return 20 * numMatches;
            case(2): //special 1 zombie - alter carefully.
                currentBoardScoreInt += 20 * numMatches;
                return 20 * numMatches;
            case(3): //3-in-a-row
                currentBoardScoreInt += (20 * 3) * numMatches;
                return (20 * 3) * numMatches;
            case(4): //4-in-a-row
                currentBoardScoreInt += (30 * 4) * numMatches;
                return (30 * 4) * numMatches;
            case(5): //5-in-a-row
                currentBoardScoreInt += (40 * 5) * numMatches;
                return (40 * 5) * numMatches;
            case(6): //L-in-a-row
                currentBoardScoreInt += (20 * 5) * numMatches;
                return (20 * 5) * numMatches;
            case(7): //T-in-a-row
                currentBoardScoreInt += (20 * 5) * numMatches;
                return (20 * 5) * numMatches;
            case(20):   //jelly
                currentBoardScoreInt += 1000;
                return 1000;
        }
        
        System.out.println("addBoardScore: you shouldn't be here");
        return 666666666;
        
    }
    
    public int getCurrentBoardScore() {
        return currentBoardScoreInt;
    }
    
    
    /**
     * setBoardCheatTwoFiveBombs
     * sets column 4 up with 5 color bombs.
     */
    public void setBoardCheatTwoFiveBombs() {
        for(int row = 0; row < BOARD_ROWS; row++){
            if(getZombie(row, 4) != null)
                getZombie(row, 4).setFiveColorBomb();
        }
    }
    
    public void setBoardCheatTwo(){
        
        ZombieCrushSagaZombie theZombie;
        //set a zombie checkerboard pattern.
        for(int row = 0; row < BOARD_ROWS; row++){
            for(int column = 0; column < BOARD_COLUMNS; column++){
                theZombie = getZombie(row, column);
                
                if(theZombie == null)
                    continue;
                
                theZombie.setZombieColor(ZOMBIE_YELLOW_STATE);
                if(row % 2 == 0){//row is even
                    if(column % 2 == 0)//column is even
                        theZombie.setZombieColor(ZOMBIE_BLUE_STATE);
                }
                else{//row is odd
                    if(column % 2 != 0)//column is oddn
                        theZombie.setZombieColor(ZOMBIE_BLUE_STATE);
                }
            }
        }
        
        theZombie = getZombie(7, 2);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(7, 3);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(7, 5);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        
        theZombie = getZombie(5, 3);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_RED_STATE);
        theZombie = getZombie(6, 3);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_RED_STATE);
        theZombie = getZombie(8, 3);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_RED_STATE);
    }
    
    public void setBoardCheatFour() {
        ZombieCrushSagaZombie theZombie;
        //set a zombie checkerboard pattern.
        for(int row = 0; row < BOARD_ROWS; row++){
            for(int column = 0; column < BOARD_COLUMNS; column++){
                theZombie = getZombie(row, column);
                
                if(theZombie == null)
                    continue;
                
                theZombie.setZombieColor(ZOMBIE_YELLOW_STATE);
                if(row % 2 == 0){//row is even
                    if(column % 2 == 0)//column is even
                        theZombie.setZombieColor(ZOMBIE_BLUE_STATE);
                }
                else{//row is odd
                    if(column % 2 != 0)//column is oddn
                        theZombie.setZombieColor(ZOMBIE_BLUE_STATE);
                }
            }
        }
        
        theZombie = getZombie(7, 2);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(7, 3);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(6, 4);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(7, 5);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        
        theZombie = getZombie(0, 0);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(1, 0);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(2, 1);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(3, 0);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        
    }
    
    public void setBoardCheatFive() {
        ZombieCrushSagaZombie theZombie;
        //set a zombie checkerboard pattern.
        for(int row = 0; row < BOARD_ROWS; row++){
            for(int column = 0; column < BOARD_COLUMNS; column++){
                theZombie = getZombie(row, column);
                
                if(theZombie == null)
                    continue;
                
                theZombie.setZombieColor(ZOMBIE_YELLOW_STATE);
                if(row % 2 == 0){//row is even
                    if(column % 2 == 0)//column is even
                        theZombie.setZombieColor(ZOMBIE_BLUE_STATE);
                }
                else{//row is odd
                    if(column % 2 != 0)//column is oddn
                        theZombie.setZombieColor(ZOMBIE_BLUE_STATE);
                }
            }
        }
        
        theZombie = getZombie(8, 2);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(8, 3);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(7, 4);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(8, 5);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(8, 6);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        
        theZombie = getZombie(0, 0);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(1, 0);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(2, 1);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(3, 0);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(4, 0);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
    }
    
    public void setBoardCheatL() {
        ZombieCrushSagaZombie theZombie;
        //set a zombie checkerboard pattern.
        for(int row = 0; row < BOARD_ROWS; row++){
            for(int column = 0; column < BOARD_COLUMNS; column++){
                theZombie = getZombie(row, column);
                
                if(theZombie == null)
                    continue;
                
                theZombie.setZombieColor(ZOMBIE_YELLOW_STATE);
                if(row % 2 == 0){//row is even
                    if(column % 2 == 0)//column is even
                        theZombie.setZombieColor(ZOMBIE_BLUE_STATE);
                }
                else{//row is odd
                    if(column % 2 != 0)//column is oddn
                        theZombie.setZombieColor(ZOMBIE_BLUE_STATE);
                }
            }
        }
        
        theZombie = getZombie(8, 1);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(8, 2);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(8, 4);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(6, 3);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(7, 3);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        
        theZombie = getZombie(8, 7);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(8, 6);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(8, 4);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(6, 5);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(7, 5);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        
        theZombie = getZombie(0, 1);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(0, 2);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(0, 4);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(1, 3);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(2, 3);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        
        theZombie = getZombie(0, 7);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(0, 6);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(0, 4);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(1, 5);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(2, 5);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        
    }
    
    public void setBoardCheatT() {
        
        ZombieCrushSagaZombie theZombie;
        //set a zombie checkerboard pattern.
        for(int row = 0; row < BOARD_ROWS; row++){
            for(int column = 0; column < BOARD_COLUMNS; column++){
                theZombie = getZombie(row, column);
                
                if(theZombie == null)
                    continue;
                
                theZombie.setZombieColor(ZOMBIE_YELLOW_STATE);
                if(row % 2 == 0){//row is even
                    if(column % 2 == 0)//column is even
                        theZombie.setZombieColor(ZOMBIE_BLUE_STATE);
                }
                else{//row is odd
                    if(column % 2 != 0)//column is oddn
                        theZombie.setZombieColor(ZOMBIE_BLUE_STATE);
                }
            }
        }
        
        theZombie = getZombie(8, 1);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(7, 1);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(5, 1);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(6, 0);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(6, 2);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        
        theZombie = getZombie(7, 7);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(7, 5);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(7, 4);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(6, 6);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(8, 6);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        
        theZombie = getZombie(0, 1);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(1, 1);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(3, 1);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(2, 0);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(2, 2);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        
        theZombie = getZombie(1, 4);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(1, 6);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(1, 7);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(0, 5);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        theZombie = getZombie(2, 5);
        if(theZombie != null)
            theZombie.setZombieColor(ZOMBIE_GREEN_STATE);
        
    }
    
    
    public void setBoardSpace(ZombieCrushSagaBoardSpace initBoardSpace){
        boardSpace = initBoardSpace;
    }
    
    public void setBoardJelly(ZombieCrushSagaBoardJelly initBoardJelly){
        boardJelly = initBoardJelly;
    }
    
    public void setSpecialActionExecuted(boolean state){
        specialActionExecuted = state;
    }
    
    public void useZombieSmasher(ZombieCrushSagaZombie zombie){
        
        zombieAffected = zombie;
        specialActionExecuted = true;
    }
    
    /**
     *
     */
    public void setLastBoardMatches(){
        for(int row = 0; row < BOARD_ROWS; row++){
            for(int column = 0; column < BOARD_COLUMNS; column++){
                lastBoardMatches[row][column] = boardMatches[row][column];
            }
        }
    }
    
    
    /**
     * get all the matches and then use this method to eliminate
     * anything in a row that was affected by a striped candy.
     *
     * @param row the row to eliminate minus the already match candies
     */
    public void setRowOneMatch(int row) {
        
        for(int column = 0; column < BOARD_COLUMNS; column++){
            if(boardMatches[row][column] == 0)
                boardMatches[row][column] = 1;
        }
        
    }
    
    /**
     * get all the matches and then use this method to eliminate
     * anything in a column that was affected by a striped candy.
     *
     * @param column the column to eliminate minus the already match candies
     */
    public void setColumnOneMatch(int column){
        
        for(int row = 0; row < BOARD_ROWS; row++){
            if(boardMatches[row][column] == 0)
                boardMatches[row][column] = 1;
        }
    }
    
    
    
    /*
     * swaps two zombie locations
     */
    public void swapTwo(ZombieCrushSagaZombie zombie1, ZombieCrushSagaZombie zombie2) {
        
        int tempGridRow = zombie1.getGridRow();
        int tempGridColumn = zombie1.getGridColumn();
        
        //update boardZombies to reflect the switch
        this.putZombie(zombie2.getGridRow(), zombie2.getGridColumn(), zombie1);
        this.putZombie(tempGridRow, tempGridColumn, zombie2);
        
        this.setSwappedZombie(zombie1);
        
    }
    
    /**
     * annotate that a fiveBomb was swapped for checking purposes. must do five bomb special
     *
     * @param zombie1
     * @param zombie2
     *
     */
    public void swapFiveBombTwo(ZombieCrushSagaZombie zombie1, ZombieCrushSagaZombie zombie2) {
        fiveBombSwapped = true;
        zombie1FiveBomb = zombie1;
        zombie2FiveBomb = zombie2;
    }
    
    /*
     * swaps two zombie locations, with one of them or both being the bomb
     */
    public void checkBoardFiveBombMatches(ZombieCrushSagaZombie zombie1, ZombieCrushSagaZombie zombie2) {
        
        boolean zombie1Special = zombie1.getZombieColor().equals(ZOMBIE_FIVE_STATE);
        boolean zombie2Special = zombie2.getZombieColor().equals(ZOMBIE_FIVE_STATE);
        numMatches++;
        
        int tempScore;
        
        //if both zombies are special, mark everything for deletion.
        if(zombie1Special && zombie2Special){
            for(int row = 0; row < BOARD_ROWS; row++){
                for(int column = 0; column < BOARD_COLUMNS; column++){
                    //if board is active
                    if(boardSpace.getBoardSpace(row, column)){
                        //if the space isn't being used for a match and there is a zombie.
                        if(boardMatches[row][column] == 0 && getZombie(row, column) != null){
                            if(getZombie(row, column).getZombieColor().equals(ZOMBIE_FIVE_STATE)){
                                boardMatches[row][column] = 2;
                                tempScore = addBoardScore(1);
                                
                                boardScore[row][column] = tempScore;
                                
                                //if it has a jelly, add the jelly score.
                                if(boardJelly.getBoardJelly(row, column))
                                    boardScore[row][column] += addBoardScore(20);
                                
                            }
                            else {//normal zombie affected
                                boardMatches[row][column] = 1;
                                tempScore = addBoardScore(1);
                                
                                boardScore[row][column] = tempScore;
                                
                                
                                //if it has a jelly, add the jelly score.
                                if(boardJelly.getBoardJelly(row, column))
                                    boardScore[row][column] += addBoardScore(20);
                                
                            }
                        }
                    }
                }
            }
            return;
        }
        String color;
        //since both are not special,
        //set one of them to the color to blow up entirely.
        //the specialZombie will be noted so that only it is deleted and not other color bombs.
        
        int specialRow;
        int specialColumn;
        if (!zombie1.isSpecialZombie()){
            color = zombie1.getZombieColor();
            specialRow = zombie2.getGridRow();
            specialColumn = zombie2.getGridColumn();
        }
        else {
            color = zombie2.getZombieColor();
            specialRow = zombie1.getGridRow();
            specialColumn = zombie1.getGridColumn();
        }
        //blow up only the certain color
        for(int row = 0; row < BOARD_ROWS; row++){
            for(int column = 0; column < BOARD_COLUMNS; column++){
                //if board is active
                if(boardSpace.getBoardSpace(row, column)){
                    //if the space isn't being used for a match and there is a zombie.
                    if(boardMatches[row][column] == 0 && getZombie(row, column) != null){
                        //if the zombie's color here is the color you want to blow up
                        if(getZombie(row, column).getZombieColor().equals(color)){
                            boardMatches[row][column] = 1;
                            tempScore = addBoardScore(1);
                            
                            boardScore[row][column] = tempScore;
                            
                            //if it has a jelly, add the jelly score.
                            if(boardJelly.getBoardJelly(row, column))
                                boardScore[row][column] += addBoardScore(20);
                            
                        }
                        //only this five bomb is marked for deletion
                        if(row == specialRow && column == specialColumn){
                            boardMatches[row][column] = 2;
                            tempScore = addBoardScore(1);
                            
                            boardScore[row][column] = tempScore;
                            
                            //if it has a jelly, add the jelly score.
                            if(boardJelly.getBoardJelly(row, column))
                                boardScore[row][column] += addBoardScore(20);
                        }
                        
                    }
                }
            }
        }
        
    }
    
    /*
     * goes through all zombies and returns true if it finds one 3-in-a-row.
     */
    public boolean hasMatches(){
        
        if (hasMatches == true)
            return true;
        
        //if nothing is dropping then there are no nulls, we can check.
        if(!checkBoardDrop()){
            
            resetBoardMatches();
            //go through all zombies and find one true.
            for(int row = 0; row < BOARD_ROWS; row++){
                for(int column = 0; column < BOARD_COLUMNS; column++){
                    if(checkMatchThreeRowQuickly(this.getZombie(row, column))){  //if true, return true.
                        return true;
                    }
                }
                
            }
        }
        
        return false; //nothing found
        
    }
    
    
    public boolean isValidRow(int row){
        return row >= 0 && row < BOARD_ROWS;
    }
    
    public boolean isValidColumn(int column){
        return column >= 0 && column < BOARD_COLUMNS;
    }
    
    /*
     * processMatches
     * checks and marks everything that needs to be deleted.
     *
     * clears the board of all the matches
     * and moves the zombies out of the grid and replaces them with nulls
     *
     * everything that needs to be dropped, gets dropped completely
     *
     * the processed board will have zombies filling every empty space.
     * the zombies that had to drop down are not in their correct x and y coordinates
     * they are in their "haven't dropped yet" x and y coordinates.
     */
    public void processMatches(){
        
        ZombieCrushSagaZombie theZombie;
        int rowCounter;                     //counts the unprocessed matches
        
        
        this.checkBoardMatches();
        
        multiplier = numMatches;
        
        setLastBoardMatches();
        
        //FIRSTLY, it checks to see if a fiveBomb was swapped,
        //or zombieSmasher active
        //THEN checks if there are matches.
        //note: do not check hasMatches first, due to odd dependencies.
        if(fiveBombSwapped || specialActionExecuted || hasMatches()){
            //take out all of the zombies that are part of a match.
            for(int row = 0; row < BOARD_ROWS; row++){
                for(int column = 0; column < BOARD_COLUMNS; column++){
                    if(boardMatches[row][column] > 0){//if this zombie is part of match, remove it.
                        theZombie = getZombie(row, column);
                        
                        theZombie.setState(INVISIBLE_STATE);
                        
                        unprocessedZombies.add(theZombie);
                        putZombie(row, column, null); //put a null in that place
                        
                    }
                }
            }
        }
        
        //add the special zombies
        for(int row = 0; row < BOARD_ROWS; row++){
            for(int column = 0; column < BOARD_COLUMNS; column++){
                
                //if this board is active, otherwise do nothing.
                if(boardSpace.getBoardSpace(row, column))
                    if(boardSpecialZombies[row][column] != 0){
                        theZombie = unprocessedZombies.remove(0);
                        
                        int color = boardSpecialZombies[row][column] % 10;
                        int specialType = boardSpecialZombies[row][column] / 10;
                        int direction = specialType % 10;
                        String directionStr = direction == 1 ? "HORIZONTAL" : "VERTICAL";
                        
                        if(specialType == 41 || specialType == 42)
                            theZombie.setStrippedColor(convertIntToZombieColor(color), directionStr);
                        else if(specialType == 5)
                            theZombie.setFiveColorBomb();
                        else if(specialType == 6)
                            theZombie.setWrappedColor(convertIntToZombieColor(color));
                        else{
                            System.out.println("processmatch: will i am, you got a problem");
                        }
                        
                        theZombie.setY(CANDY_FIRST_SPACE_Y + ((row + 1)  * BOARD_SPACE_SIZE));
                        theZombie.setX(CANDY_FIRST_SPACE_X + ((column) * BOARD_SPACE_SIZE));
                        putZombie(row, column, theZombie);
                        
                    }
            }
            
        }
        
        //check each column to see if anything needs to drop
        //move everything down by one if there are things that need to drop
        //we check from the bottom up cause the bottom stuff is changed before the top stuff
        for(int column = BOARD_COLUMNS - 1; column >= 0 ; column--){
            //if something in this column needs to drop.
            rowCounter = 0;
            while(checkColumnDrop(column)){ //if a column has things that need to be dropped, drop by one.
                //drop everything in this column by one
                for(int row = BOARD_ROWS - 1 ; row >= 0 ; row--){
                    theZombie = getZombie(row, column);
                    
                    //if the zombie is not there and the boardSpace is inactive, keep going.
                    if(theZombie == null && !boardSpace.getBoardSpace(row, column))
                        continue;
                    
                    if(theZombie != null)
                        
                        //check the space any below if it is open and empty.
                        for(int rowBelowRow = row; rowBelowRow < BOARD_ROWS; rowBelowRow++){
                            //check to see if we're going to spill over the max. if we are then no need to drop.
                            if(rowBelowRow + 1 < BOARD_ROWS)    //gotta still be within range.
                                if(getZombie(rowBelowRow + 1 ,column) == null && boardSpace.getBoardSpace(rowBelowRow + 1, column) ){
                                    
                                    boardZombies[rowBelowRow + 1][column] = theZombie;
                                    theZombie.setGridCell(rowBelowRow + 1, column);
                                    
                                    boardZombies[row][column] = null;       //erase the original zombie spot
                                    break;
                                }
                        }
                }
                
                //fill in the blank null spots in the first row with an unprocessedZombies.
                int row = this.getFirstActiveEmptyTopRow(column);
                if(getZombie(row, column) == null){
                    theZombie =  unprocessedZombies.remove(0);
                    putZombie(row, column, theZombie);
                    theZombie.setY(CANDY_FIRST_SPACE_Y - (rowCounter++ * BOARD_SPACE_SIZE));
                    theZombie.setX(CANDY_FIRST_SPACE_X + (column * BOARD_SPACE_SIZE));
                    theZombie.randomColor();
                }
                
            }//end while
        }
        
        //reset currentBoardScoreInt since it's all processed.
        //don't reset the numMatches. We need that for multiplier counting.
        resetBoardMatches();
        resetBoardSpecialZombies();
        fiveBombSwapped = false;
        swappedZombie = null;
        specialActionExecuted = false;
        zombieAffected = null;
        
        
    }
    
    /**
     * finds the first instance of a blank active space.
     *
     *
     * @param col
     * @return
     */
    public boolean checkColumnDrop(int col){
        
        ZombieCrushSagaZombie tempZombie;
        
        for(int row = 0; row < BOARD_ROWS; row++){
            tempZombie = getZombie(row, col);
            
            if(!boardSpace.getBoardSpace(row, col) && tempZombie == null)
                continue;
            
            //if boardspace is active and there is no zombie in it.
            if(boardSpace.getBoardSpace(row, col) && tempZombie == null)
                return true;
            
        }
        return false;   //nothing in this column needs to drop.
    }
    
    public boolean checkBoardDrop(){
        
        for(int column = 0; column < BOARD_COLUMNS; column++){
            if(checkColumnDrop(column)){
                return true;
            }
        }
        
        return false;
    }
    
    
    public boolean compareZombies(ZombieCrushSagaZombie zombie1, ZombieCrushSagaZombie zombie2){
        
        return (zombie1.getZombieState().equals(zombie2.getZombieState()));
        
    }
    
    /*
     * the board is randomized
     * it randomizes only the matching pieces.
     *
     */
    public void randomBoard(){
        
        while(hasMatches()){
            checkBoardMatches();    //marks all row/grid that is has a match
            
            //finds a zombie being used for matching and randomizes it.
            for(int row = 0; row < BOARD_ROWS; row++){
                for(int column = 0; column < BOARD_COLUMNS; column++){
                    if(boardMatches[row][column] > 0)
                        this.getZombie(row, column).randomColor();
                    
                }
                
            }
            
            resetBoardMatches(); //board is different now. must be rechecked for matches.
            resetBoardSpecialZombies();
        }
    }
    
    /**
     * converts int to zombieColor
     *
     * @retuns String zombiecolor
     * 1 = ZOMBIE_BLUE_STATE
     * 2 = ZOMBIE_GREEN_STATE
     * 3 = ZOMBIE_ORANGE_STATE
     * 4 = ZOMBIE_PURPLE_STATE
     * 5 = ZOMBIE_RED_STATE
     * 6 = ZOMBIE_YELLOW_STATE
     *
     */
    public String convertIntToZombieColor(int zombieColor){
        
        switch(zombieColor){
            case 1:
                return ZOMBIE_BLUE_STATE;
            case 2:
                return ZOMBIE_GREEN_STATE;
            case 3:
                return ZOMBIE_ORANGE_STATE;
            case 4:
                return ZOMBIE_PURPLE_STATE;
            case 5:
                return ZOMBIE_RED_STATE;
            case 6:
                return ZOMBIE_YELLOW_STATE;
            case 7:
                return ZOMBIE_FIVE_STATE;
            default:
                System.out.println("BoardZombies, convertIntToZombieColor" + "lol wrong place");
                return "666";
        }
    }
    
    /**
     * converts zombieColor to Int
     *
     * @retuns int
     * 1 = ZOMBIE_BLUE_STATE
     * 2 = ZOMBIE_GREEN_STATE
     * 3 = ZOMBIE_ORANGE_STATE
     * 4 = ZOMBIE_PURPLE_STATE
     * 5 = ZOMBIE_RED_STATE
     * 6 = ZOMBIE_YELLOW_STATE
     *
     */
    public int convertZombieColorToInt(String zombieColor){
        switch(zombieColor){
            case ZOMBIE_BLUE_STATE:
                return 1;
            case ZOMBIE_GREEN_STATE:
                return 2;
            case ZOMBIE_ORANGE_STATE:
                return 3;
            case ZOMBIE_PURPLE_STATE:
                return 4;
            case ZOMBIE_RED_STATE:
                return 5;
            case ZOMBIE_YELLOW_STATE:
                return 6;
            case ZOMBIE_FIVE_STATE:
                return 7;
            default:
                System.out.println("BoardZombies, convertZombieColortoInt" + "lol wrong place");
                return -999;
        }
    }
    
    
    /*
     * checks the entire board for matches and modifies the boardMatches
     * to reflect. marks matches for deletion. does not delete.
     * call processMatches alone, if you want to delete stuff.
     *
     * the matching is done by a certain precedence. They are:
     * 1. 5 in a row
     * 2. L
     * 3. T
     * 4. 4 in a row
     * 5. 3 in a row
     * note: changing the order will change the gameplay
     *
     * tl;dr
     * marks boardScore with scores before explosion
     * marks zombies for deletion
     */
    public void checkBoardMatches(){
        
        ZombieCrushSagaZombie theZombie;
        
        //reset the all necessary boards since this is a new board to check.
        resetBoardMatches();
        resetBoardSpecialZombies();
        resetBoardScore();
        
        
        numMatches = multiplier;
        
        //check every zombie for a special match.
        //this algorithm is probably not optimal but who cares? we got
        //portable super computers!
        //also this game isn't that big.
        //check all zombies for 5-in-a row
        for(int row = 0; row < BOARD_ROWS; row++){
            for(int column = 0; column < BOARD_COLUMNS; column++){
                theZombie = this.getZombie(row, column);
                checkMatchFiveRow(theZombie);
            }
        }
        //check all zombies for L-in-a row
        for(int row = 0; row < BOARD_ROWS; row++){
            for(int column = 0; column < BOARD_COLUMNS; column++){
                theZombie = this.getZombie(row, column);
                checkMatchLRow(theZombie);
            }
        }
        //check all zombies for T-in-a row
        for(int row = 0; row < BOARD_ROWS; row++){
            for(int column = 0; column < BOARD_COLUMNS; column++){
                theZombie = this.getZombie(row, column);
                checkMatchTRow(theZombie);
            }
        }
        //check all zombies for 4-in-a row
        for(int row = 0; row < BOARD_ROWS; row++){
            for(int column = 0; column < BOARD_COLUMNS; column++){
                theZombie = this.getZombie(row, column);
                checkMatchFourRow(theZombie);
            }
        }
        
        //check all zombies for 3-in-a row
        for(int row = 0; row < BOARD_ROWS; row++){
            for(int column = 0; column < BOARD_COLUMNS; column++){
                theZombie = this.getZombie(row, column);
                checkMatchThreeRow(theZombie);
            }
        }
        
        //all -1's are switched to 0s, no match
        for(int row = 0; row < BOARD_ROWS; row++){
            for(int column = 0; column < BOARD_COLUMNS; column++){
                if(boardMatches[row][column] == -1)
                    boardMatches[row][column] = 0;
            }
        }
        
        //do special stuff
        
        //if fives have been swapped, mark it all accordingly.
        if(fiveBombSwapped)
            this.checkBoardFiveBombMatches(zombie1FiveBomb, zombie2FiveBomb);
        
        //if the zombie crusher has been activated
        if(specialActionExecuted){
            int row = zombieAffected.getGridRow();
            int column = zombieAffected.getGridColumn();
            
            numMatches++;
            boardMatches[row][column] = 1;
            
            int tempScore = addBoardScore(1);
            boardScore[row][column] = tempScore;
            
            //if it has a jelly, add the jelly score.
            if(boardJelly.getBoardJelly(row, column))
                boardScore[row][column] += addBoardScore(20);
            
        }
        
        //will keep checking as long as there are more specials affected by specials.
        boolean keepChecking = true;
        
        while(keepChecking){
            keepChecking = false;
            
            //check for any special explosions
            checkBoardSpecialMatches();
            
            //check to see if there are any more special explosions affected by the special explosions
            //take out all of the zombies that are part of a match.
            for(int row = 0; row < BOARD_ROWS; row++){
                for(int column = 0; column < BOARD_COLUMNS; column++){
                    theZombie = getZombie(row, column);
                    
                    if(theZombie == null)
                        continue;
                    
                    //if the boardMatch is a 1 and the zombie is special
                    //we need to blow it up and affect the other zombies.
                    if(boardMatches[row][column] == 1 && theZombie.isSpecialZombie()){
                        boardMatches[row][column] = 2;
                        this.addSpecialZombieAffected(theZombie);
                        this.checkBoardSpecialMatches();
                        keepChecking = true;
                    }
                    
                }
            }
        }
        
        //        System.out.println("boardmatches\n" + this.toStringBoardMatches());
        //        System.out.println("boardSpecialZombies\n" + this.toStringBoardSpecialZombies());
        //        System.out.println("check final boardScore: boardScore\n" + this.toStringBoardScore());
    }
    
    public void checkBoardSpecialMatches(){
        
        //check for unused Match space, zombie isn't null, space is used.
        
        //stripped zombies affect certain 0s
        int row, column;
        int tempScore;
        while(!stripedColumnsAffected.isEmpty()){
            column = stripedColumnsAffected.remove(0);
            for(row = 0; row < BOARD_ROWS; row++){
                if(boardMatches[row][column] == 0 && getZombie(row, column) != null
                        && boardSpace.getBoardSpace(row, column)){
                    boardMatches[row][column] = 1;
                    tempScore = addBoardScore(1);
                    
                    boardScore[row][column] = tempScore;
                    
                    //if it has a jelly, add the jelly score.
                    if(boardJelly.getBoardJelly(row, column))
                        boardScore[row][column] += addBoardScore(20);
                    
                }
            }
        }
        while(!stripedRowsAffected.isEmpty()){
            row = stripedRowsAffected.remove(0);
            for(column = 0; column < BOARD_COLUMNS; column++){
                if(boardMatches[row][column] == 0 && getZombie(row, column) != null
                        && boardSpace.getBoardSpace(row, column)){
                    boardMatches[row][column] = 1;
                    tempScore = addBoardScore(1);
                    
                    boardScore[row][column] = tempScore;
                    
                    //if it has a jelly, add the jelly score.
                    if(boardJelly.getBoardJelly(row, column))
                        boardScore[row][column] += addBoardScore(20);
                    
                }
            }
        }
        
        //wrapped zombies affect certain 0s
        while(!wrappedAffected.isEmpty()){
            int theRow = wrappedAffected.remove(0);
            int theColumn = wrappedAffected.remove(0);
            
            for(row = theRow - 1; row < theRow + 2; row++){
                for(column = theColumn - 1; column < theColumn + 2; column++){
                    if(isValidRow(row) && isValidColumn(column)){
                        if(boardMatches[row][column] == 0 && getZombie(row, column) != null
                                && boardSpace.getBoardSpace(row, column)){
                            boardMatches[row][column] = 1;
                            tempScore = addBoardScore(1);
                            
                            boardScore[row][column] = tempScore;
                            
                            //if it has a jelly, add the jelly score.
                            if(boardJelly.getBoardJelly(row, column))
                                boardScore[row][column] += addBoardScore(20);
                            
                        }
                        
                    }
                }
            }
        }
        
        //color bombs affected by special zombies will affect all zombies of a certain set random color.
        while(!fiveBombAffected.isEmpty()){
            String randColor = fiveBombAffected.remove(0).randomFiveBombColor();
            
            for(row = 0; row < BOARD_ROWS; row++){
                for(column = 0; column < BOARD_COLUMNS; column++){
                    //if board is active
                    if(boardSpace.getBoardSpace(row, column)){
                        //if the space isn't being used for a match and there is a zombie.
                        if(boardMatches[row][column] == 0 && getZombie(row, column) != null){
                            //if the zombie there is the same as the matched color, blow it up
                            if(this.getZombie(row, column).getZombieColor().equals(randColor)){
                                boardMatches[row][column] = 1;
                                tempScore = addBoardScore(1);
                                
                                boardScore[row][column] = tempScore;
                                
                                //if it has a jelly, add the jelly score.
                                if(boardJelly.getBoardJelly(row, column))
                                    boardScore[row][column] += addBoardScore(20);
                            }
                        }
                        
                    }
                    
                }
            }
            
        }
        
    }
    
    /*
     * modifies the boardMatches and sets matches.
     * fills in 5s whereever there are spaces with 5-in-a-row
     */
    public void checkMatchFiveRow(ZombieCrushSagaZombie originalZombie) {
        
        if(originalZombie == null)
            return;
        
        int originalRow = originalZombie.getGridRow();
        int originalColumn = originalZombie.getGridColumn();
        boolean spacesActive = false;
        int tempScore;
        
        //check below
        //if spaces are active, then we check.
        if(originalRow + 4 < BOARD_ROWS){
            spacesActive = true;
            for(int i = 0; i < 5; i++){
                if(!boardSpace.getBoardSpace(originalRow + i, originalColumn))
                    spacesActive = false;
            }
        }
        if(spacesActive)
            if(originalRow + 4 < BOARD_ROWS){ //check for valid range
                if(boardMatches[originalRow][originalColumn] == -1)
                    if(originalZombie.compareZombie(getZombie(originalRow + 1, originalColumn)) && boardMatches[originalRow + 1][originalColumn] == -1)
                        if(originalZombie.compareZombie(getZombie(originalRow + 2, originalColumn)) && boardMatches[originalRow + 2][originalColumn] == -1)
                            if(originalZombie.compareZombie(getZombie(originalRow + 3, originalColumn)) && boardMatches[originalRow + 3][originalColumn] == -1)
                                if(originalZombie.compareZombie(getZombie(originalRow + 4, originalColumn)) && boardMatches[originalRow + 4][originalColumn] == -1){
                                    numMatches++;
                                    tempScore = addBoardScore(5) / 5;
                                    
                                    for(int i = 0; i < 5; i++) {
                                        boardMatches[originalRow + i][originalColumn] = 5;
                                        boardScore[originalRow + i][originalColumn] = tempScore;
                                        
                                        //if the space is a jelly, add it there.
                                        if(boardJelly.getBoardJelly(originalRow + i, originalColumn))
                                            boardScore[originalRow + i][originalColumn] += addBoardScore(20);
                                        
                                        //if a zombie is special, take note of what row it is so we can blow it up later.
                                        if(getZombie(originalRow + i, originalColumn).isSpecialZombie())
                                            this.addSpecialZombieAffected(getZombie(originalRow + i, originalColumn));
                                    }
                                    
                                    
                                    hasMatches = true;
                                    //we will note this as a future special candy location.
                                    
                                    //check to see if a swapped zombie resulted in the match.
                                    //if so, the swapped zombie must become the special zombie.
                                    //otherwise just place it in somewhere in the middle location.
                                    if(swappedZombie != null){
                                        //if the swapped zombie in the same column and within the matches's range.
                                        if(swappedZombie.getGridColumn() == originalColumn){
                                            if(swappedZombie.getGridRow() >= originalRow && swappedZombie.getGridRow() <= originalRow + 4)
                                                boardSpecialZombies[swappedZombie.getGridRow()][swappedZombie.getGridColumn()] = 51;
                                        }
                                        else
                                            boardSpecialZombies[originalRow + 2][originalColumn] = 51;//the middle one is special.
                                        
                                        
                                    }
                                    else    //place somewhere in the middle
                                        boardSpecialZombies[originalRow + 2][originalColumn] = 51;//the middle one is special.
                                }
            }
        
        //check right
        //if spaces are active, then we check.
        spacesActive = false;
        if(originalColumn + 4 < BOARD_COLUMNS){
            spacesActive = true;
            for(int i = 0; i < 5; i++){
                if(!boardSpace.getBoardSpace(originalRow, originalColumn + i))
                    spacesActive = false;
            }
        }
        if(spacesActive)
            if(originalColumn + 4 < BOARD_COLUMNS){ //check for valid range
                if(boardMatches[originalRow][originalColumn] == -1)
                    if(originalZombie.compareZombie(getZombie(originalRow, originalColumn + 1)) && boardMatches[originalRow][originalColumn + 1] == -1)
                        if(originalZombie.compareZombie(getZombie(originalRow, originalColumn + 2)) && boardMatches[originalRow][originalColumn + 2] == -1)
                            if(originalZombie.compareZombie(getZombie(originalRow, originalColumn + 3)) && boardMatches[originalRow][originalColumn + 3] == -1)
                                if(originalZombie.compareZombie(getZombie(originalRow, originalColumn + 4)) && boardMatches[originalRow][originalColumn + 4] == -1){
                                    numMatches++;
                                    tempScore = addBoardScore(5) / 5;
                                    
                                    for(int i = 0; i < 5; i++) {
                                        boardMatches[originalRow][originalColumn + i] = 5;
                                        boardScore[originalRow][originalColumn + i] = tempScore;
                                        
                                        //if the space is a jelly, add it there.
                                        if(boardJelly.getBoardJelly(originalRow, originalColumn + i))
                                            boardScore[originalRow][originalColumn + i] += addBoardScore(20);
                                        
                                        //if a zombie is special, take note of what row it is so we can blow it up later.
                                        if(getZombie(originalRow, originalColumn + i).isSpecialZombie())
                                            this.addSpecialZombieAffected(getZombie(originalRow, originalColumn + i));
                                    }
                                    
                                    hasMatches = true;
                                    
                                    //check to see if a swapped zombie resulted in the match.
                                    //if so, the swapped zombie must become the special zombie.
                                    //otherwise just place it in somewhere in the middle location.
                                    if(swappedZombie != null){
                                        //if the swapped zombie in the same column and within the matches's range.
                                        if(swappedZombie.getGridRow() == originalRow){
                                            if(swappedZombie.getGridColumn() >= originalColumn && swappedZombie.getGridColumn() <= originalColumn + 4)
                                                boardSpecialZombies[swappedZombie.getGridRow()][swappedZombie.getGridColumn()] = 51;
                                        }
                                        else
                                            boardSpecialZombies[originalRow][originalColumn + 2] = 51;//the middle one is special.
                                        
                                    }
                                    else    //place somewhere in the middle
                                        boardSpecialZombies[originalRow][originalColumn + 2] = 51;//the middle one is special.
                                }
            }
        
    }
    
    
    /*
     * modifies the boardMatches and sets matches.
     * fills in 4s whereever there are spaces with 4-in-a-row
     */
    public void checkMatchFourRow(ZombieCrushSagaZombie originalZombie) {
        
        if(originalZombie == null)
            return;
        
        int originalRow = originalZombie.getGridRow();
        int originalColumn = originalZombie.getGridColumn();
        boolean spacesActive = false;
        int tempScore;
        
        //check for
        //1. same colors as the original
        //2. empty boardMatches space for no-repeat scoring purposes.
        
        //check below
        //if spaces are active, then we check.
        spacesActive = true;
        if(originalRow + 3 < BOARD_ROWS){
            for(int i = 0; i < 4; i++){
                if(!boardSpace.getBoardSpace(originalRow + i, originalColumn))
                    spacesActive = false;
            }
        }
        if(spacesActive)
            if(originalRow + 3 < BOARD_ROWS){ //check for valid range
                if(boardMatches[originalRow][originalColumn] == -1)
                    if(originalZombie.compareZombie(getZombie(originalRow + 1, originalColumn)) && boardMatches[originalRow + 1][originalColumn] == -1)
                        if(originalZombie.compareZombie(getZombie(originalRow + 2, originalColumn)) && boardMatches[originalRow + 2][originalColumn] == -1)
                            if(originalZombie.compareZombie(getZombie(originalRow + 3, originalColumn)) && boardMatches[originalRow + 3][originalColumn] == -1){
                                numMatches++;
                                tempScore = addBoardScore(4) / 4;
                                
                                for(int i = 0; i < 4; i++) {
                                    boardMatches[originalRow + i][originalColumn] = 4;
                                    boardScore[originalRow + i][originalColumn] = tempScore;
                                    
                                    //if the space is a jelly, add it there.
                                    if(boardJelly.getBoardJelly(originalRow + i, originalColumn))
                                        boardScore[originalRow + i][originalColumn] += addBoardScore(20);
                                    
                                    //if a zombie is special, take note of what row it is so we can blow it up later.
                                    if(getZombie(originalRow + i, originalColumn).isSpecialZombie())
                                        this.addSpecialZombieAffected(getZombie(originalRow + i, originalColumn));
                                }
                                
                                hasMatches = true;
                                
                                int color;
                                color = convertZombieColorToInt(originalZombie.getZombieColor());
                                
                                //check to see if a swapped zombie resulted in the match.
                                //if so, the swapped zombie must become the special zombie.
                                //otherwise just place it in somewhere in the middle location.
                                //4 = 4 in a row, 10 = vertical, color = color
                                if(swappedZombie != null){
                                    //if the swapped zombie in the same column and within the matches's range.
                                    if(swappedZombie.getGridColumn() == originalColumn){
                                        if(swappedZombie.getGridRow() >= originalRow && swappedZombie.getGridRow() <= originalRow + 3)
                                            boardSpecialZombies[swappedZombie.getGridRow()][swappedZombie.getGridColumn()] = 400 + 10 + color;
                                        
                                    }
                                    else
                                        boardSpecialZombies[originalRow + 2][originalColumn] = 400 + 10 + color;//the middle one is special.
                                    
                                }
                                else    //place somewhere in the middle.
                                    boardSpecialZombies[originalRow + 2][originalColumn] = 400 + 10 + color;//the middle one is special.
                            }
            }
        
        //check right
        //if spaces are active, then we check.
        spacesActive = false;
        if(originalColumn + 3 < BOARD_COLUMNS){
            spacesActive = true;
            for(int i = 0; i < 4; i++){
                if(!boardSpace.getBoardSpace(originalRow, originalColumn + i))
                    spacesActive = false;
            }
        }
        if(spacesActive)
            if(originalColumn + 3 < BOARD_COLUMNS){ //check for valid range
                if(boardMatches[originalRow][originalColumn] == -1)
                    if(originalZombie.compareZombie(getZombie(originalRow, originalColumn + 1)) && boardMatches[originalRow][originalColumn + 1] == -1)
                        if(originalZombie.compareZombie(getZombie(originalRow, originalColumn + 2)) && boardMatches[originalRow][originalColumn + 2] == -1)
                            if(originalZombie.compareZombie(getZombie(originalRow, originalColumn + 3)) && boardMatches[originalRow][originalColumn + 3] == -1){
                                numMatches++;
                                tempScore = addBoardScore(4) / 4;
                                
                                for(int i = 0; i < 4; i++) {
                                    boardMatches[originalRow][originalColumn + i] = 4;
                                    boardScore[originalRow][originalColumn + i] = tempScore;
                                    
                                    //if the space is a jelly, add it there.
                                    if(boardJelly.getBoardJelly(originalRow, originalColumn + i))
                                        boardScore[originalRow][originalColumn + i] += addBoardScore(20);
                                    
                                    //if a zombie is special, take note of what row it is so we can blow it up later.
                                    if(getZombie(originalRow, originalColumn + i).isSpecialZombie())
                                        this.addSpecialZombieAffected(getZombie(originalRow, originalColumn + i));
                                }
                                
                                hasMatches = true;
                                
                                int color;
                                color = convertZombieColorToInt(originalZombie.getZombieColor());
                                
                                //check to see if a swapped zombie resulted in the match.
                                //if so, the swapped zombie must become the special zombie.
                                //otherwise just place it in somewhere in the middle location.
                                //4 = 4 in a row, 10 = vertical, color = color
                                if(swappedZombie != null){
                                    //if the swapped zombie in the same column and within the matches's range.
                                    if(swappedZombie.getGridRow() == originalRow){
                                        if(swappedZombie.getGridColumn() >= originalColumn && swappedZombie.getGridColumn() <= originalColumn + 3)
                                            boardSpecialZombies[swappedZombie.getGridRow()][swappedZombie.getGridColumn()] = 400 + 20 + color;
                                    }
                                    else
                                        boardSpecialZombies[originalRow][originalColumn + 2] = 400 + 20 + color;//the middle one is special.
                                    
                                    
                                }
                                else    //place somewhere in the middle
                                    boardSpecialZombies[originalRow][originalColumn + 2] = 400 + 20 + color;//the middle one is special.
                            }
            }
        
    }
    
    /*
     * modifies the boardMatches and sets matches.
     * fills in 4s whereever there are spaces with 4-in-a-row
     */
    public void checkMatchThreeRow(ZombieCrushSagaZombie originalZombie) {
        
        if(originalZombie == null)
            return;
        
        int originalRow = originalZombie.getGridRow();
        int originalColumn = originalZombie.getGridColumn();
        int tempScore;
        
        //check for
        //1. same colors as the original
        //2. empty boardMatches space for no-repeat scoring purposes.
        
        //check below
        //if spaces are active, then we check.
        
        if(originalRow + 2 < BOARD_ROWS){
            for(int i = 0; i < 2; i++){
                if(!boardSpace.getBoardSpace(originalRow + i, originalColumn)) {
				}
            }
        }
        if(originalRow + 2 < BOARD_ROWS){ //check for valid range
            if(boardMatches[originalRow][originalColumn] == -1)
                if(originalZombie.compareZombie(getZombie(originalRow + 1, originalColumn)) && boardMatches[originalRow + 1][originalColumn] == -1)
                    if(originalZombie.compareZombie(getZombie(originalRow + 2, originalColumn)) && boardMatches[originalRow + 2][originalColumn] == -1){
                        numMatches++;
                        tempScore = addBoardScore(3)/ 3;
                        
                        for(int i = 0; i < 3; i++) {
                            boardMatches[originalRow + i][originalColumn] = 3;
                            boardScore[originalRow + i][originalColumn] = tempScore;
                            
                            //if the space is a jelly, add it there.
                            if(boardJelly.getBoardJelly(originalRow + i, originalColumn))
                                boardScore[originalRow + i][originalColumn] += addBoardScore(20);
                            
                            //if a zombie is special, take note of what row it is so we can blow it up later.
                            if(getZombie(originalRow + i, originalColumn).isSpecialZombie())
                                this.addSpecialZombieAffected(getZombie(originalRow + i, originalColumn));
                        }
                        
                        hasMatches = true;
                    }
        }
        
        //check right
        //if spaces are active, then we check.
        
        if(originalColumn + 2 < BOARD_COLUMNS){ //check for valid range
            for(int i = 0; i < 2; i++){
                if(!boardSpace.getBoardSpace(originalRow, originalColumn + i)) {
				}
            }
        }
        if(originalColumn + 2 < BOARD_COLUMNS){ //check for valid range
            if(boardMatches[originalRow][originalColumn] == -1)
                if(originalZombie.compareZombie(getZombie(originalRow, originalColumn + 1)) && boardMatches[originalRow][originalColumn + 1] == -1)
                    if(originalZombie.compareZombie(getZombie(originalRow, originalColumn + 2)) && boardMatches[originalRow][originalColumn + 2] == -1){
                        numMatches++;
                        tempScore = addBoardScore(3) / 3;
                        for(int i = 0; i < 3; i++) {
                            boardMatches[originalRow][originalColumn + i] = 3;
                            boardScore[originalRow][originalColumn + i] = tempScore;
                            
                            //if the space is a jelly, add it there.
                            if(boardJelly.getBoardJelly(originalRow, originalColumn + i))
                                boardScore[originalRow][originalColumn + i] += addBoardScore(20);
                            
                            //if a zombie is special, take note of what row it is so we can blow it up later.
                            if(getZombie(originalRow, originalColumn + i).isSpecialZombie())
                                this.addSpecialZombieAffected(getZombie(originalRow, originalColumn + i));
                        }
                        
                        hasMatches = true;
                    }
        }
        
    }
    
    
    
    /*
     * modifies the boardMatches and sets matches.
     * fills in 6s whereever there are spaces with L-in-a-row
     */
    public void checkMatchLRow(ZombieCrushSagaZombie originalZombie) {
        
        if(originalZombie == null)
            return;
        
        int originalRow = originalZombie.getGridRow();
        int originalColumn = originalZombie.getGridColumn();
        int tempScore;
        
        //check for
        //1. same colors as the original
        //2. empty boardMatches space for no-repeat scoring purposes.
        
        //check |_
        //if spaces are active, then we check.
        if(originalRow - 2 >=0 && originalColumn + 2 < BOARD_COLUMNS){
            for(int i = 0; i < 3; i++){
                if(!boardSpace.getBoardSpace(originalRow - i, originalColumn)) {
				}
            }
            for(int i = 0; i < 3; i++){
                if(!boardSpace.getBoardSpace(originalRow, originalColumn + i)) {
				}
            }
        }
        if(originalRow - 2 >=0 && originalColumn + 2 < BOARD_COLUMNS){ //check for valid range
            if(boardMatches[originalRow][originalColumn] == -1)
                if(originalZombie.compareZombie(getZombie(originalRow - 1, originalColumn)) && boardMatches[originalRow - 1][originalColumn] == -1)
                    if(originalZombie.compareZombie(getZombie(originalRow - 2, originalColumn)) && boardMatches[originalRow - 2][originalColumn] == -1)
                        if(originalZombie.compareZombie(getZombie(originalRow, originalColumn + 1)) && boardMatches[originalRow][originalColumn + 1] == -1)
                            if(originalZombie.compareZombie(getZombie(originalRow, originalColumn + 2)) && boardMatches[originalRow][originalColumn + 2] == -1){
                                numMatches++;
                                tempScore = addBoardScore(6) / 5;
                                
                                for(int i = 0; i < 3; i++) {
                                    boardMatches[originalRow - i][originalColumn] = 6;
                                    boardScore[originalRow - i][originalColumn] = tempScore;
                                    
                                    //if the space is a jelly, add it there.
                                    if(boardJelly.getBoardJelly(originalRow - i, originalColumn))
                                        boardScore[originalRow - i][originalColumn] += addBoardScore(20);
                                    
                                    //if a zombie is special, take note of what row it is so we can blow it up later.
                                    if(getZombie(originalRow - i, originalColumn).isSpecialZombie())
                                        this.addSpecialZombieAffected(getZombie(originalRow - i, originalColumn));
                                }
                                for(int i = 1; i < 3; i++) {
                                    boardMatches[originalRow][originalColumn + i] = 6;
                                    boardScore[originalRow][originalColumn + i] = tempScore;
                                    
                                    //if the space is a jelly, add it there.
                                    if(boardJelly.getBoardJelly(originalRow, originalColumn + i))
                                        boardScore[originalRow][originalColumn + i] += addBoardScore(20);
                                    
                                    //if a zombie is special, take note of what row it is so we can blow it up later.
                                    if(getZombie(originalRow, originalColumn + i).isSpecialZombie())
                                        this.addSpecialZombieAffected(getZombie(originalRow, originalColumn + i));
                                }
                                
                                hasMatches = true;
                                
                                int color;
                                color = convertZombieColorToInt(originalZombie.getZombieColor());
                                
                                boardSpecialZombies[originalRow][originalColumn] = 60 + color;//the intersection one is special.
                            }
        }
        
        if(originalRow - 2 >=0 && originalColumn - 2 >= 0){ //check for valid range
            for(int i = 0; i < 3; i++){
                if(!boardSpace.getBoardSpace(originalRow - i, originalColumn)) {
				}
            }
            for(int i = 0; i < 3; i++){
                if(!boardSpace.getBoardSpace(originalRow, originalColumn - i)) {
				}
            }
        }
        if(originalRow - 2 >=0 && originalColumn - 2 >= 0){ //check for valid range
            if(boardMatches[originalRow][originalColumn] == -1)
                if(originalZombie.compareZombie(getZombie(originalRow - 1, originalColumn)) && boardMatches[originalRow - 1][originalColumn] == -1)
                    if(originalZombie.compareZombie(getZombie(originalRow - 2, originalColumn)) && boardMatches[originalRow - 2][originalColumn] == -1)
                        if(originalZombie.compareZombie(getZombie(originalRow, originalColumn - 1)) && boardMatches[originalRow][originalColumn - 1] == -1)
                            if(originalZombie.compareZombie(getZombie(originalRow, originalColumn - 2)) && boardMatches[originalRow][originalColumn - 2] == -1){
                                numMatches++;
                                tempScore = addBoardScore(6) / 5;
                                
                                for(int i = 0; i < 3; i++) {
                                    boardMatches[originalRow - i][originalColumn] = 6;
                                    boardScore[originalRow - i][originalColumn] = tempScore;
                                    
                                    //if the space is a jelly, add it there.
                                    if(boardJelly.getBoardJelly(originalRow - i, originalColumn))
                                        boardScore[originalRow - i][originalColumn] += addBoardScore(20);
                                    
                                    //if a zombie is special, take note of what row it is so we can blow it up later.
                                    if(getZombie(originalRow - i, originalColumn).isSpecialZombie())
                                        this.addSpecialZombieAffected(getZombie(originalRow - i, originalColumn));
                                }
                                for(int i = 1; i < 3; i++) {
                                    boardMatches[originalRow][originalColumn - i] = 6;
                                    boardScore[originalRow][originalColumn - i] = tempScore;
                                    
                                    //if the space is a jelly, add the jelly score there.
                                    if(boardJelly.getBoardJelly(originalRow, originalColumn - i))
                                        boardScore[originalRow][originalColumn - i] += addBoardScore(20);
                                    
                                    //if a zombie is special, take note of what row it is so we can blow it up later.
                                    if(getZombie(originalRow, originalColumn - i).isSpecialZombie())
                                        this.addSpecialZombieAffected(getZombie(originalRow, originalColumn - i));
                                }
                                
                                hasMatches = true;
                                
                                int color;
                                color = convertZombieColorToInt(originalZombie.getZombieColor());
                                
                                boardSpecialZombies[originalRow][originalColumn] = 60 + color;//the intersection one is special.
                            }
        }
        
        if(originalRow + 2 < BOARD_ROWS && originalColumn - 2 >= 0){ //check for valid range
            for(int i = 0; i < 3; i++){
                if(!boardSpace.getBoardSpace(originalRow + i, originalColumn)) {
				}
            }
            for(int i = 0; i < 3; i++){
                if(!boardSpace.getBoardSpace(originalRow, originalColumn - i)) {
				}
            }
        }
        if(originalRow + 2 < BOARD_ROWS && originalColumn - 2 >= 0){ //check for valid range
            if(boardMatches[originalRow][originalColumn] == -1)
                if(originalZombie.compareZombie(getZombie(originalRow + 1, originalColumn)) && boardMatches[originalRow + 1][originalColumn] == -1)
                    if(originalZombie.compareZombie(getZombie(originalRow + 2, originalColumn)) && boardMatches[originalRow + 2][originalColumn] == -1)
                        if(originalZombie.compareZombie(getZombie(originalRow, originalColumn - 1)) && boardMatches[originalRow][originalColumn - 1] == -1)
                            if(originalZombie.compareZombie(getZombie(originalRow, originalColumn - 2)) && boardMatches[originalRow][originalColumn - 2] == -1){
                                numMatches++;
                                tempScore = addBoardScore(6) / 5;
                                
                                for(int i = 0; i < 3; i++) {
                                    boardMatches[originalRow + i][originalColumn] = 6;
                                    boardScore[originalRow + i][originalColumn] = tempScore;
                                    
                                    //if the space is a jelly, add the jelly score there.
                                    if(boardJelly.getBoardJelly(originalRow + i, originalColumn))
                                        boardScore[originalRow + i][originalColumn] += addBoardScore(20);
                                    
                                    //if a zombie is special, take note of what row it is so we can blow it up later.
                                    if(getZombie(originalRow + i, originalColumn).isSpecialZombie())
                                        this.addSpecialZombieAffected(getZombie(originalRow + i, originalColumn));
                                }
                                for(int i = 1; i < 3; i++) {
                                    boardMatches[originalRow][originalColumn - i] = 6;
                                    boardScore[originalRow][originalColumn - i] = tempScore;
                                    
                                    //if the space is a jelly, add the jelly score there.
                                    if(boardJelly.getBoardJelly(originalRow, originalColumn - i))
                                        boardScore[originalRow][originalColumn - i] += addBoardScore(20);
                                    
                                    //if a zombie is special, take note of what row it is so we can blow it up later.
                                    if(getZombie(originalRow, originalColumn - i).isSpecialZombie())
                                        this.addSpecialZombieAffected(getZombie(originalRow, originalColumn - i));
                                }
                                
                                hasMatches = true;
                                
                                int color;
                                color = convertZombieColorToInt(originalZombie.getZombieColor());
                                
                                boardSpecialZombies[originalRow][originalColumn] = 60 + color;//the intersection one is special.
                            }
        }
        
        if(originalRow + 2 < BOARD_ROWS && originalColumn + 2 < BOARD_COLUMNS){ //check for valid range
            
            for(int i = 0; i < 3; i++){
                if(!boardSpace.getBoardSpace(originalRow + i, originalColumn)) {
				}
            }
            for(int i = 0; i < 3; i++){
                if(!boardSpace.getBoardSpace(originalRow, originalColumn + i)) {
				}
            }
        }
        if(originalRow + 2 < BOARD_ROWS && originalColumn + 2 < BOARD_COLUMNS){ //check for valid range
            if(boardMatches[originalRow][originalColumn] == -1)
                if(originalZombie.compareZombie(getZombie(originalRow + 1, originalColumn)) && boardMatches[originalRow + 1][originalColumn] == -1)
                    if(originalZombie.compareZombie(getZombie(originalRow + 2, originalColumn)) && boardMatches[originalRow + 2][originalColumn] == -1)
                        if(originalZombie.compareZombie(getZombie(originalRow, originalColumn + 1)) && boardMatches[originalRow][originalColumn + 1] == -1)
                            if(originalZombie.compareZombie(getZombie(originalRow, originalColumn + 2)) && boardMatches[originalRow][originalColumn + 2] == -1){
                                numMatches++;
                                tempScore = addBoardScore(6) / 5;
                                
                                for(int i = 0; i < 3; i++) {
                                    boardMatches[originalRow + i][originalColumn] = 6;
                                    boardScore[originalRow + i][originalColumn] = tempScore;
                                    
                                    //if the space is a jelly, add the jelly score there.
                                    if(boardJelly.getBoardJelly(originalRow + i, originalColumn))
                                        boardScore[originalRow + i][originalColumn] += addBoardScore(20);
                                    
                                    //if a zombie is special, take note of what row it is so we can blow it up later.
                                    if(getZombie(originalRow + i, originalColumn).isSpecialZombie())
                                        this.addSpecialZombieAffected(getZombie(originalRow + i, originalColumn));
                                }
                                for(int i = 1; i < 3; i++) {
                                    boardMatches[originalRow][originalColumn + i] = 6;
                                    boardScore[originalRow][originalColumn + i] = tempScore;
                                    
                                    //if the space is a jelly, add the jelly score there.
                                    if(boardJelly.getBoardJelly(originalRow, originalColumn + i))
                                        boardScore[originalRow][originalColumn + i] += addBoardScore(20);
                                    
                                    //if a zombie is special, take note of what row it is so we can blow it up later.
                                    if(getZombie(originalRow, originalColumn + i).isSpecialZombie())
                                        this.addSpecialZombieAffected(getZombie(originalRow, originalColumn + i));
                                }
                                
                                hasMatches = true;
                                
                                int color;
                                color = convertZombieColorToInt(originalZombie.getZombieColor());
                                
                                boardSpecialZombies[originalRow][originalColumn] = 60 + color;//the intersection one is special.
                            }
        }
        
    }
    
    
    
    
    /*
     * modifies the boardMatches and sets matches.
     * fills in 6s whereever there are spaces with T-in-a-row
     */
    public void checkMatchTRow(ZombieCrushSagaZombie originalZombie) {
        
        if(originalZombie == null)
            return;
        
        int originalRow = originalZombie.getGridRow();
        int originalColumn = originalZombie.getGridColumn();
        int tempScore;
        
        //check for
        //1. same colors as the original
        //2. empty boardMatches space for no-repeat scoring purposes.
        
        if(originalRow - 2 >=0 && originalColumn - 1 >= 0 && originalColumn + 1 < BOARD_COLUMNS){ //check for valid range
            
            for(int i = 0; i < 3; i++){
                if(!boardSpace.getBoardSpace(originalRow - i, originalColumn)) {
				}
            }
            for(int i = -1; i < 2; i++){
                if(!boardSpace.getBoardSpace(originalRow, originalColumn + i)) {
				}
            }
        }
        if(originalRow - 2 >=0 && originalColumn - 1 >= 0 && originalColumn + 1 < BOARD_COLUMNS){ //check for valid range
            if(boardMatches[originalRow][originalColumn] == -1)
                if(originalZombie.compareZombie(getZombie(originalRow - 1, originalColumn)) && boardMatches[originalRow - 1][originalColumn] == -1)
                    if(originalZombie.compareZombie(getZombie(originalRow - 2, originalColumn)) && boardMatches[originalRow - 2][originalColumn] == -1)
                        if(originalZombie.compareZombie(getZombie(originalRow, originalColumn + 1)) && boardMatches[originalRow][originalColumn + 1] == -1)
                            if(originalZombie.compareZombie(getZombie(originalRow, originalColumn - 1)) && boardMatches[originalRow][originalColumn - 1] == -1){
                                numMatches++;
                                tempScore = addBoardScore(7) / 5;
                                
                                for(int i = 1; i < 3; i++) {
                                    boardMatches[originalRow - i][originalColumn] = 7;
                                    boardScore[originalRow - i][originalColumn] = tempScore;
                                    
                                    //if the space is a jelly, add the jelly score there.
                                    if(boardJelly.getBoardJelly(originalRow - 1, originalColumn))
                                        boardScore[originalRow - 1][originalColumn] += addBoardScore(20);
                                    
                                    //if a zombie is special, take note of what row it is so we can blow it up later.
                                    if(getZombie(originalRow - i, originalColumn).isSpecialZombie())
                                        this.addSpecialZombieAffected(getZombie(originalRow - i, originalColumn));
                                }
                                for(int i = -1; i < 2; i++) {
                                    boardMatches[originalRow][originalColumn + i] = 7;
                                    boardScore[originalRow][originalColumn + i] = tempScore;
                                    
                                    //if the space is a jelly, add the jelly score there.
                                    if(boardJelly.getBoardJelly(originalRow, originalColumn + i))
                                        boardScore[originalRow][originalColumn + i] += addBoardScore(20);
                                    
                                    //if a zombie is special, take note of what row it is so we can blow it up later.
                                    if(getZombie(originalRow, originalColumn + i).isSpecialZombie())
                                        this.addSpecialZombieAffected(getZombie(originalRow, originalColumn + i));
                                }
                                
                                hasMatches = true;
                                
                                int color;
                                color = convertZombieColorToInt(originalZombie.getZombieColor());
                                
                                boardSpecialZombies[originalRow][originalColumn] = 60 + color;//the intersection one is special.
                            }
        }
        
        if(originalRow + 2 < BOARD_ROWS && originalColumn - 1 >= 0 && originalColumn + 1 < BOARD_COLUMNS){ //check for valid range
            
            for(int i = 0; i < 3; i++){
                if(!boardSpace.getBoardSpace(originalRow + i, originalColumn)) {
				}
            }
            for(int i = -1; i < 2; i++){
                if(!boardSpace.getBoardSpace(originalRow, originalColumn + i)) {
				}
            }
        }
        if(originalRow + 2 < BOARD_ROWS && originalColumn - 1 >= 0 && originalColumn + 1 < BOARD_COLUMNS){ //check for valid range
            if(boardMatches[originalRow][originalColumn] == -1)
                if(originalZombie.compareZombie(getZombie(originalRow + 1, originalColumn)) && boardMatches[originalRow + 1][originalColumn] == -1)
                    if(originalZombie.compareZombie(getZombie(originalRow + 2, originalColumn)) && boardMatches[originalRow + 2][originalColumn] == -1)
                        if(originalZombie.compareZombie(getZombie(originalRow, originalColumn + 1)) && boardMatches[originalRow][originalColumn + 1] == -1)
                            if(originalZombie.compareZombie(getZombie(originalRow, originalColumn - 1)) && boardMatches[originalRow][originalColumn - 1] == -1){
                                numMatches++;
                                tempScore = addBoardScore(7) / 5;
                                
                                for(int i = 1; i < 3; i++) {
                                    boardMatches[originalRow + i][originalColumn] = 7;
                                    boardScore[originalRow + i][originalColumn] = tempScore;
                                    
                                    //if the space is a jelly, add the jelly score there.
                                    if(boardJelly.getBoardJelly(originalRow + i, originalColumn))
                                        boardScore[originalRow + i][originalColumn] += addBoardScore(20);
                                    
                                    //if a zombie is special, take note of what row it is so we can blow it up later.
                                    if(getZombie(originalRow + i, originalColumn).isSpecialZombie())
                                        this.addSpecialZombieAffected(getZombie(originalRow + i, originalColumn));
                                }
                                for(int i = -1; i < 2; i++) {
                                    boardMatches[originalRow][originalColumn + i] = 7;
                                    boardScore[originalRow][originalColumn + i] = tempScore;
                                    
                                    //if the space is a jelly, add the jelly score there.
                                    if(boardJelly.getBoardJelly(originalRow, originalColumn + i))
                                        boardScore[originalRow][originalColumn + i] += addBoardScore(20);
                                    
                                    //if a zombie is special, take note of what row it is so we can blow it up later.
                                    if(getZombie(originalRow, originalColumn + i).isSpecialZombie())
                                        this.addSpecialZombieAffected(getZombie(originalRow, originalColumn + i));
                                }
                                
                                hasMatches = true;
                                
                                int color;
                                color = convertZombieColorToInt(originalZombie.getZombieColor());
                                
                                boardSpecialZombies[originalRow][originalColumn] = 60 + color;//the intersection one is special.
                            }
        }
        
        if(originalRow - 1 >= 0 && originalRow + 1 < BOARD_ROWS && originalColumn - 2 >= 0){ //check for valid range
            
            for(int i = -1; i < 2; i++){
                if(!boardSpace.getBoardSpace(originalRow + i, originalColumn)) {
				}
            }
            for(int i = 0; i < 3; i++){
                if(!boardSpace.getBoardSpace(originalRow, originalColumn - i)) {
				}
            }
        }
        if(originalRow - 1 >= 0 && originalRow + 1 < BOARD_ROWS && originalColumn - 2 >= 0){ //check for valid range
            if(boardMatches[originalRow][originalColumn] == -1)
                if(originalZombie.compareZombie(getZombie(originalRow + 1, originalColumn)) && boardMatches[originalRow + 1][originalColumn] == -1)
                    if(originalZombie.compareZombie(getZombie(originalRow - 1, originalColumn)) && boardMatches[originalRow - 1][originalColumn] == -1)
                        if(originalZombie.compareZombie(getZombie(originalRow, originalColumn - 1)) && boardMatches[originalRow][originalColumn - 1] == -1)
                            if(originalZombie.compareZombie(getZombie(originalRow, originalColumn - 2)) && boardMatches[originalRow][originalColumn - 2] == -1){
                                numMatches++;
                                tempScore = addBoardScore(7) / 5;
                                
                                for(int i = -1; i < 2; i++) {
                                    boardMatches[originalRow + i][originalColumn] = 7;
                                    boardScore[originalRow + i][originalColumn] = tempScore;
                                    
                                    //if the space is a jelly, add the jelly score there.
                                    if(boardJelly.getBoardJelly(originalRow + i, originalColumn))
                                        boardScore[originalRow + i][originalColumn] += addBoardScore(20);
                                    
                                    //if a zombie is special, take note of what row it is so we can blow it up later.
                                    if(getZombie(originalRow + i, originalColumn).isSpecialZombie())
                                        this.addSpecialZombieAffected(getZombie(originalRow + i, originalColumn));
                                }
                                for(int i = 1; i < 3; i++) {
                                    boardMatches[originalRow][originalColumn - i] = 7;
                                    boardScore[originalRow][originalColumn - i] = tempScore;
                                    
                                    //if the space is a jelly, add the jelly score there.
                                    if(boardJelly.getBoardJelly(originalRow, originalColumn - i))
                                        boardScore[originalRow][originalColumn - i] += addBoardScore(20);
                                    
                                    //if a zombie is special, take note of what row it is so we can blow it up later.
                                    if(getZombie(originalRow, originalColumn - i).isSpecialZombie())
                                        this.addSpecialZombieAffected(getZombie(originalRow, originalColumn - i));
                                }
                                
                                hasMatches = true;
                                
                                int color;
                                color = convertZombieColorToInt(originalZombie.getZombieColor());
                                
                                boardSpecialZombies[originalRow][originalColumn] = 60 + color;//the intersection one is special.
                            }
        }
        
        if(originalRow - 1 >= 0 && originalRow + 1 < BOARD_ROWS && originalColumn + 2 < BOARD_COLUMNS){ //check for valid range
            
            for(int i = -1; i < 2; i++){
                if(!boardSpace.getBoardSpace(originalRow + i, originalColumn)) {
				}
            }
            for(int i = 0; i < 3; i++){
                if(!boardSpace.getBoardSpace(originalRow, originalColumn + i)) {
				}
            }
        }
        if(originalRow - 1 >= 0 && originalRow + 1 < BOARD_ROWS && originalColumn + 2 < BOARD_COLUMNS){ //check for valid range
            if(boardMatches[originalRow][originalColumn] == -1)
                if(originalZombie.compareZombie(getZombie(originalRow + 1, originalColumn)) && boardMatches[originalRow + 1][originalColumn] == -1)
                    if(originalZombie.compareZombie(getZombie(originalRow - 1, originalColumn)) && boardMatches[originalRow - 1][originalColumn] == -1)
                        if(originalZombie.compareZombie(getZombie(originalRow, originalColumn + 1)) && boardMatches[originalRow][originalColumn + 1] == -1)
                            if(originalZombie.compareZombie(getZombie(originalRow, originalColumn + 2)) && boardMatches[originalRow][originalColumn + 2] == -1){
                                numMatches++;
                                tempScore = addBoardScore(7) / 5;
                                
                                for(int i = -1; i < 2; i++) {
                                    boardMatches[originalRow + i][originalColumn] = 7;
                                    boardScore[originalRow + i][originalColumn] = tempScore;
                                    
                                    //if the space is a jelly, add the jelly score there.
                                    if(boardJelly.getBoardJelly(originalRow + i, originalColumn))
                                        boardScore[originalRow + i][originalColumn] += addBoardScore(20);
                                    
                                    //if a zombie is special, take note of what row it is so we can blow it up later.
                                    if(getZombie(originalRow + 1, originalColumn).isSpecialZombie())
                                        this.addSpecialZombieAffected(getZombie(originalRow + 1, originalColumn));
                                }
                                for(int i = 1; i < 3; i++) {
                                    boardMatches[originalRow][originalColumn + i] = 7;
                                    boardScore[originalRow][originalColumn + i] = tempScore;
                                    
                                    //if the space is a jelly, add the jelly score there.
                                    if(boardJelly.getBoardJelly(originalRow, originalColumn + i))
                                        boardScore[originalRow][originalColumn + i] += addBoardScore(20);
                                    
                                    //if a zombie is special, take note of what row it is so we can blow it up later.
                                    if(getZombie(originalRow, originalColumn + i).isSpecialZombie())
                                        this.addSpecialZombieAffected(getZombie(originalRow, originalColumn + i));
                                }
                                
                                hasMatches = true;
                                
                                int color;
                                color = convertZombieColorToInt(originalZombie.getZombieColor());
                                
                                boardSpecialZombies[originalRow][originalColumn] = 60 + color;//the intersection one is special.
                            }
        }
        
    }
    
    /*
     * returns true in case the first instance of a 3-in-a-row is found
     * otherwise false
     */
    public boolean checkMatchThreeRowQuickly(ZombieCrushSagaZombie originalZombie) {
        
        if (originalZombie == null)
            return false;
        
        int originalRow = originalZombie.getGridRow();
        int originalColumn = originalZombie.getGridColumn();
        
        //check for
        //1. same colors as the original
        //2. empty boardMatches space for no-repeat scoring purposes.
        
        //check below
        //if spaces are active, then we check.
        if(originalRow + 2 < BOARD_ROWS) { //check for valid range
            
            for(int i = 0; i < 2; i++){
                if(!boardSpace.getBoardSpace(originalRow + i, originalColumn)) {
				}
            }
        }
        if(originalRow + 2 < BOARD_ROWS) { //check for valid range
            if(boardMatches[originalRow][originalColumn] == -1)
                if(originalZombie.compareZombie(getZombie(originalRow + 1, originalColumn)) && boardMatches[originalRow + 1][originalColumn] == -1)
                    if(originalZombie.compareZombie(getZombie(originalRow + 2, originalColumn)) && boardMatches[originalRow + 2][originalColumn] == -1){
                        hasMatches = true;
                        return true;
                    }
        }
        
        if(originalColumn + 2 < BOARD_COLUMNS) { //check for valid range
            
            for(int i = 0; i < 2; i++){
                if(!boardSpace.getBoardSpace(originalRow, originalColumn + i)) {
				}
            }
        }
        if(originalColumn + 2 < BOARD_COLUMNS) { //check for valid range
            if(boardMatches[originalRow][originalColumn] == -1)
                if(originalZombie.compareZombie(getZombie(originalRow, originalColumn + 1)) && boardMatches[originalRow][originalColumn + 1] == -1)
                    if(originalZombie.compareZombie(getZombie(originalRow, originalColumn + 2)) && boardMatches[originalRow][originalColumn + 2] == -1){
                        hasMatches = true;
                        return true;
                    }
        }
        
        return false;
    }
    
    
    /**
     * returns the boardMatches
     *
     * @return the boardMatches
     */
    public String toStringBoardMatches(){
        
        String temp = "";
        for(int row = 0; row < BOARD_ROWS; row++){
            for(int column = 0; column < BOARD_COLUMNS; column++){
                temp = temp + boardMatches[row][column] + " ";
            }
            temp = temp + "\n";
        }
        return temp;
    }
    
    /**
     * returns the boardSpecialZombies
     *
     * @return the boardSpecialZombies
     */
    public String toStringBoardSpecialZombies(){
        
        String temp = "";
        for(int row = 0; row < BOARD_ROWS; row++){
            for(int column = 0; column < BOARD_COLUMNS; column++){
                temp = temp + boardSpecialZombies[row][column] + " ";
            }
            temp = temp + "\n";
        }
        return temp;
    }
    
    /**
     * returns the boardScore
     *
     * @return the boardScore
     */
    public String toStringBoardScore(){
        
        String temp = "";
        for(int row = 0; row < BOARD_ROWS; row++){
            for(int column = 0; column < BOARD_COLUMNS; column++){
                temp = temp + boardScore[row][column] + " ";
            }
            temp = temp + "\n";
        }
        return temp;
    }
    
    
    /**
     * returns the boardZombie of colors. 1-6 each represent a color.
     *
     * @return the boardZombie of colors
     */
    public String toString(){
        
        ZombieCrushSagaZombie tempZombie;
        String temp = "";
        String color = "";
        for(int row = 0; row < BOARD_ROWS; row++){
            for(int column = 0; column < BOARD_COLUMNS; column++){
                tempZombie = getZombie(row,column);
                
                if(tempZombie != null){
                    
                    if(tempZombie.getZombieState().equals(ZOMBIE_BLUE_STATE))
                        color = "1";
                    else if(tempZombie.getZombieState().equals(ZOMBIE_GREEN_STATE))
                        color = "2";
                    else if(tempZombie.getZombieState().equals(ZOMBIE_ORANGE_STATE))
                        color = "3";
                    else if(tempZombie.getZombieState().equals(ZOMBIE_PURPLE_STATE))
                        color = "4";
                    else if(tempZombie.getZombieState().equals(ZOMBIE_RED_STATE))
                        color = "5";
                    else if(tempZombie.getZombieState().equals(ZOMBIE_YELLOW_STATE))
                        color = "6";
                    else if(tempZombie.getZombieState().equals(ZOMBIE_FIVE_STATE))
                        color = "F";
                    
                }
                else
                    color = "n";
                
                temp = temp + color + " ";
            }
            
            temp = temp + "\n";
        }
        return temp;
        
    }
    
}
