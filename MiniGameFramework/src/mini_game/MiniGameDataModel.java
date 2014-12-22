package mini_game;

import java.util.TreeMap;
import java.util.Vector;

/**
 * This class provides the base class for a game data management class. Note
 * that general game data is already provided and managed here. A sub-class
 * would provide the game-specific data.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public abstract class MiniGameDataModel
{
    // STORES THE DIFFERENT TYPES OF SPRITES
    // USED BY THE GAME
    protected TreeMap<String, SpriteType> spriteTypes;

    // GAME WORLD DIMENSIONS
    protected int gameWidth;
    protected int gameHeight;

    // KEEPS TRACK OF WHETHER THE GAME IS IN PROGRESS OR NOT
    protected MiniGameState gameState;
    
    // THESE VARIABLES HELP US DISPLAY TEXT ON THE
    // SCREEN FOR THE PURPOSE OF DEBUGGING
    protected Vector<String> debugText;
    protected boolean debugTextRenderingActive;
    protected int debugTextX;
    protected int debugTextY;
    
    // LAST MOUSE POSITION
    protected int lastMouseX;
    protected int lastMouseY;
    
    // THIS ALLOWS US TO PAUSE THE GAME IF WE LIKE
    protected boolean paused;

    /**
     * Default constructor, it should be invoked by any child class.
     */
    public MiniGameDataModel()
    {
        // INIT THE DATA STRUCTURE FOR STORING
        // THE TYPES OF SPRITES IN THE GAME
        spriteTypes = new TreeMap<String, SpriteType>();

        // AND INIT ALL THE DEBUG DISPLAY STUFF
        debugText = new Vector<String>();
        debugTextRenderingActive = false;
        debugTextX = 10;
        debugTextY = 200;

        // WE START UNPAUSED
        paused = false;
    }

    // ACCESSOR METHODS
        // getDebugText
        // getDebugTextX
        // getDebugTextY
        // getGameHeight
        // getGameWidth
        // getGameState
        // getLastMouseX
        // getLastMouseY
        // getSpriteType
        // isDebugTextRenderingActive
        // isPaused
    /**
     * For accessing the Vector containing the debug text being stored for
     * optional display.
     *
     * return the Vector that stores all the debug text.
     */
    public Vector<String> getDebugText()
    {
        return debugText;
    }

    /**
     * For accessing the x coordinate of the debug text.
     *
     * return the x coordinate of where we will start rendering the debug text.
     */
    public int getDebugTextX()
    {
        return debugTextX;
    }

    /**
     * For accessing the y coordinate of the debug text.
     *
     * @return the y coordinate of where we will start rendering the debug text.
     */
    public int getDebugTextY()
    {
        return debugTextY;
    }

    /**
     * For accessing the height of the game's canvas, and consequently the game
     * world.
     *
     * @return the height of the game world playing surface.
     */
    public int getGameHeight()
    {
        return gameHeight;
    }

    /**
     * For accessing the width of the game's canvas, and consequently the game
     * world.
     *
     * @return the width of the game world playing surface.
     */
    public int getGameWidth()
    {
        return gameWidth;
    }

    /**
     * For accessing the current game state.
     *
     * @return the current state of the game, which may only be one of
     * NOT_STARTED, IN_PROGRESS, LOSS, or WIN
     */
    public MiniGameState getGameState()
    {
        return gameState;
    }

    /**
     * Accessor for getting the last known pixel's x location of the mouse on
     * the canvas.
     *
     * @return The last known x location of the mouse.
     */
    public int getLastMouseX()
    {
        return lastMouseX;
    }

    /**
     * Accessor for getting the last known pixel's y location of the mouse on
     * the canvas.
     *
     * @return The last known y location of the mouse.
     */
    public int getLastMouseY()
    {
        return lastMouseY;
    }

    /**
     * For accessing the SpriteType object that corresponds to the id parameter.
     * We do this because a game may have many Sprites that share a SpriteType,
     * so this provides an easy access point for all of them for whoever needs
     * it.
     *
     * @param id the textual description of the sprite type to be accessed
     *
     * @return the SpriteType object corresponding to the id
     */
    public SpriteType getSpriteType(String id)
    {
        return spriteTypes.get(id);
    }

    /**
     * For asking if the debug text is currently being rendered.
     *
     * @return true if the debug text is currently active, and thus renderable,
     * false otherwise.
     */
    public boolean isDebugTextRenderingActive()
    {
        return debugTextRenderingActive;
    }

    /**
     * For asking if the game is currently paused.
     *
     * @return true if the game is paused, false otherwise
     */
    public boolean isPaused()
    {
        return paused;
    }

    // GAME STATE TEST METHODS
    // inProgress
    // lost
    // won
    /**
     * Asks if the game is currently in progress or not.
     *
     * @return true if the game is currently in progress, meaning gameplay is
     * active, false otherwise.
     */
    public boolean inProgress()
    {
        return gameState == MiniGameState.IN_PROGRESS;
    }

    /**
     * Asks if the game is over and the player lost or not.
     *
     * @return true if the game is over and the player lost
     */
    public boolean lost()
    {
        return gameState == MiniGameState.LOSS;
    }

    /**
     * Asks if the game is over and the player won or not.
     *
     * @return true if the game is over and the player won
     */
    public boolean won()
    {
        return gameState == MiniGameState.WIN;
    }

    // MUTATOR METHODS
        // activateDebugTextRendering
        // addSpriteType
        // beginGame
        // deactivateDebuTextRendering
        // endGameAsLoss
        // endGameAsWin
        // incDebugText
        // pause
        // setGameDimensions
        // setGameState
        // setLastMouseX
        // setLastMouseY
        // unpause
    /**
     * Activates the debug text, allowing it to be rendered.
     */
    public void activateDebugTextRendering()
    {
        debugTextRenderingActive = true;
    }

    /**
     * Adds the spriteTypeToAdd parameter to the data model using the sprite
     * type's id value as the binary search tree key.
     *
     * @param spriteTypeToAdd the constructed sprite to be stored.
     */
    public void addSpriteType(SpriteType spriteTypeToAdd)
    {
        spriteTypes.put(spriteTypeToAdd.getSpriteTypeID(), spriteTypeToAdd);
    }

    /**
     * Mutator method for setting the game state to GameState.IN_PROGRESS
     */
    public void beginGame()
    {
        gameState = MiniGameState.IN_PROGRESS;
    }

    /**
     * Deactivates the debug text, preventing it from being rendered.
     */
    public void deactivateDebugTextRendering()
    {
        debugTextRenderingActive = false;
    }

    /**
     * Mutator method for setting the game state to GameState.LOSS
     */
    public void endGameAsLoss()
    {
        gameState = MiniGameState.LOSS;
    }

    /**
     * Mutator method for setting the game state to GameState.WIN
     */
    public void endGameAsWin()
    {
        gameState = MiniGameState.WIN;
    }

    /**
     * Moves the position of the debug text by the provided increment.
     *
     * @param incX the amount by which to move the debug text on screen in the x
     * axis.
     *
     * @param incY the amount by which to move the debug text on screen in the y
     * axis.
     */
    public void incDebugText(int incX, int incY)
    {
        debugTextX += incX;
        debugTextY += incY;
    }

    /**
     * Pauses the game, meaning all game logic gets skipped.
     */
    public void pause()
    {
        paused = true;
    }

    /**
     * Mutator method for setting the dimensions of the playing surface.
     *
     * @param initGameWidth width in pixels of the playing surface
     *
     * @param initGameHeight height in pixels of the playing surface
     */
    public void setGameDimensions(int initGameWidth, int initGameHeight)
    {
        gameWidth = initGameWidth;
        gameHeight = initGameHeight;
    }

    /**
     * Mutator method for setting the game state.
     *
     * @param initGameState the game state to use.
     */
    public void setGameState(MiniGameState initGameState)
    {
        gameState = initGameState;
    }

    /**
     * Mutator method to set the last known pixel's x location of the mouse.
     *
     * @param initX X location of mouse.
     */
    public void setLastMouseX(int initX)
    {
        lastMouseX = initX;
    }

    /**
     * Mutator method to set the last known pixel's y location of the mouse.
     *
     * @param initY Y location of mouse.
     */
    public void setLastMouseY(int initY)
    {
        lastMouseY = initY;
    }

    /**
     * Unpauses the game, allowing game logic to be executed each frame.
     */
    public void unpause()
    {
        paused = false;
    }

    // ABSTRACT METHODS - GAME-SPECIFIC IMPLEMENTATIONS REQUIRED
        // checkMousePressOnSprites
        // reset
        // updateAll
        // updateDebugText
    /**
     * Provided on a game-to-game basis, this allows for a custom response for
     * when the player clicks on a Sprite. This method is called once per frame
     * and should test and respond for all needed Sprites in the game. Note that
     * the x,y coordinates provided refer to the canvas coordinates of the mouse
     * click.
     *
     * @param game the game currently being played, this has access to all the
     * data in the system either directly or indirectly.
     *
     * @param x the x-axis location of the mouse press
     *
     * @param y the y-axis location of the mouse press
     */
    public abstract void checkMousePressOnSprites(MiniGame game, int x, int y);
    
    
    
    
    public abstract void checkMouseDragOnSprites(MiniGame game, int x, int y, boolean drag);

    /**
     * For resetting all game data to the start of a new game.
     *
     * @param game the game in progress
     */
    public abstract void reset(MiniGame game);

    /**
     * Called each frame, this method is for updating all the game data that is
     * particular to the custom game application.
     *
     * @param game the game in progress that is to be udated.
     */
    public abstract void updateAll(MiniGame game);

    /**
     * Called each frame, this method is for updating the debug text that may be
     * currently displayed.
     *
     * @param game the game in progress from this method will likely derive the
     * textual description for debugging purposes.
     */
    public abstract void updateDebugText(MiniGame game);
}