package zombie_crush_saga.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import zombie_crush_saga.ui.ZombieCrushSagaMiniGame;
import static zombie_crush_saga.ZombieCrushSagaConstants.*;

/**
 * This class manages when the user clicks the saga's scroll up/down button
 * 
 * @author Luigi Keh & Richard Mckenna
 */
public class SagaScrollHandler implements ActionListener
{
    private ZombieCrushSagaMiniGame miniGame;
    String scrollDirection;
    
    public SagaScrollHandler(ZombieCrushSagaMiniGame initMiniGame, String initScrollDirection)
    {
        miniGame = initMiniGame;
        scrollDirection = initScrollDirection;
    }
    
    /**
     * This method is called when the user clicks a saga scroll button.
     * 
     * @param ae the event object for the button press
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        // WE ONLY LET THIS HAPPEN IF THE SAGA SCREEN IS VISIBLE
        if (miniGame.isCurrentScreenState(SAGA_SCREEN_STATE))
        {
                miniGame.scrollSagaBackground(scrollDirection);
        }
    }
}