package zombie_crush_saga;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

/**
 * This class stores the types of controls and their possible states which
 * we'll use to control the flow of the application. Note that these control
 * types and states are NOT flavor-specific.
 * 
 * @author Richard McKenna
 */
public class ZombieCrushSagaConstants
{
    // WE ONLY HAVE A LIMITIED NUMBER OF UI COMPONENT TYPES IN THIS APP
    
    //DO NOT CHANGE. No future change predicted.
    public static final int BOARD_COLUMNS = 9;
    public static final int BOARD_ROWS = 9;
    
    // Zombie Sprite Types
    public static final String ZOMBIE_TYPE = "ZOMBIE_TYPE";
    public static final String ZOMBIE_SPRITE_TYPE_PREFIX = "ZOMBIE_";
    public static final String ZOMBIE_BLUE_TYPE = "ZOMBIE_BLUE_TYPE";
    public static final String ZOMBIE_GREEN_TYPE = "ZOMBIE_GREEN_TYPE";
    public static final String ZOMBIE_ORANGE_TYPE = "ZOMBIE_ORANGE_TYPE";
    public static final String ZOMBIE_PURPLE_TYPE = "ZOMBIE_PURPLE_TYPE";
    public static final String ZOMBIE_RED_TYPE = "ZOMBIE_RED_TYPE";
    public static final String ZOMBIE_YELLOW_TYPE = "ZOMBIE_YELLOW_TYPE";
    
    // EACH SCREEN HAS ITS OWN BACKGROUND TYPE
    public static final String BACKGROUND_TYPE = "BACKGROUND_TYPE";
    
    // THIS REPRESENTS THE BUTTONS ON THE SPLASH SCREEN FOR LEVEL SELECTION
    public static final String SPLASH_BUTTON_TYPE = "SPLASH_BUTTON_TYPE";
    
    public static final String LEVEL_SELECT_BUTTON_TYPE = "LEVEL_SELECT_BUTTON_TYPE";

    //IN-GAME UI CONTROL TYPES
        //Splash Screen buttons
    public static final String SPLASH_PLAY_BUTTON_TYPE = "SPLASH_PLAY_BUTTON_TYPE";
    public static final String SPLASH_RESET_BUTTON_TYPE = "SPLASH_RESET_BUTTON_TYPE";
    public static final String SPLASH_QUIT_BUTTON_TYPE = "SPLASH_QUIT_BUTTON_TYPE"; 
    
        //Saga screen buttons
    public static final String SAGA_QUIT_BUTTON_TYPE = "SAGA_QUIT_BUTTON_TYPE";
    public static final String SAGA_SCROLL_UP_BUTTON_TYPE = "SAGA_SCROLL_UP_BUTTON_TYPE";
    public static final String SAGA_SCROLL_DOWN_BUTTON_TYPE = "SAGA_SCROLL_DOWN_BUTTON_TYPE";
        //Saga level buttons
    
    public static final String SAGA_LEVEL_SWAP_BUTTON_TYPE = "SAGA_LEVEL_SWAP_BUTTON_TYPE";
    public static final String SAGA_LEVEL_JELLY_BUTTON_TYPE = "SAGA_LEVEL_JELLY_BUTTON_TYPE";
    
        //Level Score Button
    public static final String LEVEL_SCORE_QUIT_BUTTON_TYPE = "LEVEL_SCORE_QUIT_BUTTON_TYPE";
    public static final String LEVEL_SCORE_PLAY_BUTTON_TYPE = "LEVEL_SCORE_PLAY_BUTTON_TYPE";
    
        //Gameplay Buttons
    public static final String GAMEPLAY_SMASHER_BUTTON_TYPE = "GAMEPLAY_SMASHER_BUTTON_TYPE";
    public static final String GAMEPLAY_QUIT_BUTTON_TYPE = "GAMEPLAY_QUIT_BUTTON_TYPE";
    public static final String GAMEPLAY_REPLAY_BUTTON_TYPE = "GAMEPLAY_REPLAY_BUTTON_TYPE";
    
    //game elements
    
        //level score decor
     public static final String LEVEL_SCORE_STAR_TYPE = "LEVEL_SCORE_STAR_TYPE";
    
        //gameplay decor
    public static final String BOARD_SPACE_TYPE = "BOARD_SPACE_TYPE";
    public static final String BOARD_JELLY_TYPE = "BOARD_JELLY_TYPE";
    
    
    //BELOW THIS YEE BE WARNED
    public static final String BACK_BUTTON_TYPE = "BACK_BUTTON_TYPE"; 
    public static final String TILES_COUNT_TYPE = "TILES_COUNT_TYPE"; 
    public static final String TIME_TYPE = "TIME_TYPE"; 
    public static final String STATS_BUTTON_TYPE = "STATS_BUTTON_TYPE";
    public static final String UNDO_BUTTON_TYPE = "UNDO_BUTTON_TYPE";
    public static final String TILE_STACK_TYPE = "TILE_STACK_TYPE";
    
    // DIALOG TYPES
    public static final String STATS_DIALOG_TYPE = "STATS_DIALOG_TYPE";
    public static final String WIN_DIALOG_TYPE = "WIN_DIALOG_TYPE";
    public static final String LOSS_DIALOG_TYPE = "LOSS_DIALOG_TYPE";
    
    // WE'LL USE THESE STATES TO CONTROL SWITCHING BETWEEN THE FIVE
    public static final String SPLASH_SCREEN_STATE = "SPLASH_SCREEN_STATE";
    public static final String SAGA_SCREEN_STATE = "SAGA_SCREEN_STATE";  
    public static final String LEVEL_SCORE_SCREEN_STATE = "LEVEL_SCORE_SCREEN_STATE";
    public static final String GAMEPLAY_SCREEN_STATE = "GAMEPLAY_SCREEN_STATE";    
        
    public static final String ABOUT_SCREEN_STATE = "ABOUT_SCREEN_STATE";    
    
    // THE TILES MAY HAVE 4 STATES:
        // - INVISIBLE_STATE: USED WHEN ON THE SPLASH SCREEN, MEANS A TILE
            // IS NOT DRAWN AND CANNOT BE CLICKED
        // - VISIBLE_STATE: USED WHEN ON THE GAME SCREEN, MEANS A TILE
            // IS VISIBLE AND CAN BE CLICKED (TO SELECT IT), BUT IS NOT CURRENTLY SELECTED
        // - SELECTED_STATE: USED WHEN ON THE GAME SCREEN, MEANS A TILE
            // IS VISIBLE AND CAN BE CLICKED (TO UNSELECT IT), AND IS CURRENTLY SELECTED     
        // - NOT_AVAILABLE_STATE: USED FOR A TILE THE USER HAS CLICKED ON THAT
            // IS NOT FREE. THIS LET'S US GIVE THE USER SOME FEEDBACK
    public static final String INVISIBLE_STATE = "INVISIBLE_STATE";
    public static final String VISIBLE_STATE = "VISIBLE_STATE";
    public static final String SELECTED_STATE = "SELECTED_STATE";
    public static final String MOUSE_OVER_STATE = "MOUSE_OVER_STATE";
    public static final String ANIMATED_STATE_PREFIX = "ANIMATED_STATE_";
    
    //Zombies have several states
    public static final String ZOMBIE_BLUE_STATE = "ZOMBIE_BLUE_STATE";
    public static final String ZOMBIE_GREEN_STATE = "ZOMBIE_GREEN_STATE";
    public static final String ZOMBIE_ORANGE_STATE = "ZOMBIE_ORANGE_STATE";
    public static final String ZOMBIE_PURPLE_STATE = "ZOMBIE_PURPLE_STATE";
    public static final String ZOMBIE_RED_STATE = "ZOMBIE_RED_STATE";
    public static final String ZOMBIE_YELLOW_STATE = "ZOMBIE_YELLOW_STATE";
    
    public static final String ZOMBIE_BLUE_STRIPED_HORIZONTAL_STATE = "ZOMBIE_BLUE_STRIPED_HORIZONTAL_STATE";
    public static final String ZOMBIE_GREEN_STRIPED_HORIZONTAL_STATE = "ZOMBIE_GREEN_STRIPED_HORIZONTAL_STATE";
    public static final String ZOMBIE_ORANGE_STRIPED_HORIZONTAL_STATE = "ZOMBIE_ORANGE_STRIPED_HORIZONTAL_STATE";
    public static final String ZOMBIE_PURPLE_STRIPED_HORIZONTAL_STATE = "ZOMBIE_PURPLE_STRIPED_HORIZONTAL_STATE";
    public static final String ZOMBIE_RED_STRIPED_HORIZONTAL_STATE = "ZOMBIE_RED_STRIPED_HORIZONTAL_STATE";
    public static final String ZOMBIE_YELLOW_STRIPED_HORIZONTAL_STATE = "ZOMBIE_YELLOW_STRIPED_HORIZONTAL_STATE";
    
    public static final String ZOMBIE_BLUE_STRIPED_VERTICAL_STATE = "ZOMBIE_BLUE_STRIPED_VERTICAL_STATE";
    public static final String ZOMBIE_GREEN_STRIPED_VERTICAL_STATE = "ZOMBIE_GREEN_STRIPED_VERTICAL_STATE";
    public static final String ZOMBIE_ORANGE_STRIPED_VERTICAL_STATE = "ZOMBIE_ORANGE_STRIPED_VERTICAL_STATE";
    public static final String ZOMBIE_PURPLE_STRIPED_VERTICAL_STATE = "ZOMBIE_PURPLE_STRIPED_VERTICAL_STATE";
    public static final String ZOMBIE_RED_STRIPED_VERTICAL_STATE = "ZOMBIE_RED_STRIPED_VERTICAL_STATE";
    public static final String ZOMBIE_YELLOW_STRIPED_VERTICAL_STATE = "ZOMBIE_YELLOW_STRIPED_VERTICAL_STATE";
    
    public static final String ZOMBIE_BLUE_WRAPPED_STATE = "ZOMBIE_BLUE_WRAPPED_STATE";
    public static final String ZOMBIE_GREEN_WRAPPED_STATE = "ZOMBIE_GREEN_WRAPPED_STATE";
    public static final String ZOMBIE_ORANGE_WRAPPED_STATE = "ZOMBIE_ORANGE_WRAPPED_STATE";
    public static final String ZOMBIE_PURPLE_WRAPPED_STATE = "ZOMBIE_PURPLE_WRAPPED_STATE";
    public static final String ZOMBIE_RED_WRAPPED_STATE = "ZOMBIE_RED_WRAPPED_STATE";
    public static final String ZOMBIE_YELLOW_WRAPPED_STATE = "ZOMBIE_YELLOW_WRAPPED_STATE";
    
    public static final String ZOMBIE_FIVE_STATE = "ZOMBIE_FIVE_STATE";
    
    // THE BUTTONS MAY HAVE 2 STATES:
        // - INVISIBLE_STATE: MEANS A BUTTON IS NOT DRAWN AND CAN'T BE CLICKED
        // - VISIBLE_STATE: MEANS A BUTTON IS DRAWN AND CAN BE CLICKED
        // - MOUSE_OVER_STATE: MEANS A BUTTON IS DRAWN WITH SOME HIGHLIGHTING
            // BECAUSE THE MOUSE IS HOVERING OVER THE BUTTON

    // UI CONTROL SIZE AND POSITION SETTINGS
    
        //POSITIONING THE SPLASH SCREEN BUTTONS
    public static final int SPLASH_BUTTON_WIDTH = 200;
    public static final int SPLASH_BUTTON_MARGIN = 5;
    public static final int SPLASH_BUTTON_Y = 600;
    
        //SAGA Screen Button positioning
    public static final int SAGA_QUIT_BUTTON_X = 600;
    public static final int SAGA_QUIT_BUTTON_Y = 0;
    public static final int SAGA_QUIT_BUTTON_HEIGHT = 50;
    public static final int SAGA_SCROLL_UP_BUTTON_X = 600;
    public static final int SAGA_SCROLL_UP_BUTTON_Y = SAGA_QUIT_BUTTON_HEIGHT;
    public static final int SAGA_SCROLL_UP_BUTTON_HEIGHT = 50;
    public static final int SAGA_SCROLL_DOWN_BUTTON_X = 600;
    public static final int SAGA_SCROLL_DOWN_BUTTON_Y = SAGA_SCROLL_UP_BUTTON_Y + SAGA_SCROLL_UP_BUTTON_HEIGHT;
    public static final int SAGA_SCROLL_DOWN_BUTTON_HEIGHT = 50;
    public static final float SAGA_SCROLL_AMOUNT = 300;
    public static final float SAGA_SCROLL_SPEED = SAGA_SCROLL_AMOUNT/5;
    
    public static final int SAGA_LEVEL_ONE_BUTTON_X = 69;
    public static final float SAGA_LEVEL_ONE_BUTTON_Y = 645;
    public static final float SAGA_LEVEL_BUTTON_X_DISTANCE = 118;
    public static final float SAGA_LEVEL_BUTTON_Y_DISTANCE = 241;
    
        //Level_Score button positioning
    public static final float LEVEL_SCORE_QUIT_BUTTON_X = 600;
    public static final float LEVEL_SCORE_QUIT_BUTTON_Y = 0;
    public static final float LEVEL_SCORE_PLAY_BUTTON_X = 225;
    public static final float LEVEL_SCORE_PLAY_BUTTON_Y = 615;
    
    //gameplay  button positioning
    public static final float GAMEPLAY_BUTTON_SIZE_Y = 50;
    public static final float GAMEPLAY_QUIT_BUTTON_X = 600;
    public static final float GAMEPLAY_QUIT_BUTTON_Y = 0;
    public static final float GAMEPLAY_REPLAY_BUTTON_X = 225;
    public static final float GAMEPLAY_REPLAY_BUTTON_Y = 615;
    public static final float GAMEPLAY_SMASHER_BUTTON_X = GAMEPLAY_QUIT_BUTTON_X;
    public static final float GAMEPLAY_SMASHER_BUTTON_Y =
            GAMEPLAY_QUIT_BUTTON_Y + GAMEPLAY_BUTTON_SIZE_Y;
    
    //level score decor positioning
    public static final float LEVEL_SCORE_FIRST_STAR_X = 288;
    public static final float LEVEL_SCORE_FIRST_STAR_Y = 91;
    public static final float LEVEL_SCORE_STAR_OFFSET_X = 79;
    
    
        //top left corner
    public static final float BOARD_FIRST_SPACE_X = 130;
    public static final float BOARD_FIRST_SPACE_Y = 100;
    public static final float BOARD_SPACE_SIZE = 57; //a space is a square
    public static final float BOARD_SPACE_AMOUNTS = 81;
    
    public static final float CANDY_FIRST_SPACE_X = BOARD_FIRST_SPACE_X;
    public static final float CANDY_FIRST_SPACE_Y = BOARD_FIRST_SPACE_Y - BOARD_SPACE_SIZE;
    
    // OR POSITIONING THE LEVEL SELECT BUTTONS
    public static final int LEVEL_BUTTON_WIDTH = 200;
    public static final int LEVEL_BUTTON_MARGIN = 5;
    public static final int LEVEL_BUTTON_Y = 570;

    // FOR STACKING TILES ON THE GRID
    public static final int NUM_ZOMBIES = 81;
    //public static final int ZOMBIE_IMAGE_OFFSET = 1;
    public static final int ZOMBIE_IMAGE_WIDTH = 57;
    public static final int ZOMBIE_IMAGE_HEIGHT = 57;
    public static final int Z_TILE_OFFSET = 5;           //remove later?

    // FOR MOVING TILES AROUND, 7 is ideal. 10 is a bit fast. 5 is a bit slow
    public static final int MAX_ZOMBIE_VELOCITY = 7;
    
    //display the big box of statslos
    public static final int STATS_X = 155;
    public static final int STATS_Y = 320;
    public static final int STATS_OFFSET_X = 20;
    public static final int STATS_TEXT_OFFSET = 25;
    
    //Level Score Text Location
    public static final int LEVEL_SCORE_NAME_X = 200;
    public static final int LEVEL_SCORE_NAME_Y = 70;
    public static final int LEVEL_SCORE_X = 300;
    public static final int LEVEL_SCORE_Y = 230;
    public static final int LEVEL_SCORE_OFFSET = 80;
    public static final int LEVEL_SCORE_OBJECTIVE_X = 250;
    public static final int LEVEL_SCORE_OBJECTIVE_Y = 350;
    
    public static final int GAMEPLAY_SCORE_X = 20;
    public static final int GAMEPLAY_SCORE_Y = 100;
    
    public static final int GAMEPLAY_MOVES_X = 20;
    public static final int GAMEPLAY_MOVES_Y = 50;
    
    //may not be needed
    // THESE ARE USED FOR FORMATTING THE TIME OF GAME
    public static final long MILLIS_IN_A_SECOND = 1000;
    public static final long MILLIS_IN_A_MINUTE = 1000 * 60;
    public static final long MILLIS_IN_AN_HOUR  = 1000 * 60 * 60;

    // USED FOR DOING OUR VICTORY ANIMATION
    public static final int WIN_PATH_NODES = 360;   //360 points in a circle.
    public static final int WIN_PATH_TOLERANCE = 100;
    public static final int WIN_PATH_COORD = 100;
    public static final int WIN_PATH_RADIUS = 300;  //for the circle.

    // COLORS USED FOR RENDERING VARIOUS THINGS, INCLUDING THE
    // COLOR KEY, WHICH REFERS TO THE COLOR TO IGNORE WHEN
    // LOADING ART.
    public static final Color COLOR_KEY = new Color(255, 174, 201);
    public static final Color DEBUG_TEXT_COLOR = Color.BLACK;
    public static final Color TEXT_DISPLAY_COLOR = new Color (10, 160, 10);
    //public static final Color SELECTED_TILE_COLOR = new Color(255,255,0,100);         //highlights the tile, ugly yellow
    public static final Color INCORRECTLY_SELECTED_TILE_COLOR = new Color(255, 50, 50, 100);
    public static final Color STATS_COLOR = new Color(0, 60, 0);

    // FONTS USED DURING FOR TEXTUAL GAME DISPLAYS
    public static final Font TEXT_DISPLAY_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 48);
    public static final Font DEBUG_TEXT_FONT = new Font(Font.MONOSPACED, Font.BOLD, 14);
    public static final Font STATS_FONT = new Font(Font.MONOSPACED, Font.BOLD, 24);
    public static final Font GAMEPLAY_SCORE_DISPLAY_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 12);
    public static final Font GAMEPLAY_MOVES_DISPLAY_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 12);
    
    public static final Font GAMEPLAY_SCORE_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 20);
    
    public static final Font LEVEL_SCORE_LEVEL_NAME_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 50);
    public static final Font LEVEL_SCORE_SCORE_TARGET_FONT = new Font(Font.MONOSPACED, Font.BOLD, 30);
    public static final Font LEVEL_SCORE_SCORE_HIGH_FONT = new Font(Font.MONOSPACED, Font.BOLD, 20);
    public static final Font LEVEL_SCORE_SCORE_OBJECTIVE_FONT = new Font(Font.MONOSPACED, Font.BOLD, 20);
    
    public static final Color LEVEL_SCORE_TEXT_COLOR = Color.WHITE;
    
    // AND HERE ARE ALL OUR GUI SETTINGS.
    public static final Font PROGRESS_METER_FONT = new Font("Serif", Font.BOLD, 22);
    public static final Color PROGRESS_METER_TEXT_COLOR = Color.YELLOW;
    public static final int PROGRESS_METER_TEXT_X = 40;
    public static final int PROGRESS_METER_TEXT_Y = 149;
    public static final int PROGRESS_METER_X = 32;
    public static final int PROGRESS_METER_Y = 353;
    public static final Insets PROGRESS_BAR_CORNERS = new Insets(201, 0, 0, 13);//top, left, bottom, right
    public static final Color PROGRESS_BAR_COLOR = Color.GREEN;
    
    public static final int PROGRESS_METER_STAR_LINE_X1 = 10;
    public static final int PROGRESS_METER_STAR_LINE_X2 = 55;
    public static final Color PROGRESS_METER_STAR_LINE_COLOR = Color.WHITE;
    public static final Font PROGRESS_METER_STAR_LINE_FONT = new Font("Serif", Font.BOLD, 16);
    
    // AND AUDIO STUFF
    public static final String SUCCESS_AUDIO_TYPE = "SUCCESS_AUDIO_TYPE";
    public static final String FAILURE_AUDIO_TYPE = "FAILURE_AUDIO_TYPE";
    public static final String THEME_SONG_TYPE = "THEME_SONG_TYPE";
}