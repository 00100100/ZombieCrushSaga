package zombie_crush_saga.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import zombie_crush_saga.ui.ZombieCrushSagaMiniGame;
import static zombie_crush_saga.ZombieCrushSagaConstants.*;

/**
 * This event handler responds to when the user selects
 * a ZombieCrushSaga level to play on the splash screen.
 * 
 * @author Richard McKenna
 */
public class LevelScorePlayLevelHandler implements ActionListener
{
    // HERE'S THE GAME WE'LL UPDATE
    private ZombieCrushSagaMiniGame miniGame;
    
    /**
     * This constructor just stores the game and the level to
     * load for later.
     *     
     * @param initGame The game to update.
     * 
     * @param initLevelFile The level to load when the user requests it. 
     */
    public LevelScorePlayLevelHandler(  ZombieCrushSagaMiniGame initGame,
                                String initLevelFile)
    {
        miniGame = initGame;
    }
    
    /**
     * Here is the event response. This code is executed when
     * the user clicks on a button for selecting a level
     * which is how the user starts a game. Note that the game 
     * data is already locked for this thread before it is called, 
     * and that it will be unlocked after it returns.
     * 
     * @param ae the event object for the button press
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        // WE ONLY LET THIS HAPPEN IF THE SPLASH SCREEN IS VISIBLE
        
        
        if (miniGame.isCurrentScreenState(LEVEL_SCORE_SCREEN_STATE) )
        {
            
            //no need to load the level, the levelselect button did that in order to
            //display past scores.
            
            // GO TO THE GAME
            miniGame.switchToGameplayScreen();
        }
    }
}