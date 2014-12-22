package mini_game;

import audio_manager.AudioManager;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Timer;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The classes from the mini_game package, including this class, provide a
 * framework for making a Swing based game. This class serves as the focal point
 * of that game, with access to all the important game data and controls. Note
 * that we will use a Timer to provide fixed-rate updates of the game data and
 * rendering, so we will also use a ReentrantLock to make sure we don't change
 * data mid-render.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public abstract class MiniGame
{
    // THIS IS THE NAME OF THE CUSTOMIZED GAME
    protected String name;
    
    // data WILL MANAGE OUR GAME DATA, WHICH SHOULD
    // BE CUSTOMIZED FOR THE GIVEN GAME
    protected MiniGameDataModel data;
    
    // WE ARE GOING TO HAVE 2 THREADS AT WORK IN THIS APPLICATION,
    // THE MAIN GUI THREAD, WHICH WILL LISTEN FOR USER INTERACTION
    // AND CALL OUR EVENT HANDLERS, AND THE TIMER THREAD, WHICH ON
    // A FIXED SCHEDULE (e.g. 30 times/second) WILL UPDATE ALL THE
    // GAME DATA AND THEN RENDER. THIS LOCK WILL MAKE SURE THAT
    // ONE THREAD DOESN'T RUIN WHAT THE OTHER IS DOING (CALLED A
    // RACE CONDITION). FOR EXAMPLE, IT WOULD BE BAD IF WE STARTED
    // RENDERING THE GAME AND 1/2 WAY THROUGH, THE GAME DATA CHANGED.
    // THAT MAY CAUSE BIG PROBLEMS. EACH THREAD WILL NEED TO LOCK
    // THE DATA BEFORE EACH USE AND THEN UNLOCK WHEN DONE WITH IT.
    protected ReentrantLock dataLock;
    
    // EVERY GAME WILL HAVE A SINGLE CANVAS INSIDE
    // OUR WINDOW. WE CAN PAINT AND HANDLE EVENTS
    // FOR BUTTONS OURSELVES
    protected JFrame window;
    protected JPanel canvas;
    
    // HERE ARE OUR GUI COMPONENTS. NOTE WE ARE NOT
    // USING SWING COMPONENTS (except JFrame and JPanel),
    // WE ARE MANAGING THE BUTTONS AND OTHER DISPLAY
    // ITEMS OURSLEVES. TreeMap IS SIMPLY A BINARY SEARCH
    // TREE WHERE THE String PARAMETER SPECIFIES THE ID
    // USED FOR KEEPING THE CONTROLS IN SORTED ORDER.
    // ONE HAS TO UNIQUELY NAME EACH CONTROL THAT GOES
    // IN THESE DATA STRUCTURES
    protected TreeMap<String, Sprite> guiButtons;
    protected TreeMap<String, Sprite> guiDecor;
    protected TreeMap<String, Sprite> guiDialogs;
    
    // WE WILL HAVE A TIMER RUNNING IN ANOTHER THREAD THAT
    // EVERY frameDuration AMOUNT OF TIME WILL UPDATE THE
    // GAME AND THEN RENDER. frameDuration IS THE NUMBER
    // OF MILLISECONDS IT TAKES PER FRAME, SO WE CAN
    // CALCULATE IT BY DIVIDING 1000 (MILLISECONDS IN 1 SEC.)
    // BY THE FRAME RATE, i.e. framesPerSecond
    // NOTE THAT THE GameTimerTask WILL HAVE THE CUSTOM
    // BEHAVIOUR PERFORMED EACH FRAME BY THE GAME
    protected Timer gameTimer;
    protected MiniGameTimerTask gameTimerTask;
    protected int framesPerSecond;
    protected int frameDuration;
    
    // THESE VARIABLES STORE THE PIXEL DISTANCES FROM THE EDGES
    // OF THE CANVAS WHERE THE GAME WILL BE PLAYED. BY SETTING
    // THESE VALUES TO 0, THE FULL CANVAS WOULD BE THE PLAY AREA.
    // THESE ARE LEFT protected, SO A SUB CLASS, WHICH REPRESENTS
    // A CUSTOM GAME COULD CHANGE THESE AS THE DEVELOPER SEES FIT.
    protected float boundaryLeft = 0;
    protected float boundaryRight = 0;
    protected float boundaryTop = 0;
    protected float boundaryBottom = 0;
    // WE'LL MANAGE THE EVENTS IN A SLIGHTLY DIFFERENT WAY
    // THIS TIME SINCE WE ARE RENDERING ALL OUR OWN
    // BUTTONS AND BECAUSE WE HAVE TO WORRY ABOUT 2 THREADS.
    // THIS OBJECT WILL RELAY BOTH MOUSE INTERACTIONS AND
    // KEYBOARD INTERACTIONS TO THE CUSTOM HANDLER, MAKING
    // SURE TO AVOID RACE CONDITIONS
    protected MiniGameEventRelayer gamh;
    
    // THIS CLASS WILL PLAY OUR AUDIO FILES
    protected AudioManager audio;
    
    // THIS ALLOWS FOR CUSTOM KEY RESPONSES
    protected KeyListener keyHandler;

    /**
     * Accessor method for this app's key handler
     *
     * @return The key handler registered for this app.
     */
    public KeyListener getKeyHandler()
    {
        return keyHandler;
    }

    /**
     * Mutator method for the keyH handler.
     *
     * @param initKeyHandler The key handler to register for this app.
     */
    public void setKeyListener(KeyListener initKeyHandler)
    {
        keyHandler = initKeyHandler;
    }

    /**
     * This method sets up everything, including the GUI and the game data, and
     * starts the timer, which will force state updates and rendering. Note that
     * rendering will not be seen until the game's window is set visible.
     *
     * @param appTitle the name of the game application, it will be put in the
     * window's title bar.
     *
     * @param initFramesPerSecond the frame rate to be used for running the game
     * application. This refers to how many times each second the game state is
     * updated and rendered.
     */
    public void initMiniGame(String appTitle, int initFramesPerSecond)
    {
        // KEEP THE TITLE AND FRAME RATE
        name = appTitle;
        framesPerSecond = initFramesPerSecond;

        // CALCULATE THE TIME EACH FRAME SHOULD TAKE
        frameDuration = 1000 / framesPerSecond;

        // CONSTRUCT OUR LOCK, WHICH WILL MAKE SURE
        // WE ARE NOT UPDATING THE GAME DATA SIMULATEOUSLY
        // IN TWO DIFFERENT THREADS
        dataLock = new ReentrantLock();

        // AND NOW SETUP THE FULL APP. NOTE THAT SOME
        // OF THESE METHODS MUST BE CUSTOMLY PROVIDED FOR
        // EACH GAME IMPLEMENTATION
//        initAudio();
        initWindow();
        initData();
        initGUI();
        initHandlers();
        initTimer();

        // LET THE USER START THE GAME ON DEMAND
        data.setGameState(MiniGameState.NOT_STARTED);
    }

    // ACCESSOR METHODS
        // getAudio
        // getDataModel
        // getFrameRate
        // getGUIButtons
        // getGUIDecor
        // getBoundaryLeft
        // getBoundaryRight
        // getBoundaryTop
        // getBoundaryBottom
        // getCanvas
    
    /**
     * For accessing the audio player.
     *
     * @return the AudioManager, which can load and play sound and music files.
     */
    public AudioManager getAudio()
    {
        return audio;
    }

    /**
     * For accessing the game data.
     *
     * @return the GameDataModel that stores all the game data.
     */
    public MiniGameDataModel getDataModel()
    {
        return data;
    }

    /**
     * For accessing the frame rate.
     *
     * @return the frame rate, in frames per second of this application.
     */
    public int getFrameRate()
    {
        return framesPerSecond;
    }

    /**
     * For accessing the game GUI buttons.
     *
     * @return the data structure containing all the game buttons.
     */
    public TreeMap<String, Sprite> getGUIButtons()
    {
        return guiButtons;
    }

    /**
     * For accessing the game GUI decor.
     *
     * @return the data structure containing all the game decor.
     */
    public TreeMap<String, Sprite> getGUIDecor()
    {
        return guiDecor;
    }

    /**
     * For accessing the game GUI dialogs.
     *
     * @return the data structure containing all the game dialogs.
     */
    public TreeMap<String, Sprite> getGUIDialogs()
    {
        return guiDialogs;
    }

    /**
     * For accessing the distance from the left edge of the game canvas that
     * will be part of the playing game area.
     *
     * @return the left boundary in pixels of the playing game area.
     */
    public float getBoundaryLeft()
    {
        return boundaryLeft;
    }

    /**
     * For accessing the distance from the left edge of the game canvas that
     * represents the right edge of the playing game area.
     *
     * @return the right boundary in pixels of the playing game area.
     */
    public float getBoundaryRight()
    {
        return boundaryRight;
    }

    /**
     * For accessing the distance from the top edge of the game canvas that will
     * be part of the playing game area.
     *
     * @return the top boundary in pixels of the playing game area.
     */
    public float getBoundaryTop()
    {
        return boundaryTop;
    }

    /**
     * For accessing the distance from the top edge of the game canvas that
     * represents the bottom edge of the playing game area.
     *
     * @return the bottom boundary in pixels of the playing game area.
     */
    public float getBoundaryBottom()
    {
        return boundaryBottom;
    }

    /**
     * For accessing the canvas, which is the JPanel that serves as the game
     * rendering surface and mouse event source.
     */
    public JPanel getCanvas()
    {
        return canvas;
    }

    // INITIALIZATION METHODS - NOTE THAT METHODS ARE MADE private
    // IN PART TO REMOVE THE TEMPTATION TO OVERRIDE THEM
        // initAudio
        // initWindow
        // initGUI
        // initHandler
    
    /**
     * Initialize the audio manager so that the game can play sounds and music.
     */
    private void initAudio()
    {
        // MAKE THE AUDIO MANAGER
        audio = new AudioManager();

        // AND LOAD THE AUDIO, NOTE THIS IS 
        // GAME SPECIFIC, SOME GAMES MAY PLAY
        // SOUND EFFECTS AND MUSIC, OTHERS WON'T
        initAudioContent();
    }

    /**
     * Initializes our GUI's window. Note that this does not initialize the
     * components inside, or the event handlers.
     */
    private void initWindow()
    {
        // CONSTRUCT OUR WINDOW
        window = new JFrame(name);

        // NOTE THAT THE WINDOW WILL BE RESIZED LATER
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setResizable(false);

        // THIS IS JUST A SIMPLE LITTLE GAME THAT DOESN'T
        // SAVE ANY DATA, SO WE'LL JUST KILL THE APP WHEN
        // THE USER PRESSES THE X
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * This should initialize and setup all GUI components. Note that it will
     * invoke the custom-implemented initGUIControls method, which the game
     * developer must provide to setup buttons.
     */
    private void initGUI()
    {
        // INITIALIZE OUR GUI DATA STRUCTURES
        guiButtons = new TreeMap();
        guiDecor = new TreeMap();
        guiDialogs = new TreeMap();

        // WE'LL LAYOUT EVERYTHING USING PIXEL COORDINATES
        window.setLayout(null);

        // GUI CONTROLS ARE SETUP BY THE GAME DEVELOPER
        // USING THIS FRAMEWORK
        initGUIControls();

        // ULTIMATELY, EVERYTHING SHOULD BE INSIDE THE CANVAS
        window.add(canvas);
        canvas.setBounds(0, 0, data.getGameWidth(), data.getGameHeight());
    }

    /**
     * Sets up the event handler mechanics, including invoking the
     * initGUIHandlers method, which would be provided by the game developer
     * using this framework, and would presumably be different for each game.
     */
    private void initHandlers()
    {
        // SETUP THE LOW-LEVEL HANDLER WHO WILL
        // RELAY EVERYTHING
        MiniGameEventRelayer mger = new MiniGameEventRelayer(this);
        canvas.addMouseListener(mger);
        canvas.addMouseMotionListener(mger);
        window.setFocusable(true);
        window.addKeyListener(mger);
        canvas.addKeyListener(mger);

        // AND NOW LET THE GAME DEVELOPER PROVIDE
        // CUSTOM HANDLERS
        initGUIHandlers();
    }

    /**
     * Sets up the timer, which will run the game updates and rendering on a
     * fixed-interval schedule.
     */
    public void initTimer()
    {
        gameTimerTask = new MiniGameTimerTask(this);
        dataLock = new ReentrantLock();
        gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(gameTimerTask, 100, frameDuration);
    }

    // METHODS FOR RUNNING THE GAME, PROVIDING THE MECHANICS
    // OF THESE APPLICATIONS. NOTE THAT THE DEVELOPER USING
    // THIS FRAMEWORK NEED NOT EVEN KNOW ABOUT THESE METHODS,
    // JUST HOW TO PLUG INTO THEM THE SAME WAY YOU DON'T KNOW
    // ABOUT ALL THE INTERNAL WORKINGS OF SWING
        // beginUsingData
        // endUsingData
        // killApplication
        // loadImage
        // loadImageWithColorKey
        // processButtonPress
        // startGame
        // update
    
    /**
     * This method locks access to the game data for the thread that invokes
     * this method. All other threads will be locked out upon their own call to
     * this method and will be forced to wait until this thread ends its use.
     */
    public void beginUsingData()
    {
        //  System.out.println("LOCK #" + dataLock.getHoldCount() + ": " + dataLock);
        dataLock.lock();
    }

    /**
     * This method frees access to the game data for the thread that invokes
     * this method. This will result in notifying any waiting thread that it may
     * proceed.
     */
    public void endUsingData()
    {
        //System.out.println("UNLOCK #" + dataLock.getHoldCount() + ": " + dataLock);
        dataLock.unlock();
    }

    /**
     * Call this method to kill the application associated with this object,
     * including closing the window.
     */
    public void killApplication()
    {
        window.setVisible(false);
        System.exit(0);
    }

    /**
     * Loads an image using the fileName as the full path, returning the
     * constructed and completely loaded Image.
     *
     * @param fileName full path and name of the location of the image file to
     * be loaded.
     *
     * @return the loaded Image, with all data fully loaded.
     */
    public BufferedImage loadImage(String fileName)
    {
        // LOAD THE IMAGE
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image img = tk.createImage(fileName);

        // AND WAIT FOR IT TO BE FULLY IN MEMORY BEFORE RETURNING IT
        MediaTracker tracker = new MediaTracker(window);
        tracker.addImage(img, 0);
        try
        {
            tracker.waitForID(0);
        } catch (InterruptedException ie)
        {
            System.out.println("MT INTERRUPTED");
        }

        // WE'LL USE BUFFERED IMAGES
        BufferedImage imageToLoad = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = imageToLoad.getGraphics();
        g.drawImage(img, 0, 0, null);

        return imageToLoad;
    }

    /**
     * Loads an image using the fileName as the full path, returning the
     * constructed and completely loaded Image. Note that all pixels with the
     * colorKey value will be made transparent by setting their alpha values to
     * 0.
     *
     * @param fileName full path and name of the location of the image file to
     * be loaded.
     *
     * @return the loaded Image, with all data fully loaded and with colorKey
     * pixels transparent.
     */
    public BufferedImage loadImageWithColorKey(String fileName, Color colorKey)
    {
        // LOAD THE IMAGE
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image img = tk.createImage(fileName);

        // WAIT FOR IT TO BE FULLY IN MEMORY
        MediaTracker tracker = new MediaTracker(window);
        tracker.addImage(img, 0);
        try
        {
            tracker.waitForAll();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        // WE'LL PUT IT IN A BufferedImage OBJECT SINCE
        // THAT TYPE OF IMAGE PROVIDES US ACCESS TO
        // ALL OF THE RAW DATA
        BufferedImage imageToLoad = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = imageToLoad.getGraphics();
        g.drawImage(img, 0, 0, null);

        // NOW MAKE ALL PIXELS WITH COLOR (64, 224, 224) TRANSPARENT
        WritableRaster raster = imageToLoad.getRaster();
        int[] dummy = null;
        for (int i = 0; i < raster.getWidth(); i++)
        {
            for (int j = 0; j < raster.getHeight(); j++)
            {
                int[] pixel = raster.getPixel(i, j, dummy);
                if ((pixel[0] == colorKey.getRed())
                        && (pixel[1] == colorKey.getGreen())
                        && (pixel[2] == colorKey.getBlue()))
                {
                    pixel[3] = 0;
                    raster.setPixel(i, j, pixel);
                }
            }
        }
        return imageToLoad;
    }

    /**
     * When invoked, this method results in each button in the GUI testing to
     * see if the x, y coordinates are inside its bounds. If they are, the
     * button's actionPerformed method is invoked and the appropriate event
     * response is executed.
     *
     * @param x the x coordinate on the canvas of the mouse press
     *
     * @param y the y coordinate on the canvas of the mouse press
     *
     * @return true if the mouse press resulted in a button's event handler
     * being executed, false otherwise. This is important because if false is
     * returned, other game logic should proceed.
     */
    public boolean processButtonPress(int x, int y)
    {
        boolean buttonClickPerformed = false;

        // TEST EACH BUTTON
        for (Sprite s : guiButtons.values())
        {
            // THIS METHOD WILL INVOKE actionPeformed WHEN NEEDED
            buttonClickPerformed = s.testForClick(this, x, y);

            // ONLY EXECUTE THE FIRST ONE, SINCE BUTTONS
            // SHOULD NOT OVERLAP
            if (buttonClickPerformed)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Displays the window, allowing the MiniGame application to start accepting
     * user input and allow the user to actually play the game.
     */
    public void startGame()
    {
        // DISPLAY THE WINDOW
        window.setVisible(true);

        // LET'S NOW CORRECT THE WINDOW SIZE. THE REASON WE DO THIS
        // IS BECAUSE WE'RE USING null LAYOUT MANAGER, SO WE NEED TO
        // SIZE STUFF OURSELVES, AND WE WANT THE WINDOW SIZE TO BE THE
        // SIZE OF THE CANVAS + THE BORDER OF THE WINDOW, WHICH WOULD
        // INCLUDE THE TITLE BAR. THIS IS CALLED THE WINDOW'S INSETS
        Insets insets = window.getInsets();
        int correctedWidth = data.getGameWidth() + insets.left + insets.right;
        int correctedHeight = data.getGameHeight() + insets.top + insets.bottom;
        window.setSize(correctedWidth, correctedHeight);
    }

    /**
     * This method is called once per frame and updates and renders everything
     * in the game including the gui.
     */
    public void update()
    {
        // WE ONLY PERFORM GAME LOGIC
        // IF THE GAME IS UNDERWAY
        if (data.inProgress() || data.won() || data.lost())
        {
            //		data.updateDebugText(this);
            if (!data.isPaused())
            {
                data.updateAll(this);
            }
        }

        // WE ALWAYS HAVE TO WORRY ABOUT UPDATING THE GUI
        updateGUI();
    }

    // ABSTRACT METHODS - GAME-SPECIFIC IMPLEMENTATIONS REQUIRED
        // initAudioContent
        // initData
        // initGUIControls
        // initGUIHandlers
        // reset
        // updateGUI
    
    /**
     * Initializes the sound and music to be used by the application. Note that
     * we will simply load these files
     */
    public abstract void initAudioContent();

    /**
     * Initializes the game data used by the application. Note that it is this
     * method's obligation to construct and set this Game's custom GameDataModel
     * object as well as any other needed game objects.
     */
    public abstract void initData();

    /**
     * Initializes the game controls, like buttons, used by the game
     * application.
     */
    public abstract void initGUIControls();

    /**
     * Initializes the game event handlers for things like game gui buttons.
     */
    public abstract void initGUIHandlers();

    /**
     * Invoked when a new game is started, it resets all relevant game data and
     * gui control states.
     */
    public abstract void reset();

    /**
     * Updates the state of all gui controls according to the current game
     * conditions.
     */
    public abstract void updateGUI();
}