package zombie_crush_saga.ui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import zombie_crush_saga.data.ZombieCrushSagaDataModel;
import mini_game.MiniGame;
import static zombie_crush_saga.ZombieCrushSagaConstants.*;
import mini_game.Sprite;
import mini_game.SpriteType;
import properties_manager.PropertiesManager;
import zombie_crush_saga.ZombieCrushSaga.ZombieCrushSagaPropertyType;
import zombie_crush_saga.file.ZombieCrushSagaFileManager;
import zombie_crush_saga.data.ZombieCrushSagaRecord;
import zombie_crush_saga.data.ZombieCrushSagaBoardJelly;
import zombie_crush_saga.data.ZombieCrushSagaBoardSpace;
import zombie_crush_saga.events.LevelScoreCloseHandler;
import zombie_crush_saga.events.ExitHandler;
import zombie_crush_saga.events.GameplayQuitHandler;
import zombie_crush_saga.events.GameplayReplayHandler;
import zombie_crush_saga.events.GameplaySmasherHandler;
import zombie_crush_saga.events.LevelScorePlayLevelHandler;
import zombie_crush_saga.events.SagaScrollHandler;
import zombie_crush_saga.events.SplashPlayHandler;
import zombie_crush_saga.events.SagaSelectLevelHandler;
import zombie_crush_saga.events.SplashQuitHandler;
import zombie_crush_saga.events.SplashResetHandler;
import zombie_crush_saga.events.ZombieCrushSagaKeyHandler;

/**
 * This is the actual mini game, as extended from the mini game framework. It
 * manages all the UI elements.
 *
 * @author Richard McKenna
 */
public class ZombieCrushSagaMiniGame extends MiniGame
{
    // THE PLAYER RECORD FOR EACH LEVEL, WHICH LIVES BEYOND ONE SESSION
    private ZombieCrushSagaRecord record;
    
    // HANDLES ERROR CONDITIONS
    private ZombieCrushSagaErrorHandler errorHandler;
    
    // MANAGES LOADING OF LEVELS AND THE PLAYER RECORDS FILES
    private ZombieCrushSagaFileManager fileManager;
    
    // THE SCREEN CURRENTLY BEING PLAYED
    private String currentScreenState;
    
    // THE TARGET COORDINATES IN WHICH The Background CURRENTLY HEADING
    private float targetY;
    
    // ACCESSOR METHODS
    // - getPlayerRecord
    // - getErrorHandler
    // - getFileManager
    // - isCurrentScreenState
    
    /**
     * Accessor method for getting the player record object, which
     * summarizes the player's record on all levels.
     *
     * @return The player's complete record.
     */
    public ZombieCrushSagaRecord getPlayerRecord()
    {
        return record;
    }
    
    /**
     * Accessor method for getting the application's error handler.
     *
     * @return The error handler.
     */
    public ZombieCrushSagaErrorHandler getErrorHandler()
    {
        return errorHandler;
    }
    
    /**
     * Accessor method for getting the app's file manager.
     *
     * @return The file manager.
     */
    public ZombieCrushSagaFileManager getFileManager()
    {
        return fileManager;
    }
    
    /**
     * Used for testing to see if the current screen state matches
     * the testScreenState argument. If it mates, true is returned,
     * else false.
     *
     * @param testScreenState Screen state to test against the
     * current state.
     *
     * @return true if the current state is testScreenState, false otherwise.
     */
    public boolean isCurrentScreenState(String testScreenState)
    {
        return testScreenState.equals(currentScreenState);
    }
    
    // SERVICE METHODS
    // - displayStats
    // - savePlayerRecord
    // - switchToGameplayScreen
    // - switchToSplashScreen
    // - updateBoundaries
    
    /**
     * This method displays makes the stats dialog display visible,
     * which includes the text inside.
     */
    public void displayStats()
    {
        // MAKE SURE ONLY THE PROPER DIALOG IS VISIBLE
        guiDialogs.get(WIN_DIALOG_TYPE).setState(INVISIBLE_STATE);
        guiDialogs.get(LOSS_DIALOG_TYPE).setState(INVISIBLE_STATE);
        guiDialogs.get(STATS_DIALOG_TYPE).setState(VISIBLE_STATE);
    }
    
    /**
     * This method forces the file manager to save the current player record.
     */
    public void savePlayerRecord()
    {
        // THIS CURRENTLY DOES NOTHING, INSTEAD, IT MUST SAVE ALL THE
        // PLAYER RECORDS IN THE SAME FORMAT IT IS BEING LOADED
        
        fileManager.saveRecord();
    }
    
    /**
     * This method switches the application to the gameplay screen, making
     * all the appropriate UI controls visible & invisible.
     */
    public void switchToGameplayScreen()
    {
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(GAMEPLAY_SCREEN_STATE);
        
        // ACTIVATE THE TOOLBAR AND ITS CONTROLS
        // DEACTIVATE THE TOOLBAR CONTROLS
        
        //deactive level score buttons
        guiButtons.get(LEVEL_SCORE_PLAY_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(LEVEL_SCORE_PLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_SCORE_QUIT_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(LEVEL_SCORE_QUIT_BUTTON_TYPE).setEnabled(false);
        
        //disable stars
        guiDecor.get(LEVEL_SCORE_STAR_TYPE + 1).setState(INVISIBLE_STATE);
        guiDecor.get(LEVEL_SCORE_STAR_TYPE + 2).setState(INVISIBLE_STATE);
        guiDecor.get(LEVEL_SCORE_STAR_TYPE + 3).setState(INVISIBLE_STATE);
        
        //activate the gameplay UI
        guiButtons.get(GAMEPLAY_QUIT_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(GAMEPLAY_QUIT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(GAMEPLAY_SMASHER_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(GAMEPLAY_SMASHER_BUTTON_TYPE).setEnabled(true);
        
        //enable the board Spaces that need to be active
        ZombieCrushSagaBoardSpace tempSpace = ((ZombieCrushSagaDataModel)data).getBoardSpace();
        ZombieCrushSagaBoardJelly tempJelly = ((ZombieCrushSagaDataModel)data).getBoardJelly();
        //only render a space if there's no jelly on top of it.
        for(int i = 0; i < BOARD_ROWS; i++){
            for(int j = 0; j < BOARD_COLUMNS; j++){
                if(tempSpace.getBoardSpace(i, j) == true && tempJelly.getBoardJelly(i, j) == false)
                    guiDecor.get(BOARD_SPACE_TYPE + i + j).setState(VISIBLE_STATE);
            }
        }
        //enable the board Jelly that need to be active
        for(int i = 0; i < BOARD_ROWS; i++){
            for(int j = 0; j < BOARD_COLUMNS; j++){
                if(tempJelly.getBoardJelly(i, j) == true)
                    guiDecor.get(BOARD_JELLY_TYPE + i + j).setState(VISIBLE_STATE);
            }
        }
        
        // MOVE THE Zombies TO THE STACK AND MAKE THEM VISIBLE
        ((ZombieCrushSagaDataModel)data).enableZombies(true);
        
        data.reset(this);
        //ensure a random board.
        ((ZombieCrushSagaDataModel)data).randomizeBoardZombies();
        
        // AND CHANGE THE SCREEN STATE
        currentScreenState = GAMEPLAY_SCREEN_STATE;
        
        // PLAY THE GAMEPLAY SCREEN SONG
        //        audio.stop(ZombieCrushSagaPropertyType.SPLASH_SCREEN_SONG_CUE.toString());
        //  audio.play(ZombieCrushSagaPropertyType.GAMEPLAY_SONG_CUE.toString(), true);
    }
    
    /**
     * This method switches the application to the splash screen, making
     * all the appropriate UI controls visible & invisible.
     */
    public void switchToSplashScreen()
    {
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(SPLASH_SCREEN_STATE);
        
        // DEACTIVATE THE TOOLBAR CONTROLS
        guiButtons.get(SPLASH_PLAY_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(SPLASH_PLAY_BUTTON_TYPE).setEnabled(false);
        
        // ACTIVATE THE LEVEL SELECT BUTTONS
        // DEACTIVATE THE LEVEL SELECT BUTTONS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> levels = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.LEVEL_OPTIONS);
        for (String level : levels)
        {
            guiButtons.get(level).setState(INVISIBLE_STATE);
            guiButtons.get(level).setEnabled(false);
        }
        
        // DEACTIVATE ALL DIALOGS
        guiDialogs.get(WIN_DIALOG_TYPE).setState(INVISIBLE_STATE);
        guiDialogs.get(LOSS_DIALOG_TYPE).setState(INVISIBLE_STATE);
        guiDialogs.get(STATS_DIALOG_TYPE).setState(INVISIBLE_STATE);
        
        // HIDE THE TILES
        ((ZombieCrushSagaDataModel)data).enableZombies(false);
        
        // MAKE THE CURRENT SCREEN THE SPLASH SCREEN
        currentScreenState = SPLASH_SCREEN_STATE;
        
        // PLAY THE WELCOME SCREEN SONG
        // audio.play(ZombieCrushSagaPropertyType.SPLASH_SCREEN_SONG_CUE.toString(), true);
        // audio.stop(ZombieCrushSagaPropertyType.GAMEPLAY_SONG_CUE.toString());
    }
    
    /**
     * This method switches the application to the saga screen, making
     * all the appropriate UI controls visible & invisible.
     */
    public void switchToSagaScreen()
    {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(SAGA_SCREEN_STATE);
        
        // Display the bottom of the background
        guiDecor.get(BACKGROUND_TYPE).setY(guiDecor.get(BACKGROUND_TYPE).getAABBheight() * -1 + this.getDataModel().getGameHeight());
        
        // ACTIVATE THE TOOLBAR AND ITS CONTROLS
        
        //turn off level_score UI
        guiButtons.get(LEVEL_SCORE_PLAY_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(LEVEL_SCORE_PLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_SCORE_QUIT_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(LEVEL_SCORE_QUIT_BUTTON_TYPE).setEnabled(false);
        
        //disable stars
        guiDecor.get(LEVEL_SCORE_STAR_TYPE + 1).setState(INVISIBLE_STATE);
        guiDecor.get(LEVEL_SCORE_STAR_TYPE + 2).setState(INVISIBLE_STATE);
        guiDecor.get(LEVEL_SCORE_STAR_TYPE + 3).setState(INVISIBLE_STATE);
        
        //turn on the Saga screen buttons
        guiButtons.get(SAGA_QUIT_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(SAGA_QUIT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SAGA_SCROLL_UP_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(SAGA_SCROLL_UP_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SAGA_SCROLL_DOWN_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(SAGA_SCROLL_DOWN_BUTTON_TYPE).setEnabled(true);
        
        // DEACTIVATE THE Splash SELECT BUTTONS
        ArrayList<String> splashes = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.SPLASH_OPTIONS);
        for (String splash : splashes)
        {
            guiButtons.get(splash).setState(INVISIBLE_STATE);
            guiButtons.get(splash).setEnabled(false);
        }
        
        // Activate all of the Level Select Buttons, one at a time, stop when the player has reached his score
        ArrayList<String> levels = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.LEVEL_OPTIONS);
        String dataPath = props.getProperty(ZombieCrushSagaPropertyType.DATA_PATH);
        //always turn on level 1.
        guiButtons.get(levels.get(0)).setState(VISIBLE_STATE);
        guiButtons.get(levels.get(0)).setEnabled(true);
        
        //if one level shouldn't be visible, then the others after shouldn't.
        for (int i = 1; i < levels.size(); i++)
        {
            //get the leveldata for the previous level.
            fileManager.loadLevel(dataPath + levels.get(i - 1).substring(2));
            
            //if the previous level's high score has a star
            if(record.getHighScore(dataPath + levels.get(i - 1).substring(2)) >= ((ZombieCrushSagaDataModel)data).getStarGoal(0)){
                guiButtons.get(levels.get(i)).setState(VISIBLE_STATE);
                guiButtons.get(levels.get(i)).setEnabled(true);
            }
            else
                break;
        }
        
         // MOVE THE Zombies TO THE STACK AND MAKE THEM not visible
        ((ZombieCrushSagaDataModel)data).enableZombies(false);
        
        // AND CHANGE THE SCREEN STATE
        currentScreenState = SAGA_SCREEN_STATE;
        
        // PLAY THE GAMEPLAY SCREEN SONG
        // audio.stop(ZombieCrushSagaPropertyType.SPLASH_SCREEN_SONG_CUE.toString());
        // audio.play(ZombieCrushSagaPropertyType.GAMEPLAY_SONG_CUE.toString(), true);
    }
    
    /**
     * This method switches the application to the level score screen, making
     * all the appropriate UI controls visible & invisible.
     */
    public void switchToLevelScoreScreen(){
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(LEVEL_SCORE_SCREEN_STATE);
        
        // Display the bottom of the background
        guiDecor.get(BACKGROUND_TYPE).setY(0);
        
        // ACTIVATE THE TOOLBAR AND ITS CONTROLS
        //turn on play button
        guiButtons.get(LEVEL_SCORE_PLAY_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(LEVEL_SCORE_PLAY_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_SCORE_QUIT_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(LEVEL_SCORE_QUIT_BUTTON_TYPE).setEnabled(true);
        
        //turn off the Saga screen buttons
        guiButtons.get(SAGA_QUIT_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(SAGA_QUIT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SAGA_SCROLL_UP_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(SAGA_SCROLL_UP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SAGA_SCROLL_DOWN_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(SAGA_SCROLL_DOWN_BUTTON_TYPE).setEnabled(false);
        
        //turn off the gameplay stuff
        guiButtons.get(GAMEPLAY_QUIT_BUTTON_TYPE).setEnabled(false);
        guiDialogs.get(LOSS_DIALOG_TYPE).setState(INVISIBLE_STATE);
        guiDialogs.get(STATS_DIALOG_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(GAMEPLAY_REPLAY_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(GAMEPLAY_REPLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(GAMEPLAY_SMASHER_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(GAMEPLAY_SMASHER_BUTTON_TYPE).setEnabled(false);
        
        
        // Activate all of the Level Select Buttons
        // DEACTIVATE THE LEVEL SELECT BUTTONS
        ArrayList<String> levels = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.LEVEL_OPTIONS);
        for (String level : levels)
        {
            guiButtons.get(level).setState(INVISIBLE_STATE);
            guiButtons.get(level).setEnabled(false);
        }
        
        //check amount of stars to turn VISIBLE_STATE based on score.
        int highScore = record.getHighScore(((ZombieCrushSagaDataModel)data).getCurrentLevel());
        if(highScore > ((ZombieCrushSagaDataModel)data).getStarGoal(0))
            guiDecor.get(LEVEL_SCORE_STAR_TYPE + 1).setState(VISIBLE_STATE);
        if(highScore > ((ZombieCrushSagaDataModel)data).getStarGoal(1))
            guiDecor.get(LEVEL_SCORE_STAR_TYPE + 2).setState(VISIBLE_STATE);
        if(highScore > ((ZombieCrushSagaDataModel)data).getStarGoal(2))
            guiDecor.get(LEVEL_SCORE_STAR_TYPE + 3).setState(VISIBLE_STATE);
        
        //enable the board SpacesString
        for(int i = 0; i < BOARD_ROWS; i++){
            for(int j = 0; j < BOARD_COLUMNS; j++){
                guiDecor.get(BOARD_SPACE_TYPE + i + j).setState(INVISIBLE_STATE);
            }
        }
        //enable the board Jelly that need to be active
        for(int i = 0; i < BOARD_ROWS; i++){
            for(int j = 0; j < BOARD_COLUMNS; j++){
                    guiDecor.get(BOARD_JELLY_TYPE + i + j).setState(INVISIBLE_STATE);
            }
        }
        // MOVE THE Zombies TO THE STACK AND MAKE THEM VISIBLE
        ((ZombieCrushSagaDataModel)data).enableZombies(false);
        
        // AND CHANGE THE SCREEN STATE
        currentScreenState = LEVEL_SCORE_SCREEN_STATE;
        
    }
    
    /**
     * Scrolls the saga background and buttons with level buttons.
     *
     * @param String
     */
    public void scrollSagaBackground(String direction)
    {
        //add tjhe buttons later.
        
        //y = location of background on the canvas
        //maxHeight = maximum height of the background.
        float y = guiDecor.get(BACKGROUND_TYPE).getY();
        float maxHeight = guiDecor.get(BACKGROUND_TYPE).getAABBheight() * -1 + this.getDataModel().getGameHeight();
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> levels = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.LEVEL_OPTIONS);
        
        if(direction.equals("up"))
        {
            //checks to see if we're going beyond the upper boundaries.
            if(y + SAGA_SCROLL_AMOUNT > 0){
                targetY = 0;
                //to ensure we do not "overshoot" our target
                guiDecor.get(BACKGROUND_TYPE).setVy((targetY - y)/4);
                
                for (String level : levels)
                {
                    guiButtons.get(level).setVy((targetY - y)/4);
                }
            }
            else {
                targetY = y + SAGA_SCROLL_AMOUNT;
                guiDecor.get(BACKGROUND_TYPE).setVy(SAGA_SCROLL_SPEED);
                
                for (String level : levels)
                {
                    guiButtons.get(level).setVy(SAGA_SCROLL_SPEED);
                }
            }
        }
        else if (direction.equals("down"))
        {
            //checks to see if we're going beyond the lower boundaries.
            if(y - SAGA_SCROLL_AMOUNT <= maxHeight) {
                targetY = maxHeight;
                //to ensure we do not "overshoot" our target
                guiDecor.get(BACKGROUND_TYPE).setVy((targetY - y)/4);
                
                for (String level : levels)
                {
                    guiButtons.get(level).setVy((targetY - y)/4);
                }
            }
            else {
                targetY = y - SAGA_SCROLL_AMOUNT;
                guiDecor.get(BACKGROUND_TYPE).setVy(-SAGA_SCROLL_SPEED);
                
                for (String level : levels)
                {
                    guiButtons.get(level).setVy(-SAGA_SCROLL_SPEED);
                }
            }
        }
    }
    
    /**
     * This method updates the game grid boundaries, which will depend
     * on the level loaded.
     */
    public void updateBoundaries()
    {
        // NOTE THAT THE ONLY ONES WE CARE ABOUT ARE THE LEFT & TOP BOUNDARIES
        float totalWidth = ((ZombieCrushSagaDataModel)data).getGridColumns() * ZOMBIE_IMAGE_WIDTH;
        float halfTotalWidth = totalWidth/2.0f;
        float halfViewportWidth = data.getGameWidth()/2.0f;
        boundaryLeft = halfViewportWidth - halfTotalWidth;
        
        // THE LEFT & TOP BOUNDARIES ARE WHERE WE START RENDERING TILES IN THE GRID
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        float topOffset = Integer.parseInt(props.getProperty(ZombieCrushSagaPropertyType.GAME_TOP_OFFSET.toString()));
        float totalHeight = ((ZombieCrushSagaDataModel)data).getGridRows() * ZOMBIE_IMAGE_HEIGHT;
        float halfTotalHeight = totalHeight/2.0f;
        float halfViewportHeight = (data.getGameHeight() - topOffset)/2.0f;
        boundaryTop = topOffset + halfViewportHeight - halfTotalHeight;
    }
    
    // METHODS OVERRIDDEN FROM MiniGame
    // - initAudioContent
    // - initData
    // - initGUIControls
    // - initGUIHandlers
    // - reset
    // - updateGUI
    
    @Override
    /**
     * Initializes the sound and music to be used by the application.
     */
    public void initAudioContent()
    {
        
        return;
        
        
        //i disabled this cause this shit is annoying.
        //        try
        //        {
        //            PropertiesManager props = PropertiesManager.getPropertiesManager();
        //            String audioPath = props.getProperty(ZombieCrushSagaPropertyType.AUDIO_PATH);
        //
        //            // LOAD ALL THE AUDIO
        //            loadAudioCue(ZombieCrushSagaPropertyType.SELECT_AUDIO_CUE);
        //            loadAudioCue(ZombieCrushSagaPropertyType.MATCH_AUDIO_CUE);
        //            loadAudioCue(ZombieCrushSagaPropertyType.NO_MATCH_AUDIO_CUE);
        //            loadAudioCue(ZombieCrushSagaPropertyType.BLOCKED_TILE_AUDIO_CUE);
        //            loadAudioCue(ZombieCrushSagaPropertyType.UNDO_AUDIO_CUE);
        //            loadAudioCue(ZombieCrushSagaPropertyType.WIN_AUDIO_CUE);
        //            loadAudioCue(ZombieCrushSagaPropertyType.LOSS_AUDIO_CUE);
        //            loadAudioCue(ZombieCrushSagaPropertyType.SPLASH_SCREEN_SONG_CUE);
        //            loadAudioCue(ZombieCrushSagaPropertyType.GAMEPLAY_SONG_CUE);
        //
        //            // PLAY THE WELCOME SCREEN SONG
        //            audio.play(ZombieCrushSagaPropertyType.SPLASH_SCREEN_SONG_CUE.toString(), true);
        //        }
        //        catch(UnsupportedAudioFileException | IOException | LineUnavailableException | InvalidMidiDataException | MidiUnavailableException e)
        //        {
        //            errorHandler.processError(ZombieCrushSagaPropertyType.AUDIO_FILE_ERROR);
        //        }
    }
    
    /**
     * This helper method loads the audio file associated with audioCueType,
     * which should have been specified via an XML properties file.
     */
    @SuppressWarnings("unused")
	private void loadAudioCue(ZombieCrushSagaPropertyType audioCueType)
            throws  UnsupportedAudioFileException, IOException, LineUnavailableException,
            InvalidMidiDataException, MidiUnavailableException
    {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String audioPath = props.getProperty(ZombieCrushSagaPropertyType.AUDIO_PATH);
        String cue = props.getProperty(audioCueType.toString());
        audio.loadAudio(audioCueType.toString(), audioPath + cue);
    }
    
    /**
     * Initializes the game data used by the application. Note
     * that it is this method's obligation to construct and set
     * this Game's custom GameDataModel object as well as any
     * other needed game objects.
     */
    @Override
    public void initData()
    {
        // INIT OUR ERROR HANDLER
        errorHandler = new ZombieCrushSagaErrorHandler(window);
        
        // INIT OUR FILE MANAGER
        fileManager = new ZombieCrushSagaFileManager(this);
        
        // LOAD THE PLAYER'S RECORD FROM A FILE
        record = fileManager.loadRecord();
        
        // INIT OUR DATA MANAGER
        data = new ZombieCrushSagaDataModel(this);
        
        // LOAD THE GAME DIMENSIONS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        int gameWidth = Integer.parseInt(props.getProperty(ZombieCrushSagaPropertyType.GAME_WIDTH.toString()));
        int gameHeight = Integer.parseInt(props.getProperty(ZombieCrushSagaPropertyType.GAME_HEIGHT.toString()));
        data.setGameDimensions(gameWidth, gameHeight);
        
        // THIS WILL CHANGE WHEN WE LOAD A LEVEL
        boundaryLeft = Integer.parseInt(props.getProperty(ZombieCrushSagaPropertyType.GAME_LEFT_OFFSET.toString()));
        boundaryTop = Integer.parseInt(props.getProperty(ZombieCrushSagaPropertyType.GAME_TOP_OFFSET.toString()));
        boundaryRight = gameWidth - boundaryLeft;
        boundaryBottom = gameHeight;
    }
    
    /**
     * Initializes the game controls, like buttons, used by
     * the game application. Note that this includes the tiles,
     * which serve as buttons of sorts.
     */
    @Override
    public void initGUIControls()
    {
        // WE'LL USE AND REUSE THESE FOR LOADING STUFF
        BufferedImage img;
        float x, y;
        SpriteType sT;
        Sprite s;
        
        // FIRST PUT THE ICON IN THE WINDOW
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(ZombieCrushSagaPropertyType.IMG_PATH);
        String windowIconFile = props.getProperty(ZombieCrushSagaPropertyType.WINDOW_ICON);
        img = loadImage(imgPath + windowIconFile);
        window.setIconImage(img);
        
        // CONSTRUCT THE PANEL WHERE WE'LL DRAW EVERYTHING
        //this will draw: the level score stats.
        canvas = new ZombieCrushSagaPanel(this, (ZombieCrushSagaDataModel)data);
        
        // LOAD THE BACKGROUNDS, WHICH ARE GUI DECOR
        currentScreenState = SPLASH_SCREEN_STATE;
        sT = new SpriteType(BACKGROUND_TYPE);
        img = loadImage(imgPath + props.getProperty(ZombieCrushSagaPropertyType.SPLASH_BACKGROUND_IMAGE_NAME));
        sT.addState(SPLASH_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(ZombieCrushSagaPropertyType.SAGA_BACKGROUND_IMAGE_NAME));
        sT.addState(SAGA_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(ZombieCrushSagaPropertyType.LEVEL_SCORE_BACKGROUND_IMAGE_NAME));
        sT.addState(LEVEL_SCORE_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(ZombieCrushSagaPropertyType.GAMEPLAY_BACKGROUND_IMAGE_NAME));
        sT.addState(GAMEPLAY_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, SPLASH_SCREEN_STATE);
        guiDecor.put(BACKGROUND_TYPE, s);
        
        //Splash Screen - Add play, reset, and quit button.
        ArrayList<String> splash = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.SPLASH_OPTIONS);
        ArrayList<String> splashImageNames = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.SPLASH_IMAGE_OPTIONS);
        ArrayList<String> splashMouseOverImageNames = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.SPLASH_MOUSE_OVER_IMAGE_OPTIONS);
        float totalWidth = splash.size() * (SPLASH_BUTTON_WIDTH + SPLASH_BUTTON_MARGIN) - SPLASH_BUTTON_MARGIN;
        float gameWidth = Integer.parseInt(props.getProperty(ZombieCrushSagaPropertyType.GAME_WIDTH));
        x = (gameWidth - totalWidth)/2.0f;
        y = SPLASH_BUTTON_Y;
        for (int i = 0; i < splash.size(); i++)
        {
            sT = new SpriteType(SPLASH_BUTTON_TYPE);
            img = loadImageWithColorKey(imgPath + splashImageNames.get(i), COLOR_KEY);
            sT.addState(VISIBLE_STATE, img);
            img = loadImageWithColorKey(imgPath + splashMouseOverImageNames.get(i), COLOR_KEY);
            sT.addState(MOUSE_OVER_STATE, img);
            s = new Sprite(sT, x, y, 0, 0, VISIBLE_STATE);
            guiButtons.put(splash.get(i), s);
            x += SPLASH_BUTTON_WIDTH + SPLASH_BUTTON_MARGIN;
        }
        //Saga Screen UI
        // THEN THE QUIT BUTTON
        String sagaQuitButton = props.getProperty(ZombieCrushSagaPropertyType.SAGA_QUIT_BUTTON_IMAGE_NAME);
        sT = new SpriteType(SAGA_QUIT_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + sagaQuitButton, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        String sagaQuitMouseOverButton = props.getProperty(ZombieCrushSagaPropertyType.SAGA_QUIT_BUTTON_MOUSE_OVER_IMAGE_NAME);
        img = loadImageWithColorKey(imgPath + sagaQuitMouseOverButton, COLOR_KEY);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, SAGA_QUIT_BUTTON_X, SAGA_QUIT_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(SAGA_QUIT_BUTTON_TYPE, s);
        guiButtons.get(SAGA_QUIT_BUTTON_TYPE).setEnabled(false);
        
        // THEN THE Scroll up BUTTON
        String sagaScrollUpButton = props.getProperty(ZombieCrushSagaPropertyType.SAGA_SCROLL_UP_BUTTON_IMAGE_NAME);
        sT = new SpriteType(SAGA_SCROLL_UP_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + sagaScrollUpButton, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        String sagaScrollUpMouseOverButton = props.getProperty(ZombieCrushSagaPropertyType.SAGA_SCROLL_UP_BUTTON_MOUSE_OVER_IMAGE_NAME);
        img = loadImageWithColorKey(imgPath + sagaScrollUpMouseOverButton, COLOR_KEY);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, SAGA_SCROLL_UP_BUTTON_X, SAGA_SCROLL_UP_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(SAGA_SCROLL_UP_BUTTON_TYPE, s);
        guiButtons.get(SAGA_SCROLL_UP_BUTTON_TYPE).setEnabled(false);
        // THEN THE Scroll down BUTTON
        String sagaScrollDownButton = props.getProperty(ZombieCrushSagaPropertyType.SAGA_SCROLL_DOWN_BUTTON_IMAGE_NAME);
        sT = new SpriteType(SAGA_SCROLL_DOWN_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + sagaScrollDownButton, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        String sagaScrollDownMouseOverButton = props.getProperty(ZombieCrushSagaPropertyType.SAGA_SCROLL_DOWN_BUTTON_MOUSE_OVER_IMAGE_NAME);
        img = loadImageWithColorKey(imgPath + sagaScrollDownMouseOverButton, COLOR_KEY);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, SAGA_SCROLL_DOWN_BUTTON_X, SAGA_SCROLL_DOWN_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(SAGA_SCROLL_DOWN_BUTTON_TYPE, s);
        guiButtons.get(SAGA_SCROLL_DOWN_BUTTON_TYPE).setEnabled(false);
        
        // ADD A BUTTON FOR EACH LEVEL AVAILABLE
        ArrayList<String> levels = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.LEVEL_OPTIONS);
        ArrayList<String> levelImageNames = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.LEVEL_IMAGE_OPTIONS);
        ArrayList<String> levelMouseOverImageNames = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.LEVEL_MOUSE_OVER_IMAGE_OPTIONS);
        for (int i = 0; i < levels.size(); i++)
        {
            sT = new SpriteType(LEVEL_SELECT_BUTTON_TYPE);
            img = loadImageWithColorKey(imgPath + levelImageNames.get(i), COLOR_KEY);
            sT.addState(VISIBLE_STATE, img);
            img = loadImageWithColorKey(imgPath + levelMouseOverImageNames.get(i), COLOR_KEY);
            sT.addState(MOUSE_OVER_STATE, img);
            x = SAGA_LEVEL_ONE_BUTTON_X;
            y = SAGA_LEVEL_ONE_BUTTON_Y;
            
            //do the zigzag pattern on the map.
            int addX, addY;
            if((i / 5) % 2 == 0)//if even
                addX = (int)SAGA_LEVEL_BUTTON_X_DISTANCE * (i % 5);
            else    //if odd
                addX = (int)SAGA_LEVEL_BUTTON_X_DISTANCE * (4 - (i % 5));
            
            addY = (int)SAGA_LEVEL_BUTTON_Y_DISTANCE * (i / 5);
            
            s = new Sprite(sT, x + addX, y - addY, 0, 0, INVISIBLE_STATE);
            guiButtons.put(levels.get(i), s);   //normally levels have names of "level_#", # being a number > 0.
            guiButtons.get(levels.get(i)).setEnabled(false);
            x += LEVEL_BUTTON_WIDTH + LEVEL_BUTTON_MARGIN;
        }
        
        //Level_Score Screen buttons below
        // Close Level Score Button
        String levelScoreQuitButton = props.getProperty(ZombieCrushSagaPropertyType.LEVEL_SCORE_QUIT_BUTTON_IMAGE_NAME);
        sT = new SpriteType(LEVEL_SCORE_QUIT_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + levelScoreQuitButton, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        String levelScoreQuitMouseOverButton = props.getProperty(ZombieCrushSagaPropertyType.LEVEL_SCORE_QUIT_BUTTON_MOUSE_OVER_IMAGE_NAME);
        img = loadImageWithColorKey(imgPath + levelScoreQuitMouseOverButton, COLOR_KEY);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, LEVEL_SCORE_QUIT_BUTTON_X, LEVEL_SCORE_QUIT_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(LEVEL_SCORE_QUIT_BUTTON_TYPE, s);
        guiButtons.get(LEVEL_SCORE_QUIT_BUTTON_TYPE).setEnabled(false);
        
        //Play level button from the level score
        String levelScorePlayButton = props.getProperty(ZombieCrushSagaPropertyType.LEVEL_SCORE_PLAY_BUTTON_IMAGE_NAME);
        sT = new SpriteType(LEVEL_SCORE_PLAY_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + levelScorePlayButton, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        String levelScorePlayButtonMouseOverButton = props.getProperty(ZombieCrushSagaPropertyType.LEVEL_SCORE_PLAY_BUTTON_MOUSE_OVER_IMAGE_NAME);
        img = loadImageWithColorKey(imgPath + levelScorePlayButtonMouseOverButton, COLOR_KEY);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, LEVEL_SCORE_PLAY_BUTTON_X, LEVEL_SCORE_PLAY_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(LEVEL_SCORE_PLAY_BUTTON_TYPE, s);
        guiButtons.get(LEVEL_SCORE_PLAY_BUTTON_TYPE).setEnabled(false);
        
        //level score stars
        //star 1
        String levelScoreStar1 = props.getProperty(ZombieCrushSagaPropertyType.LEVEL_SCORE_STAR_IMAGE_NAME);
        sT = new SpriteType(LEVEL_SCORE_STAR_TYPE);
        img = loadImageWithColorKey(imgPath + levelScoreStar1, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        s = new Sprite(sT, LEVEL_SCORE_FIRST_STAR_X, LEVEL_SCORE_FIRST_STAR_Y, 0, 0, INVISIBLE_STATE);
        guiDecor.put(LEVEL_SCORE_STAR_TYPE + 1, s);
        //star 2
        String levelScoreStar2 = props.getProperty(ZombieCrushSagaPropertyType.LEVEL_SCORE_STAR_IMAGE_NAME);
        sT = new SpriteType(LEVEL_SCORE_STAR_TYPE);
        img = loadImageWithColorKey(imgPath + levelScoreStar2, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        s = new Sprite(sT, LEVEL_SCORE_FIRST_STAR_X - LEVEL_SCORE_STAR_OFFSET_X, LEVEL_SCORE_FIRST_STAR_Y, 0, 0, INVISIBLE_STATE);
        guiDecor.put(LEVEL_SCORE_STAR_TYPE + 2, s);
        //star 3
        String levelScoreStar3 = props.getProperty(ZombieCrushSagaPropertyType.LEVEL_SCORE_STAR_IMAGE_NAME);
        sT = new SpriteType(LEVEL_SCORE_STAR_TYPE);
        img = loadImageWithColorKey(imgPath + levelScoreStar3, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        s = new Sprite(sT, LEVEL_SCORE_FIRST_STAR_X + LEVEL_SCORE_STAR_OFFSET_X, LEVEL_SCORE_FIRST_STAR_Y, 0, 0, INVISIBLE_STATE);
        guiDecor.put(LEVEL_SCORE_STAR_TYPE + 3, s);
        
        //Gameplay buttons
            //play
        String gameplayPlayButton = props.getProperty(ZombieCrushSagaPropertyType.GAMEPLAY_REPLAY_BUTTON_IMAGE_NAME);
        sT = new SpriteType(GAMEPLAY_REPLAY_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + gameplayPlayButton, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        String gameplayPlayButtonMouseOverButton = props.getProperty(ZombieCrushSagaPropertyType.GAMEPLAY_REPLAY_BUTTON_MOUSE_OVER_IMAGE_NAME);
        img = loadImageWithColorKey(imgPath + gameplayPlayButtonMouseOverButton, COLOR_KEY);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, GAMEPLAY_REPLAY_BUTTON_X, GAMEPLAY_REPLAY_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(GAMEPLAY_REPLAY_BUTTON_TYPE, s);
        guiButtons.get(GAMEPLAY_REPLAY_BUTTON_TYPE).setEnabled(false);
            //quit button
        String gameplayQuitButton = props.getProperty(ZombieCrushSagaPropertyType.GAMEPLAY_QUIT_BUTTON_IMAGE_NAME);
        sT = new SpriteType(GAMEPLAY_QUIT_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + gameplayQuitButton, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        String gameplayQuitButtonMouseOverButton = props.getProperty(ZombieCrushSagaPropertyType.GAMEPLAY_QUIT_BUTTON_MOUSE_OVER_IMAGE_NAME);
        img = loadImageWithColorKey(imgPath + gameplayQuitButtonMouseOverButton, COLOR_KEY);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, GAMEPLAY_QUIT_BUTTON_X, GAMEPLAY_QUIT_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(GAMEPLAY_QUIT_BUTTON_TYPE, s);
        guiButtons.get(GAMEPLAY_QUIT_BUTTON_TYPE).setEnabled(false);
                //smasher button
        String gameplaySmasherButton = props.getProperty(ZombieCrushSagaPropertyType.GAMEPLAY_SMASHER_BUTTON_IMAGE_NAME);
        sT = new SpriteType(GAMEPLAY_SMASHER_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + gameplaySmasherButton, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        String gameplaySmasherButtonMouseOverButton = props.getProperty(ZombieCrushSagaPropertyType.GAMEPLAY_SMASHER_BUTTON_MOUSE_OVER_IMAGE_NAME);
        img = loadImageWithColorKey(imgPath + gameplaySmasherButtonMouseOverButton, COLOR_KEY);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, GAMEPLAY_SMASHER_BUTTON_X, GAMEPLAY_SMASHER_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(GAMEPLAY_SMASHER_BUTTON_TYPE, s);
        guiButtons.get(GAMEPLAY_SMASHER_BUTTON_TYPE).setEnabled(false);
        
        //theBoardSpaces ( 9 x 9 = 81)
        //and 81 sprites
        for(int i = 0; i < BOARD_ROWS; i++){
            for(int j = 0; j < BOARD_COLUMNS; j++){
                
                String boardSpace = props.getProperty(
                        ZombieCrushSagaPropertyType.BOARD_SPACE_IMAGE_NAME);
                sT = new SpriteType(BOARD_SPACE_TYPE);
                img = loadImageWithColorKey(imgPath + boardSpace, COLOR_KEY);
                sT.addState(VISIBLE_STATE, img);
                s = new Sprite(sT,
                        BOARD_FIRST_SPACE_X + (BOARD_SPACE_SIZE * j),
                        BOARD_FIRST_SPACE_Y + (BOARD_SPACE_SIZE * i),
                        0, 0, INVISIBLE_STATE);
                guiDecor.put(BOARD_SPACE_TYPE + i + j, s);
                guiDecor.get(BOARD_SPACE_TYPE + i + j).setEnabled(false);
                
            }
        }
        
        //theBoardJelly ( 9 x 9 = 81)
        //and 81 sprites
        for(int i = 0; i < BOARD_ROWS; i++){
            for(int j = 0; j < BOARD_COLUMNS; j++){
                
                String boardJelly = props.getProperty(
                        ZombieCrushSagaPropertyType.BOARD_JELLY_IMAGE_NAME);
                sT = new SpriteType(BOARD_JELLY_TYPE);
                img = loadImageWithColorKey(imgPath + boardJelly, COLOR_KEY);
                sT.addState(VISIBLE_STATE, img);
                
                String boardJellyAnimated = props.getProperty(
                        ZombieCrushSagaPropertyType.BOARD_JELLY_ANIMATED_IMAGE_NAME);
                img = loadImageWithColorKey(imgPath + boardJellyAnimated, COLOR_KEY);
                sT.addState(ANIMATED_STATE_PREFIX + 0, img);
                
                s = new Sprite(sT,
                        BOARD_FIRST_SPACE_X + (BOARD_SPACE_SIZE * j),
                        BOARD_FIRST_SPACE_Y + (BOARD_SPACE_SIZE * i),
                        0, 0, INVISIBLE_STATE);
                guiDecor.put(BOARD_JELLY_TYPE + i + j, s);
                guiDecor.get(BOARD_JELLY_TYPE + i + j).setEnabled(false);
                
            }
        }
        
        // NOW ADD THE DIALOGS
        // AND THE STATS DISPLAY
        String statsDialog = props.getProperty(ZombieCrushSagaPropertyType.STATS_DIALOG_IMAGE_NAME);
        sT = new SpriteType(STATS_DIALOG_TYPE);
        img = loadImageWithColorKey(imgPath + statsDialog, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        x = (data.getGameWidth()/2) - (img.getWidth(null)/2);
        y = (data.getGameHeight()/2) - (img.getHeight(null)/2);
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE_STATE);
        guiDialogs.put(STATS_DIALOG_TYPE, s);
        
        // AND THE WIN CONDITION DISPLAY
        String winDisplay = props.getProperty(ZombieCrushSagaPropertyType.WIN_DIALOG_IMAGE_NAME);
        sT = new SpriteType(WIN_DIALOG_TYPE);
        img = loadImageWithColorKey(imgPath + winDisplay, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        x = (data.getGameWidth()/2) - (img.getWidth(null)/2);
        y = (data.getGameHeight()/2) - (img.getHeight(null)/2);
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE_STATE);
        guiDialogs.put(WIN_DIALOG_TYPE, s);
        
        // AND THE LOSS CONDITION DISPLAY
        String lossDisplay = props.getProperty(ZombieCrushSagaPropertyType.LOSS_DIALOG_IMAGE_NAME);
        sT = new SpriteType(LOSS_DIALOG_TYPE);
        img = loadImageWithColorKey(imgPath + lossDisplay, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        x = (data.getGameWidth()/2) - (img.getWidth(null)/2);
        y = (data.getGameHeight()/2) - (img.getHeight(null)/2);
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE_STATE);
        guiDialogs.put(LOSS_DIALOG_TYPE, s);
        
        // THEN THE Zombies STACKED TO THE TOP LEFT
        ((ZombieCrushSagaDataModel)data).initZombies();
    }
    
    /**
     * Initializes the game event handlers for things like
     * game gui buttons.
     */
    @Override
    public void initGUIHandlers()
    {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String dataPath = props.getProperty(ZombieCrushSagaPropertyType.DATA_PATH);
        
        // WE'LL HAVE A CUSTOM RESPONSE FOR WHEN THE USER CLOSES THE WINDOW
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        ExitHandler eh = new ExitHandler(this);
        window.addWindowListener(eh);
        
        //SPLASH BUTTONS: Play, Reset, Quit
        // Splash Play EVENT HANDLER
        SplashPlayHandler sph = new SplashPlayHandler(this);
        guiButtons.get(SPLASH_PLAY_BUTTON_TYPE).setActionListener(sph);
        //Splash Reset EVENT HANDLER
        SplashResetHandler srh = new SplashResetHandler(this);
        guiButtons.get(SPLASH_RESET_BUTTON_TYPE).setActionListener(srh);
        // Splash Quit EVENT HANDLER
        SplashQuitHandler sqh = new SplashQuitHandler(this);
        guiButtons.get(SPLASH_QUIT_BUTTON_TYPE).setActionListener(sqh);
        
        //SAGA BUTTONS: Quit, Scroll up, Scroll down
        guiButtons.get(SAGA_QUIT_BUTTON_TYPE).setActionListener(sqh);
        SagaScrollHandler sagaSUH = new SagaScrollHandler(this, "up");
        SagaScrollHandler sagaSDH = new SagaScrollHandler(this, "down");
        guiButtons.get(SAGA_SCROLL_UP_BUTTON_TYPE).setActionListener(sagaSUH);
        guiButtons.get(SAGA_SCROLL_DOWN_BUTTON_TYPE).setActionListener(sagaSDH);
        
        // LEVEL BUTTON EVENT HANDLERS
        ArrayList<String> levels = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.LEVEL_OPTIONS);
        for (String levelFile : levels)
        {
            SagaSelectLevelHandler slh = new SagaSelectLevelHandler(this, dataPath + levelFile.substring(2));
            guiButtons.get(levelFile).setActionListener(slh);
        }
        
        //Level Score Buttons
            //Quit Button
        LevelScoreCloseHandler clsh = new LevelScoreCloseHandler(this);
        guiButtons.get(LEVEL_SCORE_QUIT_BUTTON_TYPE).setActionListener(clsh);
            //for the one level button we have.
        LevelScorePlayLevelHandler plh = new LevelScorePlayLevelHandler(this, dataPath + null);
        guiButtons.get(LEVEL_SCORE_PLAY_BUTTON_TYPE).setActionListener(plh);
        
        //Set the gameplay buttons
            //quit
        GameplayQuitHandler gqh = new GameplayQuitHandler(this);
        guiButtons.get(GAMEPLAY_QUIT_BUTTON_TYPE).setActionListener(gqh);
            //replay
        GameplayReplayHandler grh = new GameplayReplayHandler(this);
        guiButtons.get(GAMEPLAY_REPLAY_BUTTON_TYPE).setActionListener(grh);
           //smasher
        GameplaySmasherHandler gsh = new GameplaySmasherHandler(this);
        guiButtons.get(GAMEPLAY_SMASHER_BUTTON_TYPE).setActionListener(gsh);
        
        
        ZombieCrushSagaKeyHandler zkh = new ZombieCrushSagaKeyHandler(this);
        this.setKeyListener(zkh);
        
    }
    
    /**
     * Invoked when a new game is started, it resets all relevant
     * game data and gui control states.
     */
    @Override
    public void reset()
    {
        audio.play(ZombieCrushSagaPropertyType.GAMEPLAY_SONG_CUE.toString(), true);
        data.reset(this);
    }
    
    /**
     * Updates the state of all gui controls according to the
     * current game conditions.
     */
    @Override
    public void updateGUI()
    {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> levels = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.LEVEL_OPTIONS);
        
        //update the Saga Screen UI Elements if it's being scrolled until
        //it reaches its target. update the sprite
        if(this.getGUIDecor().get(BACKGROUND_TYPE).getY() != targetY){
            this.getGUIDecor().get(BACKGROUND_TYPE).update(this);
            for (String level : levels)
            {
                guiButtons.get(level).update(this);
            }
        }
        else{ //if the target is reached, stop moving the Saga UI Elements (Map and Buttons).
            for (String level : levels)
            {
                guiButtons.get(level).setVy(0);
            }
            guiDecor.get(BACKGROUND_TYPE).setVy(0);
        }
        
        //update the boardSpaces and boardJelly only in the gameplay screen
        if(currentScreenState.equals(GAMEPLAY_SCREEN_STATE)){
            ZombieCrushSagaBoardSpace tempSpace = ((ZombieCrushSagaDataModel)data).getBoardSpace();
            ZombieCrushSagaBoardJelly tempJelly = ((ZombieCrushSagaDataModel)data).getBoardJelly();
            for(int row = 0; row < BOARD_ROWS; row++){
                for(int column = 0; column < BOARD_COLUMNS; column++){
                    
                    //if this jelly is visible
                    if(this.getGUIDecor().get(BOARD_JELLY_TYPE + row + column).getState().equals(VISIBLE_STATE))
                        if(!tempJelly.getBoardJelly(row, column)){ //if there is no more jelly anymore, animate it.
                            this.getGUIDecor().get(BOARD_JELLY_TYPE + row + column).setState(ANIMATED_STATE_PREFIX + 0);
                            continue;
                        }
                            
                    //if there's no jelly, and an empty space, draw a space instead.
                    if(!tempJelly.getBoardJelly(row, column) && tempSpace.getBoardSpace(row, column)){
                        this.getGUIDecor().get(BOARD_JELLY_TYPE + row + column).setState(INVISIBLE_STATE);
                        this.getGUIDecor().get(BOARD_SPACE_TYPE + row + column).setState(VISIBLE_STATE);
                        continue;
                    }
                    
                    //if there's jelly, draw the jelly.
                    if(tempJelly.getBoardJelly(row, column)){
                        this.getGUIDecor().get(BOARD_JELLY_TYPE + row + column).setState(VISIBLE_STATE);
                        this.getGUIDecor().get(BOARD_SPACE_TYPE + row + column).setState(INVISIBLE_STATE);
                        continue;
                    }
                    
                }
            }
        }
        
        // GO THROUGH THE VISIBLE BUTTONS TO TRIGGER MOUSE OVERS
        Iterator<Sprite> buttonsIt = guiButtons.values().iterator();
        while (buttonsIt.hasNext())
        {
            Sprite button = buttonsIt.next();
            
            // ARE WE ENTERING A BUTTON?
            if (button.getState().equals(VISIBLE_STATE))
            {
                if (button.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
                {
                    button.setState(MOUSE_OVER_STATE);
                }
            }
            // ARE WE EXITING A BUTTON?
            else if (button.getState().equals(MOUSE_OVER_STATE))
            {
                if (!button.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
                {
                    button.setState(VISIBLE_STATE);
                }
            }
        }
    }
}