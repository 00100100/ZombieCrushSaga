package mahjong_solitaire.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import mahjong_solitaire.data.MahjongSolitaireDataModel;
import mahjong_solitaire.file.MahjongSolitaireFileManager;
import mahjong_solitaire.ui.MahjongSolitaireMiniGame;
import static mahjong_solitaire.MahjongSolitaireConstants.*;

/**
 * This event handler responds to when the user selects
 * a Mahjong level to play on the splash screen.
 * 
 * @author Richard McKenna
 */
public class SelectLevelHandler implements ActionListener
{
    // HERE'S THE GAME WE'LL UPDATE
    private MahjongSolitaireMiniGame game;
    
    // HERE'S THE LEVEL TO LOAD
    private String levelFile;
    
    /**
     * This constructor just stores the game and the level to
     * load for later.
     *     
     * @param initGame The game to update.
     * 
     * @param initLevelFile The level to load when the user requests it. 
     */
    public SelectLevelHandler(  MahjongSolitaireMiniGame initGame,
                                String initLevelFile)
    {
        game = initGame;
        levelFile = initLevelFile;
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
        if (game.isCurrentScreenState(SPLASH_SCREEN_STATE))
        {
            // GET THE GAME'S DATA MODEL, WHICH IS ALREADY LOCKED FOR US
            MahjongSolitaireDataModel data = (MahjongSolitaireDataModel)game.getDataModel();
        
            // UPDATE THE DATA
            MahjongSolitaireFileManager fileManager = game.getFileManager();
            fileManager.loadLevel(levelFile);

            // GO TO THE GAME
            game.switchToGameScreen();
        }
    }
}