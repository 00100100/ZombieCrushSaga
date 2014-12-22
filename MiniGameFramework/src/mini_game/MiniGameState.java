package mini_game;
/**
 * This enum stores the only four states used
 * by our Game application.
 * 
 * NOT_STARTED refers to when the application has
 * begun, but the user has not yet chosen to play
 * a game yet.
 * 
 * IN_PROGRESS is when the game is underway and 
 * game logic is executed each frame
 * 
 * LOSS is when a game has been completed and the
 * player lost.
 * 
 * WIN is when a game has been completed and the
 * player won.
 * 
 * @author Richard McKenna
 * @version 1.0
 */
enum MiniGameState{NOT_STARTED, IN_PROGRESS, LOSS, WIN};
