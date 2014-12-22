package zombie_crush_saga.ui;

import java.util.ArrayList;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import static zombie_crush_saga.ZombieCrushSagaConstants.*;

/**
 * This class represents a single ZOMBIE in the game world.
 * 
 * @author Richard McKenna
 */
public class ZombieCrushSagaZombie extends Sprite
{
    
    private String zombieColor;     // There are 6 basic colors and then the color Bomb color.
    private String zombieState;     //get zombie state; there are: base colors, stripped, wrapped, and bomb.

    //stores if the user selected this.
    private boolean selected;
    
    // WHEN WE PUT A TILE IN THE GRID WE TELL IT WHAT COLUMN AND ROW
    // IT IS LOCATED TO MAKE THE UNDO OPERATION EASY LATER ON
    private int gridColumn;
    private int gridRow;
    
    // THIS IS true WHEN THIS TILE IS MOVING, WHICH HELPS US FIGURE
    // OUT WHEN IT HAS REACHED A DESTINATION NODE
    private boolean movingToTarget;
    
    // THE TARGET COORDINATES IN WHICH IT IS CURRENTLY HEADING
    private float targetX;
    private float targetY;
    
    // WIN ANIMATIONS CAN BE GENERATED SIMPLY BY PUTTING TILES ON A PATH    
    private ArrayList<Integer> winPath;
    
    /**
     * This constructor initializes this tile for use, including all the
     * sprite-related data from its ancestor class, Sprite.
     */
    public ZombieCrushSagaZombie(    SpriteType initSpriteType,
                                    float initX, 	float initY,
                                    float initVx, 	float initVy,
                                    String initState,   String initZombieType)
    {
        // SEND ALL THE Sprite DATA TO A Sprite CONSTRUCTOR
        super(initSpriteType, initX, initY, initVx, initVy, initState);
        
        // INIT THE ZOMBIE TYPE
        zombieState = initZombieType;
        zombieColor = zombieState;      //do not initialize as special candy.
        selected = false;
    }
    
    // ACCESSOR METHODS
        // -getZombieColorType
        // -getGridColumn
        // -getGridRow
        // -getTargetX
        // -getTargetY
        // -isMovingToTarget
    
    /**
     * checks the direction of the type candy, if it has any.
     * 
     * @return String vertical, horizontal, or none.
     */
    public String getDirection()
    {
        if(zombieState.contains("VERTICAL"))
            return "VERTICAL";
        else if(zombieState.contains("HORIZONTAL"))
            return "HORIZONTAL";
        
        return "NONE";
            
    }
    
    /**
     * Accessor method for getting this zombie color type.
     *
     * @return The zombie color type for this zombie.
     */
    public String getZombieState()
    { 
        return zombieState;  
    }
    
    /**
     * Accessor method for getting the tile grid column that this tile
     * is either currently in, or was most recently in.
     * 
     * @return The grid column this tile is or most recently was located in.
     */
    public int getGridColumn()
    {
        return gridColumn;
    }
    
    /**
     * 
     * 
     * @return String
     */
    public String getZombieColor() {
        return zombieColor;
    }
    
    /**
     * Accessor method for getting the tile grid row that this tile
     * is either currently in, or was most recently in.
     * 
     * @return The grid row this tile is or most recently was located in.
     */
    public int getGridRow() 
    { 
        return gridRow; 
    }
    
    /**
     * Accessor method for getting the x-axis target coordinate for this tile.
     * 
     * @return The x-axis target coordinate for this tile.
     */
    public float getTargetX() 
    { 
        return targetX; 
    }
    
    /**
     * Accessor method for getting the y-axis target coordinate for this tile.
     * 
     * @return The y-axis target coordinate for this teil.
     */
    public float getTargetY() 
    { 
        return targetY; 
    }
    
    /**
     * Accessor method for getting whether this tile is currently moving toward
     * target coordinates or not.
     * 
     * @return true if this tile is currently moving toward target coordinates,
     * false otherwise.
     */
    public boolean isMovingToTarget() 
    { 
        return movingToTarget; 
    }
    
    public boolean isSelected() {
        
        return selected;
    }
            
    // MUTATOR METHODS
        // -setGridCell
        // -setTarget
    /**
     * toggles selected.
     */
    public void toggleSelected(){
        if (selected) //true
            selected = false;
        else
            selected = true;
    }
    
    /**
     * Mutator method for setting both the grid column and row that
     * this tile is being placed in.
     * 
     * @param initGridColumn The column this tile is being placed in
     * in the ZombieCrushSaga game grid.
     * 
     * @param initGridRow The row this tile is being placed in
     * in the ZombieCrushSaga game grid.
     */
    public void setGridCell(int initGridRow, int initGridColumn)
    {
        gridColumn = initGridColumn;
        gridRow = initGridRow;
    }
    
    /*
     * no enums to protect you. lol
     * 
     */
    public void setZombieColor(String type){
        this.setState(type);
        zombieState = type;
        zombieColor = type;
    }
    
    /**
     * Mutator method for setting bot the x-axis and y-axis target
     * coordinates for this tile.
     * 
     * @param initTargetX The x-axis target coordinate to move this
     * tile towards.
     * 
     * @param initTargetY The y-axis target coordinate to move this
     * tile towards.
     */
    public void setTarget(float initTargetX, float initTargetY) 
    {
        targetX = initTargetX; 
        targetY = initTargetY;
    }  

    // METHOD FOR MATHING
    // -match
    
     public boolean isInvisible(){
         return state.equals(INVISIBLE_STATE);
     }
    
    /**
     * returns true if the zombie is nor a normal basic type zombie.
     * if the current zombie state is not normal, then it must be special
     * 
     * @return boolean the result of whether a zombie is special. returns true if special.
     */
    public boolean isSpecialZombie(){
        
        if(zombieState.equals(ZOMBIE_BLUE_STATE))
            return false;
        if(zombieState.equals(ZOMBIE_GREEN_STATE))
            return false;
        if(zombieState.equals(ZOMBIE_ORANGE_STATE))
            return false;
        if(zombieState.equals(ZOMBIE_PURPLE_STATE))
            return false;
        if(zombieState.equals(ZOMBIE_RED_STATE))
            return false;
        if(zombieState.equals(ZOMBIE_YELLOW_STATE))
            return false;
        
        return true;
        
    }
    
    /**
     * This method tests to see if this Zombie matches the testZombie argument
     * and returns true if they are neighboring, false otherwise.
     * 
     * @param testZombie The tile to compare this tile to.
     * 
     * @return true if this tile is a neighbor for the testZombie argument,
     * false otherwise.
     */
    public boolean isAdjacentZombie(ZombieCrushSagaZombie testZombie)
    {
        //testZombie is to the left
        if (this.getGridColumn() - 1 == testZombie.getGridColumn())
            if(this.getGridRow() == testZombie.getGridRow())
                return true;
        
        //testZombie is to the right
        if (this.getGridColumn() + 1 == testZombie.getGridColumn())
            if(this.getGridRow() == testZombie.getGridRow())
                return true;
        
        //testZombie is to the up
        if (this.getGridColumn()  == testZombie.getGridColumn())
            if(this.getGridRow() - 1 == testZombie.getGridRow())
                return true;
        
        //testZombie is to the down
        if (this.getGridColumn()  == testZombie.getGridColumn())
            if(this.getGridRow() + 1 == testZombie.getGridRow())
                return true;
        
        return false;
        
    }
    
    // PATHFINDING METHODS
        // -calculateDistanceToTarget
        // -initWinPath
        // -startMovingToTarget
        // -updateWinPath
    
    /**
     * This method calculates the distance from this tile's current location
     * to the target coordinates on a direct line.
     * 
     * @return The total distance on a direct line from where the tile is
     * currently, to where its target is.
     */
    public float calculateDistanceToTarget()
    {
        // GET THE X-AXIS DISTANCE TO GO
        float diffX = targetX - x;
        
        // AND THE Y-AXIS DISTANCE TO GO
        float diffY = targetY - y;
        
        // AND EMPLOY THE PYTHAGOREAN THEOREM TO CALCULATE THE DISTANCE
        float distance = (float)Math.sqrt((diffX * diffX) + (diffY * diffY));
        
        // AND RETURN THE DISTANCE
        return distance;
    }
    
   /**
    * gives the zombie a randomColor color state
    */
    public void randomColor(){
        
        int rand;
        rand = (int)(Math.random() * 6);

        switch(rand){
            case 0:
                this.setState(ZOMBIE_BLUE_STATE);
                zombieState = ZOMBIE_BLUE_STATE;
                zombieColor = zombieState;
                break;
            case 1:
                this.setState(ZOMBIE_GREEN_STATE); 
                zombieState = ZOMBIE_GREEN_STATE;
                zombieColor = zombieState;
                break;
            case 2:
                this.setState(ZOMBIE_ORANGE_STATE); 
                zombieState = ZOMBIE_ORANGE_STATE;
                zombieColor = zombieState;
                break;
            case 3:
                this.setState(ZOMBIE_PURPLE_STATE); 
                zombieState = ZOMBIE_PURPLE_STATE;
                zombieColor = zombieState;
                break;
            case 4:
                this.setState(ZOMBIE_RED_STATE); 
                zombieState = ZOMBIE_RED_STATE;
                zombieColor = zombieState;
                break;
            case 5:
                this.setState(ZOMBIE_YELLOW_STATE); 
                zombieState = ZOMBIE_YELLOW_STATE;
                zombieColor = zombieState;
                break;
            default:
                System.out.println("Error: naughty mr. bix. OH SENATOR!");
                
        }
    
    }
    
   /**
     * gives back a random color that the Five Bomb will destroy.
     * 
     * @return String a random Color that the fiveBomb will blow up
     */
    public String randomFiveBombColor(){
        
        int rand;
        rand = (int)(Math.random() * 6);
        
        switch(rand){
            case 0:
                return ZOMBIE_BLUE_STATE;
            case 1:
                return ZOMBIE_GREEN_STATE;
            case 2:
                return ZOMBIE_ORANGE_STATE;
            case 3:
                return ZOMBIE_PURPLE_STATE;
            case 4:
                return ZOMBIE_RED_STATE;
            case 5:
                return ZOMBIE_YELLOW_STATE;
        }
        
        return "randomFiveBombColo: shit, you shouldn't be here.";
    
    }
    
    /**
     * this is a color bomb.
     * sets base color to FIVE (state)
     * sets state to ZOMBIE_FIVE_STATE
     * 
     */
    public void setFiveColorBomb(){
        
        zombieColor = ZOMBIE_FIVE_STATE;
        zombieState = ZOMBIE_FIVE_STATE;
        this.setState(zombieState);
        
    }
    
      /**
     * gives the zombie a wrapped Color
     * 
     * @param color the color of the wrapped zombie.
     */
    public void setWrappedColor(String color){
        
        zombieColor = color;
        switch (zombieColor) {
            case ZOMBIE_BLUE_STATE:
                zombieState = ZOMBIE_BLUE_WRAPPED_STATE;
                break;
            case ZOMBIE_GREEN_STATE:
                zombieState = ZOMBIE_GREEN_WRAPPED_STATE;
                break;
            case ZOMBIE_ORANGE_STATE:
                zombieState = ZOMBIE_ORANGE_WRAPPED_STATE;
                break;
            case ZOMBIE_PURPLE_STATE:
                zombieState = ZOMBIE_PURPLE_WRAPPED_STATE;
                break;
            case ZOMBIE_RED_STATE:
                zombieState = ZOMBIE_RED_WRAPPED_STATE;
                break;
            case ZOMBIE_YELLOW_STATE:
                zombieState = ZOMBIE_YELLOW_WRAPPED_STATE;
                break;
            default:
                System.out.println("ZombieCrushSagaZombie, setWrappedColor: Too many colors");
                break;
        }
        
        this.setState(zombieState);
    }
    
    
    /**
     * gives the zombie a stripped Color
     * it will randomly be either vertical or horizontal
     * 
     * @param color the color of the stripped zombie.
     */
    public void setStrippedColor(String color, String direction){
        
        //int rand;
        //rand = (int)(Math.random() * 2);
        
        //this may seem unnecessary but it has to be done to ensure correctness.
        zombieColor = color;
        switch (zombieColor) {
            case ZOMBIE_BLUE_STATE:
                if(direction.equals("HORIZONTAL"))
                    zombieState = ZOMBIE_BLUE_STRIPED_HORIZONTAL_STATE;
                else
                    zombieState = ZOMBIE_BLUE_STRIPED_VERTICAL_STATE;
                break;
            case ZOMBIE_GREEN_STATE:
                if(direction.equals("HORIZONTAL"))
                    zombieState = ZOMBIE_GREEN_STRIPED_HORIZONTAL_STATE;
                else
                    zombieState = ZOMBIE_GREEN_STRIPED_VERTICAL_STATE;
                break;
            case ZOMBIE_ORANGE_STATE:
                if(direction.equals("HORIZONTAL"))
                    zombieState = ZOMBIE_ORANGE_STRIPED_HORIZONTAL_STATE;
                else
                    zombieState = ZOMBIE_ORANGE_STRIPED_VERTICAL_STATE;
                break;
            case ZOMBIE_PURPLE_STATE:
                if(direction.equals("HORIZONTAL"))
                    zombieState = ZOMBIE_PURPLE_STRIPED_HORIZONTAL_STATE;
                else
                    zombieState = ZOMBIE_PURPLE_STRIPED_VERTICAL_STATE;
                break;
            case ZOMBIE_RED_STATE:
                if(direction.equals("HORIZONTAL"))
                    zombieState = ZOMBIE_RED_STRIPED_HORIZONTAL_STATE;
                else
                    zombieState = ZOMBIE_RED_STRIPED_VERTICAL_STATE;
                break;
            case ZOMBIE_YELLOW_STATE:
                if(direction.equals("HORIZONTAL"))
                    zombieState = ZOMBIE_YELLOW_STRIPED_HORIZONTAL_STATE;
                else
                    zombieState = ZOMBIE_YELLOW_STRIPED_VERTICAL_STATE;
                break;
            default:
                System.out.println("ZombieCrushSagaZombie, setStrippedColor: Too many colors");
                break;
        }
        
        this.setState(zombieState);
        
    }
    
    public void deselect(){
        if(this.isSelected()) //if selected, unselect
            this.toggleSelected();
        this.setState(zombieState);
    }
    
    
    /**
     * compares this zombie's base color with another zombie's base color
     * 
     * ex: striped blue and blue are a match
     * 
     * @return boolean the result of the base color comparison
     */
    public boolean compareZombie(ZombieCrushSagaZombie otherZombie){
        
        //can't compare what's not there. nulls cannot call anything.
        if(otherZombie == null)
            return false;
        
        //can't compare invisible zombies.
        if(this.getState().equals(INVISIBLE_STATE) || otherZombie.getState().equals(INVISIBLE_STATE))
            return false;
            
        //zombie five states can't match with anything
        if(zombieColor.equals(ZOMBIE_FIVE_STATE))
            return false;
        
        return otherZombie.getZombieColor().equals(zombieColor);
        
    }
    
    
    /**
     * This method builds a path for this tile for the 
     * win animations by slightly randomizing the locations
     * of the nodes in the winPathNodes argument.
     */
    public void initWinPath(ArrayList<Integer> winPathNodes)
    {
        // CONSTRUCT THE PATH
        winPath = new ArrayList<Integer>(winPathNodes.size());
        for (int i = 0; i < winPathNodes.size(); i+=2)
        {
            // AND FILL IT WITH FUZZY PATH NODES
            int toleranceX = (int)(WIN_PATH_TOLERANCE * Math.random()) - (WIN_PATH_TOLERANCE/2);
            int toleranceY = (int)(WIN_PATH_TOLERANCE * Math.random()) - (WIN_PATH_TOLERANCE/2);
            int x = winPathNodes.get(i) + toleranceX;
            int y = winPathNodes.get(i+1) + toleranceY;
            winPath.add(x);
            winPath.add(y);
        }
    }    
    
    /**
     * Allows the tile to start moving by initializing its properly
     * scaled velocity vector pointed towards it target coordinates.
     * 
     * @param maxVelocity The maximum velocity of this tile, which
     * we'll then compute the x and y axis components for taking into
     * account the trajectory angle.
     */
    public void startMovingToTarget(int maxVelocity)
    {
        // LET ITS POSITIONG GET UPDATED
        movingToTarget = true;
        
        // CALCULATE THE ANGLE OF THE TRAJECTORY TO THE TARGET
        float diffX = targetX - x;
        float diffY = targetY - y;
        float tanResult = diffY/diffX;
        float angleInRadians = (float)Math.atan(tanResult);
        
        // COMPUTE THE X VELOCITY COMPONENT
        vX = (float)(maxVelocity * Math.cos(angleInRadians));
        
        // CLAMP THE VELOCTY IN CASE OF NEGATIVE ANGLES
        if ((diffX < 0) && (vX > 0)) vX *= -1;
        if ((diffX > 0) && (vX < 0)) vX *= -1;
        
        // COMPUTE THE Y VELOCITY COMPONENT
        vY = (float)(maxVelocity * Math.sin(angleInRadians));       
        
        // CLAMP THE VELOCITY IN CASE OF NEGATIVE ANGLES
        if ((diffY < 0) && (vY > 0)) vY *= -1;
        if ((diffY > 0) && (vY < 0)) vY *= -1;
    }
    
    /**
     * returns the state and color
     * 
     * @return String state and color
     */
    @Override
    public String toString(){
        return "State: " + zombieState + "\n" + "Color: " + zombieColor;   
    }
    
    // METHODS OVERRIDDEN FROM Sprite
    // -update

    /**
     * Called each frame, this method ensures that this tile is updated
     * according to the path it is on.
     * 
     * @param game The ZombieCrushSaga game this tile is part of.
     */
    @Override
    public void update(MiniGame game)
    {
        // IF NOT, IF THIS TILE IS ALMOST AT ITS TARGET DESTINATION,
        // JUST GO TO THE TARGET AND THEN STOP MOVING
        if (calculateDistanceToTarget() < MAX_ZOMBIE_VELOCITY)
        {
            vX = 0;
            vY = 0;
            x = targetX;
            y = targetY;
            movingToTarget = false;
        }
        // OTHERWISE, JUST DO A NORMAL UPDATE, WHICH WILL CHANGE ITS POSITION
        // USING ITS CURRENT VELOCITY.
        else
        {
            super.update(game);
        }
    }
}