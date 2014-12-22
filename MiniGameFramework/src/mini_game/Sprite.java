package mini_game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Sprite objects represent a visible game entity, which may be a game
 * character, enemy, or button. Sprites can register ActionListeners and thus
 * test for and respond to mouse presses on them. Note that Sprite objects store
 * data particular to single visible entity, and that multiple Sprites will
 * share a single SpriteType, which refers to shared characteristics (like
 * artwork) among those Sprites.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public class Sprite
{
    // WHAT TYPE OF SPRITE IS IT? NOTE THE SpriteType OBJECT
    // HAS ALL THE ARTWORK FOR THE SPRITE. THE REASON IS IF
    // WE HAVE 50 IDENTICAL ORCs, WE ONLY WANT THEIR IMAGES
    // LOADED ONCE, BUT EACH WOULD HAVE INDIVIDUAL CHARACTERISITCS
    // LIKE POSITION, VELOCITY, ETC.
    protected SpriteType spriteType;
    
    // ALLOWS THE SPRITE TO BE ENABLED AND DISABLED
    protected boolean enabled;
    
    // CURRENT GAME WORLD/CANVAS LOCATION OF this SPRITE
    protected float x;
    protected float y;
    
    // CURRENT GAME WORLD/CANVAS VELCOTIY OF this SPRITE. NOTE
    // THAT VELOCITY REFERS TO PIXELS TO BE MOVED PER FRAME
    protected float vX;
    protected float vY;
    
    // CURRENT STATE OF this SPRITE, IT DICTATES WHICH IMAGE
    // SHOULD BE USED FOR RENDERING IT
    protected String state;
    
    // AABB REFERS TO AXIS-ALIGNED-BOUNDING-BOX, WHICH IS A LONG
    // WAY OF SAYING A SQUARE COLLISION BOX. IN SOME GAMES THE
    // COLLISION BOX MAY NEED TO BE SMALLER THAN THE ARTWORK, AND
    // MAY EVEN CHANGE PERIODICALLY.
    protected float aabbX;
    protected float aabbY;
    protected float aabbWidth;
    protected float aabbHeight;
    
    // THE CUSTOM EVENT HANDLER FOR THIS Sprite WHOSE actionPerformed
    // METHOD IS TO INVOKED WHEN THE PLAYER CLICKS ON THIS Sprite
    private ActionListener listener;
    
    // ID NUMBER OF THIS Sprite, THIS IS AUTOMATICALLY ASSIGNED UPON
    // Sprite CONSTRUCTION. NOTE THAT THIS IS A READ-ONLY VALUE
    private int id;
    
    // USED FOR ASSIGNING ID NUMBERS TO SPRITES. EACH TIME ANOTHER
    // Sprite OBJECT IS CONSTRUCTED, WE INCREMENT THIS COUNTER, THUS
    // GETTING A NEW, UNIQUE NUMBER
    private static int idCounter;

    /**
     * Constructor for making a new Sprite with an initial position, velocity,
     * state, etc. Note that a unique, accessible ID number is automatically
     * assigned to each constructed Sprite.
     *
     * @param initSpriteType the SpriteType of this Sprite object, it dictates
     * what artwork will be used for rendering it.
     *
     * @param initX initial X-axis position of this Sprite.
     *
     * @param initY initial Y-axis position of this Sprite.
     *
     * @param initVx initial X-axis velocity of this Sprite.
     *
     * @param initVy initial Y-axis velocity of this Sprite.
     *
     * @param initState initial state of this Sprite, which determines which of
     * the possible images to use in rendering
     */
    public Sprite(SpriteType initSpriteType,
            float initX, float initY,
            float initVx, float initVy,
            String initState)
    {
        // INIT WITH THE PARAMETERS PROVIDED
        spriteType = initSpriteType;
        x = initX;
        y = initY;
        vX = initVx;
        vY = initVy;
        state = initState;
        id = idCounter;

        // IT IS ENABLED BY DEFAULT
        enabled = true;

        // CHANGE THE ID COUNTER FOR THE NEXT Sprite
        idCounter++;

        // BY DEFAULT THE AABB FITS THE SPRITE. NOTE THAT
        // ONE MAY CHANGE THIS USING THE SET METHODS
        aabbX = 0;
        aabbY = 0;
        aabbWidth = spriteType.getWidth();
        aabbHeight = spriteType.getHeight();
    }

    // ACCESSOR METHODS
        // getSpriteType
        // getAABBx
        // getAABBy
        // getAABBwidth
        // getAABBheight
        // getX
        // getY
        // getVx
        // getVy
        // getState
        // getID
        // isEnabled
    
    /**
     * For accessing the axis-aligned bounding box' x-axis coordinate
     *
     * @return the aabb's x value
     */
    public float getAABBx()
    {
        return aabbX;
    }

    /**
     * For accessing the axis-aligned bounding box' y-axis coordinate
     *
     * @return the aabb's y value
     */
    public float getAABBy()
    {
        return aabbY;
    }

    /**
     * For accessing the axis-aligned bounding box' width
     *
     * @return the aabb's width
     */
    public float getAABBwidth()
    {
        return aabbWidth;
    }

    /**
     * For accessing the axis-aligned bounding box' height
     *
     * @return the aabb's height
     */
    public float getAABBheight()
    {
        return aabbHeight;
    }

    /**
     * For accessing the SpriteType object.
     *
     * return the SpriteType of this sprite.
     */
    public SpriteType getSpriteType()
    {
        return spriteType;
    }

    /**
     * For accessing the X-axis coordinate of this Sprite.
     *
     * return the x-axis location of this Sprite according to the top-left
     * corner of the image used.
     */
    public float getX()
    {
        return x;
    }

    /**
     * For accessing the Y-axis coordinate of this Sprite.
     *
     * @return the y-axis location of this Sprite according to the top-left
     * corner of the image used.
     */
    public float getY()
    {
        return y;
    }

    /**
     * For accessing the X-axis velocity of this Sprite. Note that velocity
     * refers to pixels (game world units) moved each frame.
     *
     * @return the x-axis velocity of this Sprite.
     */
    public float getVx()
    {
        return vX;
    }

    /**
     * For accessing the Y-axis velocity of this Sprite. Note that velocity
     * refers to pixels (game world units) moved each frame.
     *
     * @return the y-axis velocity of this Sprite.
     */
    public float getVy()
    {
        return vY;
    }

    /**
     * For accessing the current state of this Sprite. The state determines
     * which image is used for rendering.
     *
     * @return the current Sprite state.
     */
    public String getState()
    {
        return state;
    }

    /**
     * For accessing the automatically generated ID number of this Sprite
     * object.
     *
     * @return the ID number of this Sprite. Note that each Sprite has a unique
     * id, which may or many not be used by the game application developer for
     * things like event handling.
     */
    public int getID()
    {
        return id;
    }

    /**
     * This method checks to see if this sprite is enabled or not. If it is
     * enabled, true is returned, else false.
     *
     * @return true if this sprite is enabled, else false.
     */
    public boolean isEnabled()
    {
        return enabled;
    }

    // MUTATOR METHODS
    // setActionListener
    // setSpriteType
    // setX
    // setY
    // setVx
    // setVy
    // setState
    // setEnabled
    /**
     * Mutator method for specifying this Sprite's event handler for when the
     * player clicks on it. Note addActionListener was deliberately used to
     * avoid confusion, since each Sprite may only have one ActionListener
     *
     * param initListener the event handler to register for this sprite to be
     * associated with mouse presses within its bounds.
     */
    public void setActionListener(ActionListener initListener)
    {
        listener = initListener;
    }

    /**
     * Mutator method for setting the type of Sprite this is.
     *
     * @param initSpriteType the SpriteType to use for rendering this sprite.
     */
    public void setSpriteType(SpriteType initSpriteType)
    {
        spriteType = initSpriteType;
    }

    /**
     * Mutator method for setting the x-axis location of this Sprite.
     *
     * @param initX the x-axis location to move this Sprite to. Note that if a
     * value less than 0 or greater than the game world width is provided the
     * Sprite may no longer be visible, since it won't be in the game canvas.
     */
    public void setX(float initX)
    {
        x = initX;
    }

    /**
     * Mutator method for setting the y-axis location of this Sprite.
     *
     * @param initY the y-axis location to move this Sprite to. Note that if a
     * value less than 0 or greater than the game world height is provided the
     * Sprite may no longer be visible, since it won't be in the game canvas.
     */
    public void setY(float initY)
    {
        y = initY;
    }

    /**
     * Mutator method for setting the x-axis velocity of this Sprite.
     *
     * @param initVx the x-axis velocity to be used. Note that this refers to
     * the amount to move this Sprite each frame in the x-axis.
     */
    public void setVx(float initVx)
    {
        vX = initVx;
    }

    /**
     * Mutator method for setting the y-axis velocity of this Sprite.
     *
     * @param initVy the y-axis velocity to be used. Note that this refers to
     * the amount to move this Sprite each frame in the y-axis.
     */
    public void setVy(float initVy)
    {
        vY = initVy;
    }

    /**
     * Mutator method for setting the state of this Sprite.
     *
     * @param initState the state to use for this Sprite, which will determine
     * which image to use for rendering. Note that one should only set the state
     * to one of the states made available by this Sprite's SpriteType. Using a
     * different will result in error.
     */
    public void setState(String initState)
    {
        state = initState;
    }

    /**
     * Mutator method for enabling and disabling this sprite.
     *
     * @param initEnabled This value enables and disables this sprite.
     */
    public void setEnabled(boolean initEnabled)
    {
        enabled = initEnabled;
    }

    // ADDITIONAL METHODS
        // aabbsOverlap
        // calculateDistanceToSprite
        // containsPoint
        // testForClick
        // update
    
    /**
     * This method tests to see if the the testSprite's AABB overlaps this
     * Sprite's AABB. true is returned if they overlap, false otherwise.
     *
     * return true if this Sprite's bounding box overlaps the testSprite's
     * bounding box. false is returned otherwise.
     */
    public boolean aabbsOverlap(Sprite testSprite)
    {
        if ((x + aabbX) > (testSprite.x + testSprite.aabbX + testSprite.aabbWidth))
        {
            return false;
        } else if ((x + aabbX + aabbWidth) < (testSprite.x + testSprite.aabbX))
        {
            return false;
        } else if ((y + aabbY) > (testSprite.y + testSprite.aabbY + testSprite.aabbHeight))
        {
            return false;
        } else if ((y + aabbY + aabbHeight) < (testSprite.y + testSprite.aabbY))
        {
            return false;
        } else
        {
            return true;
        }
    }

    /**
     * This method calculates and returns the real number distance from this
     * sprite to the targetSprite using the top-left corner of their AABBs as
     * points.
     *
     * @param targetSprite the Sprite to measure the distance to
     *
     * @return a scalar representing the direct distance from this Sprite's AABB
     * to the targetSprite's AABB.
     */
    public float calculateDistanceToSprite(Sprite targetSprite)
    {
        float targetSpriteCenterX = targetSprite.x + targetSprite.aabbX + (targetSprite.aabbWidth / 2);
        float targetSpriteCenterY = targetSprite.y + targetSprite.aabbY + (targetSprite.aabbHeight / 2);

        float centerX = x + aabbX + (aabbWidth / 2);
        float centerY = y + aabbY + (aabbHeight / 2);

        float deltaX = targetSpriteCenterX - centerX;
        float deltaY = targetSpriteCenterY - centerY;

        return (float) Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
    }

    /**
     * This method tests to see if the point represented by pointX, pointY is
     * inside this Sprite's AABB. If it is, true is returned, otherwise false
     * is.
     *
     * @param pointX - the x-axis coordinate of the test point
     *
     * @param pointY - the y-axis coordinate of the test point
     *
     * @return true if the point is inside this Sprite's AABB, false otherwise.
     * Note that the AABB's borders are considered inside the box.
     */
    public boolean containsPoint(float pointX, float pointY)
    {
        // FIRST MOVE THE POINT TO LOCAL COORDINATES
        pointX = pointX - x;
        pointY = pointY - y;

        boolean inXRange = false;
        if ((pointX > aabbX) && (pointX < (aabbX + aabbWidth)))
        {
            inXRange = true;
        }
        boolean inYRange = false;
        if ((pointY > aabbY) && (pointY < (aabbY + aabbHeight)))
        {
            inYRange = true;
        }
        return inXRange && inYRange;
    }

    /**
     * This method tests to if the x,y coordinates are inside this Sprite, and
     * if they are, the registered event listener is invoked, making sure that a
     * lock is first obtained on the data such that necessary updates may be
     * made.
     *
     * @param game the game application being used.
     *
     * @param x the x-axis coordinate of the point being tested.
     *
     * @param y the y-axis coordinate of the point being tested.
     *
     * @return true if the point was inside the Sprite and an event handler is
     * invoked in response. false otherwise, which means false will be returned
     * if the point is inside the Sprite, but no event handler is registered for
     * this Sprite.
     */
    public boolean testForClick(MiniGame game, int x, int y)
    {
        if (!enabled)
        {
            return false;
        }

        if (containsPoint(x, y) && (listener != null))
        {
            ActionEvent ae;
            ae = new ActionEvent(this, id, spriteType.getSpriteTypeID());
            game.beginUsingData();
            listener.actionPerformed(ae);
            game.endUsingData();
            return true;
        } else
        {
            return false;
        }
    }

    /**
     * Called once per frame, this method does a simple update for this sprite,
     * moving its position according to its current velocity, making sure it
     * doesn't leave the playing area. Note that this method should be
     * overridden to provide custom AI or physics behaviour for a particular
     * type of Sprite.
     *
     * @param game
     */
    public void update(MiniGame game)
    {
        // MOVE THE SPRITE USING ITS VELOCITY
        x += vX;
        y += vY;
    }
}