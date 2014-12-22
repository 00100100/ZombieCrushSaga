package zombie_crush_saga.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JPanel;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import properties_manager.PropertiesManager;
import zombie_crush_saga.ZombieCrushSaga;
import zombie_crush_saga.data.ZombieCrushSagaDataModel;
import static zombie_crush_saga.ZombieCrushSagaConstants.*;
import zombie_crush_saga.data.ZombieCrushSagaRecord;

/**
 * This class performs all of the rendering for the ZombieCrushSaga game application.
 *
 * @author Richard McKenna
 */
public class ZombieCrushSagaPanel extends JPanel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// THIS IS ACTUALLY OUR ZombieCrushSaga Solitaire APP, WE NEED THIS
    // BECAUSE IT HAS THE GUI STUFF THAT WE NEED TO RENDER
    private MiniGame game;
    
    // AND HERE IS ALL THE GAME DATA THAT WE NEED TO RENDER
    private ZombieCrushSagaDataModel data;
    
    // WE'LL USE THIS TO FORMAT SOME TEXT FOR DISPLAY PURPOSES
    private NumberFormat numberFormatter;
    
    // WE'LL USE THIS AS THE BASE IMAGE FOR RENDERING UNSELECTED TILES
    private BufferedImage blankZombieImage;
    
    // WE'LL USE THIS AS THE BASE IMAGE FOR RENDERING SELECTED TILES
    private BufferedImage blankZombieSelectedImage;
    
    /**
     * This constructor stores the game and data references,
     * which we'll need for rendering.
     *
     * @param initGame the ZombieCrushSaga Solitaire game that is using
     * this panel for rendering.
     *
     * @param initData the ZombieCrushSaga Solitaire game data.
     */
    public ZombieCrushSagaPanel(MiniGame initGame, ZombieCrushSagaDataModel initData)
    {
        game = initGame;
        data = initData;
        numberFormatter = NumberFormat.getNumberInstance();
        numberFormatter.setMinimumFractionDigits(3);
        numberFormatter.setMaximumFractionDigits(3);
    }
    
    // MUTATOR METHODS
    // -setBlankTileImage
    // -setBlankZombieSelectedImage
    
    /**
     * This mutator method sets the base image to use for rendering tiles.
     *
     * @param initBlankZombieImage The image to use as the base for rendering tiles.
     */
    public void setBlankZombieImage(BufferedImage initBlankZombieImage)
    {
        blankZombieImage = initBlankZombieImage;
    }
    
    /**
     * This mutator method sets the base image to use for rendering selected tiles.
     *
     * @param initBlankTileSelectedImage The image to use as the base for rendering
     * selected tiles.
     */
    public void setBlankZombieSelectedImage(BufferedImage initBlankZombieSelectedImage)
    {
        blankZombieSelectedImage = initBlankZombieSelectedImage;
    }
    
    /**
     * This is where rendering starts. This method is called each frame, and the
     * entire game application is rendered here with the help of a number of
     * helper methods.
     *
     * @param g The Graphics context for this panel.
     */
    @Override
    public void paintComponent(Graphics g)
    {
        try
        {
            // MAKE SURE WE HAVE EXCLUSIVE ACCESS TO THE GAME DATA
            game.beginUsingData();
            
            // CLEAR THE PANEL
            super.paintComponent(g);
            
            // RENDER THE BACKGROUND, WHICHEVER SCREEN WE'RE ON
            renderBackground(g);
            
            // AND THE BUTTONS AND DECOR
            renderGUIControls(g);
            
            // AND THE TILES
            renderZombies(g);
            
            // AND THE DIALOGS, IF THERE ARE ANY
            renderDialogs(g);
            
            // AND THE moves left AND score STATS
            renderStats(g);
            
            //render end game victory stats.
            renderStatsLevel(g);
            
            // AND THE level score STATS
            renderLevelScore(g);
            
            //render progress bar
            renderProgressBar(g);
            
            //render zombie Smasher cursor
            renderCursor(g);
            
            //render individual game zombie scores in the game screen.
            renderZombieScore(g);
            
            // RENDERING THE GRID WHERE ALL THE TILES GO CAN BE HELPFUL
            // DURING DEBUGGIN TO BETTER UNDERSTAND HOW THEY RE LAID OUT
            //renderGrid(g); useless for now
            
            // AND FINALLY, TEXT FOR DEBUGGING
            //renderDebuggingText(g); useless for now
        }
        finally
        {
            // RELEASE THE LOCK
            game.endUsingData();
        }
    }
    
    // RENDERING HELPER METHODS
    // - renderBackground
    // - renderGUIControls
    // - renderTiles
    // - renderDialogs
    // - renderGrid
    // - renderDebuggingText
    
    /**
     * Renders the background image, which is different depending on the screen.
     *
     * @param g the Graphics context of this panel.
     */
    public void renderBackground(Graphics g)
    {
        // THERE IS ONLY ONE CURRENTLY SET
        Sprite bg = game.getGUIDecor().get(BACKGROUND_TYPE);
        renderSprite(g, bg);
    }
    
    /**
     * renders any custom cursors
     * 
     * @param g  the Graphics context of this panel.
     */
    public void renderCursor(Graphics g){
        
        if (((ZombieCrushSagaMiniGame)game).isCurrentScreenState(GAMEPLAY_SCREEN_STATE)
                && data.getZombieCrusherActive())
        {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String imgPath = props.getProperty(ZombieCrushSaga.ZombieCrushSagaPropertyType.IMG_PATH);
            String cursorImg = props.getProperty(ZombieCrushSaga.ZombieCrushSagaPropertyType.CURSOR_ZOMBIE_SMASHER);
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Image image = game.loadImageWithColorKey(imgPath + cursorImg, COLOR_KEY);
                
            Point thePoint = new Point(this.getX(), this.getY());
            
            Cursor c = toolkit.createCustomCursor(image , thePoint, "cursorCrap");
            this.setCursor(c);
        }
        else    //default cursor.
            setCursor (Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        
    }
    
    /**
     * Renders all the GUI decor and buttons.
     *
     * @param g this panel's rendering context.
     */
    public void renderGUIControls(Graphics g)
    {
        // GET EACH DECOR IMAGE ONE AT A TIME
        Collection<Sprite> decorSprites = game.getGUIDecor().values();
        for (Sprite s : decorSprites)
        {
            renderSprite(g, s);
        }
        
        // AND NOW RENDER THE BUTTONS
        Collection<Sprite> buttonSprites = game.getGUIButtons().values();
        for (Sprite s : buttonSprites)
        {
            renderSprite(g, s);
        }
        
    }
    
    /**
     * renders the Progress Bar
     * 
     * @param g this panel's rendering context.
     */
    public void renderProgressBar(Graphics g){
        
        
        if (((ZombieCrushSagaMiniGame)game).isCurrentScreenState(GAMEPLAY_SCREEN_STATE)
              && data.inProgress())
        {
           
            int score = data.getScore();
            
            // FIRST THE ACTUAL BAR
            //percentage (1.00 = 100%) = score / 3 star goal
            float barPercentage = (float) score / (float) data.getStarGoal(2);
            
                //limit it to 1.
            if(barPercentage >= 1)
                barPercentage = 1;
            
            float barHeight =  barPercentage * (PROGRESS_BAR_CORNERS.top - PROGRESS_BAR_CORNERS.bottom);
            float barWidth = PROGRESS_BAR_CORNERS.left - PROGRESS_BAR_CORNERS.right;
            
            g.setColor(PROGRESS_BAR_COLOR);
            g.fillRect(PROGRESS_METER_X, PROGRESS_METER_Y, (int) barWidth, (int) -barHeight );
            
            // AND THEN THE TEXT ON THE PROGRESS BAR
            //String progressText = data.getCurrentSun() + "/" + COST_OF_TROPHY + " Sun";
            g.setFont(PROGRESS_METER_FONT);
            g.setColor(PROGRESS_METER_TEXT_COLOR);
            
            //display the correct amount of current stars.
            String stars = "";
            if(score >= data.getStarGoal(2))
                stars = "***";
            else if(score >= data.getStarGoal(1))
                stars = "**";
            else if(score >= data.getStarGoal(0))
                stars = "*";
            g.drawString(stars, PROGRESS_METER_TEXT_X, PROGRESS_METER_TEXT_Y);
            
            g.setFont(PROGRESS_METER_STAR_LINE_FONT);
            //draw a line indicating percentage point for star 1.
            float star1Percentage = (float) data.getStarGoal(0) / (float) data.getStarGoal(2);
            float star1y = star1Percentage * (PROGRESS_BAR_CORNERS.top - PROGRESS_BAR_CORNERS.bottom);
            g.setColor(PROGRESS_METER_STAR_LINE_COLOR);
            g.drawLine(PROGRESS_METER_STAR_LINE_X1
                    ,PROGRESS_METER_Y - (int)star1y
                    ,PROGRESS_METER_STAR_LINE_X2
                    ,PROGRESS_METER_Y - (int)star1y);
             g.drawString("* " + data.getStarGoal(0), PROGRESS_METER_STAR_LINE_X2, PROGRESS_METER_Y - (int)star1y + 9);
            
              //draw a line indicating percentage point for star 2.
            float star2Percentage = (float) data.getStarGoal(1) / (float) data.getStarGoal(2);
            float star2y = star2Percentage * (PROGRESS_BAR_CORNERS.top - PROGRESS_BAR_CORNERS.bottom);
            g.setColor(PROGRESS_METER_STAR_LINE_COLOR);
            g.drawLine(PROGRESS_METER_STAR_LINE_X1
                    ,PROGRESS_METER_Y - (int)star2y
                    ,PROGRESS_METER_STAR_LINE_X2
                    ,PROGRESS_METER_Y - (int)star2y);
             g.drawString("** " + data.getStarGoal(1), PROGRESS_METER_STAR_LINE_X2, PROGRESS_METER_Y - (int)star2y + 9);
            
        }
    }
    
    /**
     * This method renders the on-screen stats that change as
     * the game progresses. This means things like the moves remaining
     * and the score
     *
     * @param g the Graphics context for this panel
     */
    public void renderStats(Graphics g)
    {
        // RENDER THE stats in game. moves and score
        if (((ZombieCrushSagaMiniGame)game).isCurrentScreenState(GAMEPLAY_SCREEN_STATE)
                && data.inProgress())
        {
            
             // RENDER THE score
            String gameplayScore = "" + data.getScore();
            int gameplayScore_x = GAMEPLAY_SCORE_X;
            int gameplayScore_y = GAMEPLAY_SCORE_Y;
            
            g.setFont(GAMEPLAY_SCORE_DISPLAY_FONT);
            g.setColor(Color.WHITE);
            g.drawString(gameplayScore, gameplayScore_x, gameplayScore_y);
            
            // RENDER THE moves
            String gameplayMoves = "" + data.getMoves();
            int gameplayMoves_x = GAMEPLAY_MOVES_X;
            int gameplayMoves_y = GAMEPLAY_MOVES_Y;
            
            g.setFont(GAMEPLAY_MOVES_DISPLAY_FONT);
            g.setColor(Color.WHITE);
            g.drawString(gameplayMoves, gameplayMoves_x, gameplayMoves_y);
            
        }
    }
    
    /**
     * renders the big box of stats when you win or lose
     * 
     * 
     * @param g 
     */
    public void renderStatsLevel(Graphics g)
    {
        
        // RENDER THE Stats only if the stats window is open.
        if (((ZombieCrushSagaMiniGame)game).isCurrentScreenState(GAMEPLAY_SCREEN_STATE)
                && game.getGUIDialogs().get(STATS_DIALOG_TYPE).getState().equals(VISIBLE_STATE))
        {
            //render the level name and then games, wins, losses, win%, fastest win
            ZombieCrushSagaRecord record = ((ZombieCrushSagaMiniGame)game).getPlayerRecord();
            
            String currentLevel = data.getCurrentLevel();
            
            int stats_x = STATS_X;
            int stats_y = STATS_Y;
            g.setFont(STATS_FONT);
            //if you win, display victory, if you lose, display loss.
            if(data.won()){
                g.drawString("VICTORY", stats_x, stats_y);
            }
            else{
                g.drawString("DEFEAT", stats_x, stats_y);
            }
            
            String theLevel = currentLevel;
            //parse up until the last slash.
            while (theLevel.indexOf('/') != -1)
                theLevel = theLevel.substring(theLevel.indexOf('/') + 1);
            
            //remove the .extension
            theLevel = theLevel.substring(0, theLevel.indexOf('.'));
            //remove all the dashes
            theLevel = theLevel.replace('_', ' ');
            //upper case everything
            theLevel = theLevel.toUpperCase();
                   
            stats_x = STATS_X ;
            stats_y = STATS_Y + STATS_TEXT_OFFSET;
            g.drawString(theLevel, stats_x, stats_y);
            
            //display score
            stats_x = STATS_X ;
            stats_y += STATS_TEXT_OFFSET;
            int score = data.getScore();
            g.drawString("Score: " + score, stats_x, stats_y);
            
            //display high score
            stats_x = STATS_X ;
            stats_y += STATS_TEXT_OFFSET;
            int highScore = record.getHighScore(data.getCurrentLevel());
            g.drawString("High Score: " + highScore, stats_x, stats_y);
            
        }
    }
    
    public void renderLevelScore(Graphics g)
    {
        
        // RENDER THE Stats TIME only if the stats window is open.
        if (((ZombieCrushSagaMiniGame)game).isCurrentScreenState(LEVEL_SCORE_SCREEN_STATE))
        {
            //render the level name and then games, wins, losses, win%, fastest win
            ZombieCrushSagaRecord record = ((ZombieCrushSagaMiniGame)game).getPlayerRecord();
            
            //cut off the dataFile
            String currentLevel = "Level " + data.getCurrentLevel().substring(data.getCurrentLevel().indexOf('_') + 1);
            //cut off the extension.
            currentLevel = currentLevel.substring(0, currentLevel.indexOf('.'));
            
            int currentHighScore = record.getHighScore(data.getCurrentLevel());
            
            String statsTarget;
            if(currentHighScore < data.getStarGoal(0))
                statsTarget = "" + data.getStarGoal(0);
            else if(currentHighScore < data.getStarGoal(1))
                statsTarget = "" + data.getStarGoal(1);
            else if (currentHighScore < data.getStarGoal(2))
                statsTarget = "" + data.getStarGoal(2);
            else
                statsTarget = "" + currentHighScore;
            
            String statsHighScore = "" + currentHighScore;
            String statsObjective = "";
            
            int levelType = data.getLevelType();
            
            if(currentHighScore < data.getStarGoal(0))
                statsObjective = "Get 1 stars!";
            else if(currentHighScore < data.getStarGoal(1))
                statsObjective = "Get 2 stars!";
            else if(currentHighScore < data.getStarGoal(2))
                statsObjective = "Get 3 stars!";
            else
                statsObjective = "Beat your high score!";
            
            g.setFont(LEVEL_SCORE_LEVEL_NAME_FONT);
            g.setColor(LEVEL_SCORE_TEXT_COLOR);
            g.drawString(currentLevel, LEVEL_SCORE_NAME_X, LEVEL_SCORE_NAME_Y);
            
            g.setFont(LEVEL_SCORE_SCORE_TARGET_FONT);
            g.setColor(LEVEL_SCORE_TEXT_COLOR);
            g.drawString(statsTarget, LEVEL_SCORE_X, LEVEL_SCORE_Y);
            
            g.setFont(LEVEL_SCORE_SCORE_HIGH_FONT);
            g.setColor(LEVEL_SCORE_TEXT_COLOR);
            g.drawString(statsHighScore, LEVEL_SCORE_X, LEVEL_SCORE_Y + LEVEL_SCORE_OFFSET);
            
            g.setFont(LEVEL_SCORE_SCORE_OBJECTIVE_FONT);
            g.setColor(LEVEL_SCORE_TEXT_COLOR);
            g.drawString(statsObjective, LEVEL_SCORE_OBJECTIVE_X, LEVEL_SCORE_OBJECTIVE_Y );
            
            //if levelType == jelly, add another objective.
            if(levelType != 0){
                g.setFont(LEVEL_SCORE_SCORE_OBJECTIVE_FONT);
                g.setColor(LEVEL_SCORE_TEXT_COLOR);
                g.drawString("Clear all jelly!", LEVEL_SCORE_OBJECTIVE_X,
                        LEVEL_SCORE_OBJECTIVE_Y + LEVEL_SCORE_SCORE_OBJECTIVE_FONT.getSize() );
                
            }
            
        }
    }
    
    /**
     * Renders all the game tiles, doing so carefully such
     * that they are rendered in the proper order.
     *
     * @param g the Graphics context of this panel.
     */
    public void renderZombies(Graphics g)
    {
        // DRAW THE TOP TILES ON THE STACK
        if (!data.won())
        {
            ArrayList<ZombieCrushSagaZombie> stackZombies = data.getStackZombies();
                    
            for (ZombieCrushSagaZombie zombie : stackZombies)
            {
                 renderZombie(g, zombie);
            }
            
        }
        
        // THEN DRAW ALL THE MOVING TILES
        Iterator<ZombieCrushSagaZombie> movingTiles = data.getMovingZombies();
        while (movingTiles.hasNext())
        {
            ZombieCrushSagaZombie tile = movingTiles.next();
            renderZombie(g, tile);
        }
    }
    
    /**
     * Helper method for rendering the tiles that are currently moving.
     *
     * @param g Rendering context for this panel.
     *
     * @param zombieToRender Tile to render to this panel.
     */
    public void renderZombie(Graphics g, ZombieCrushSagaZombie zombieToRender)
    {
        // ONLY RENDER VISIBLE TILES
        if (!zombieToRender.getState().equals(INVISIBLE_STATE))
        {
            // First THE TILE IMAGE
            SpriteType bgST = zombieToRender.getSpriteType();
            Image img = bgST.getStateImage(zombieToRender.getState());
            g.drawImage(img, (int)zombieToRender.getX(), (int)zombieToRender.getY(), bgST.getWidth(), bgST.getHeight(), null);
            
            // then DRAW THE BLANK TILE or selected IMAGE
            if (zombieToRender.isSelected()) //if selected
                g.drawImage(blankZombieSelectedImage, (int)zombieToRender.getX(), (int)zombieToRender.getY(), null);
            else
                g.drawImage(blankZombieImage, (int)zombieToRender.getX(), (int)zombieToRender.getY(), null);;
            
        }
    }
    
    /**
     * Renders the game dialog boxes.
     *
     * @param g This panel's graphics context.
     */
    public void renderDialogs(Graphics g)
    {
        // GET EACH DECOR IMAGE ONE AT A TIME
        Collection<Sprite> dialogSprites = game.getGUIDialogs().values();
        for (Sprite s : dialogSprites)
        {
            // RENDER THE DIALOG, NOTE IT WILL ONLY DO IT IF IT'S VISIBLE
            renderSprite(g, s);
        }
    }
    
    /**
     * Renders the s Sprite into the Graphics context g. Note
     * that each Sprite knows its own x,y coordinate location.
     *
     * @param g the Graphics context of this panel
     *
     * @param s the Sprite to be rendered
     */
    public void renderSprite(Graphics g, Sprite s)
    {
        // ONLY RENDER THE VISIBLE ONES
        if (!s.getState().equals(INVISIBLE_STATE))
        {
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            g.drawImage(img, (int)s.getX(), (int)s.getY(), bgST.getWidth(), bgST.getHeight(), null);
        }
    }
    
    public void renderZombieScore(Graphics g){
        
        // RENDER THE Stats TIME only if the stats window is open.
        if (((ZombieCrushSagaMiniGame)game).isCurrentScreenState(GAMEPLAY_SCREEN_STATE)
                && data.getRenderScore())
        {
            //render all of the scores.
            int[][] boardScore = data.getBoardScore();
            
            g.setFont(GAMEPLAY_SCORE_FONT);
            g.setColor(Color.white);    //possible variable color.
            
            for(int row = 0; row < BOARD_ROWS; row++){
                for(int column = 0; column < BOARD_COLUMNS; column++){
                    if(boardScore[row][column] > 0){
                        g.drawString("" + boardScore[row][column],
                                data.calculateZombieXInGrid(column) + 10 ,
                                data.calculateZombieYInGrid(row) + 30) ;
                    }
                }
            }
        }
    }
    
    /**
     * This method renders grid lines in the game tile grid to help
     * during debugging.
     *
     * @param g Graphics context for this panel.
     */
    public void renderGrid(Graphics g)
    {
        // ONLY RENDER THE GRID IF WE'RE DEBUGGING
        if (data.isDebugTextRenderingActive())
        {
            for (int i = 0; i < data.getGridColumns(); i++)
            {
                for (int j = 0; j < data.getGridRows(); j++)
                {
                    int x = data.calculateZombieXInGrid(i);
                    int y = data.calculateZombieYInGrid(j);
                    g.drawRect(x, y, ZOMBIE_IMAGE_WIDTH, ZOMBIE_IMAGE_HEIGHT);
                }
            }
        }
    }
    
    /**
     * Renders the debugging text to the panel. Note
     * that the rendering will only actually be done
     * if data has activated debug text rendering.
     *
     * @param g the Graphics context for this panel
     */
    public void renderDebuggingText(Graphics g)
    {
        // IF IT'S ACTIVATED
        if (data.isDebugTextRenderingActive())
        {
            // ENABLE PROPER RENDER SETTINGS
            g.setFont(DEBUG_TEXT_FONT);
            g.setColor(DEBUG_TEXT_COLOR);
            
            // GO THROUGH ALL THE DEBUG TEXT
            Iterator<String> it = data.getDebugText().iterator();
            int x = data.getDebugTextX();
            int y = data.getDebugTextY();
            while (it.hasNext())
            {
                // RENDER THE TEXT
                String text = it.next();
                g.drawString(text, x, y);
                y += 20;
            }
        }
    }
}