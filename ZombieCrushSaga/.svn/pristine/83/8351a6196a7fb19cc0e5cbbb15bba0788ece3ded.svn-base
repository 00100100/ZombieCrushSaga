package mahjong_solitaire.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import mahjong_solitaire.ui.MahjongSolitaireMiniGame;

/**
 * This event handler responds to when the user clicks on the Stats
 * button, which triggers displaying the stats dialog.
 * 
 * @author Richard McKenna
 */
public class StatsHandler implements ActionListener
{
    // THE MAHJONG GAME CONTAINING THE UNDO BUTTON
    private MahjongSolitaireMiniGame miniGame;
    
    /**
     * This constructor simply inits the object by 
     * keeping the game for later.
     * 
     * @param initGame The Mahjong game that contains
     * the back button.
     */
    public StatsHandler(MahjongSolitaireMiniGame initMiniGame)
    {
        miniGame = initMiniGame;
    }

    /**
     * This method provides the response, which is simply to
     * display the game stats in a dialog. We let the game
     * take care of implementing how.
     * 
     * @param ae Event object.
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        // DISPLAY THE STATS
        miniGame.displayStats();
    }
}