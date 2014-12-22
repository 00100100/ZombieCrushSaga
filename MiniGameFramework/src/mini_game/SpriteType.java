package mini_game;

import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * A SpriteType describes a particular type of Sprite. In most games, artwork is
 * shared between similar game objects. So, if a game has 20 Orcs, they all
 * share the same artwork. We don't want to load images for each Orc, that's a
 * waste of resources. Instead, we load the art once, and share it. This is
 * common practice in all types of applications.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public class SpriteType
{
    // EACH SpriteType HAS A UNIQUE NAME FOR IDENTIFYING IT,
    // SHOULD THE NEED ARISE.
    private String spriteTypeID;
    
    // A SpriteType MAY HAVE A NUMBER OF DIFFERENT IMAGES TO REPRESENT
    // IT. FOR EXAMPLE, A CHARACTER SPRITE MAY HAVE ONE FOR FACING LEFT,
    // RIGHT, UP, AND DOWN, SO EACH WOULD BE CONSIDERED A DIFFERENT
    // STATE, AND SO EACH IMAGE SHOULD BE PLACED HERE, MAPPING A UNIQUE
    // STATE NAME TO AN IMAGE.
    private HashMap<String, BufferedImage> states;
    
    // WIDTH AND HEIGHT OF THIS SpriteType's IMAGES
    private int width;
    private int height;

    /**
     * Constructor, it requires the sprite type id. Note that states
     * must be subsequently added.
     * 
     * @param initSpriteTypeID ID that uniquely identifies this sprite type.
     */
    public SpriteType(String initSpriteTypeID)
    {
        spriteTypeID = initSpriteTypeID;
        states = new HashMap<String, BufferedImage>();
        width = -1;
        height = -1;
    }

    // ACCESSOR METHODS
        // getSpriteTypeID
        // getStateImage
        // getHeight
        // getWidth
    
    /**
     * Accesses and returns the ID (name) of this sprite type.
     *
     * @return this SpriteType's unique ID
     */
    public String getSpriteTypeID()
    {
        return spriteTypeID;
    }

    /**
     * Accessor method for accessing and returning the Image that correponds to
     * a given state.
     *
     * @param stateName the state for which one wishes to access the image.
     *
     * @return the image registered for the stateName state.
     */
    public BufferedImage getStateImage(String stateName)
    {
        return states.get(stateName);
    }

    /**
     * Accesses and returns the height of all the images used by this
     * SpriteType. Note that all images for a given SpriteType should be
     * identically sized.
     *
     * @return the height in pixels of this SpriteType
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * Accesses and returns the width of all the images used by this SpriteType.
     * Note that all images for a given SpriteType should be identically sized.
     *
     * @return the width in pixels of this SpriteType
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * Adds a state, along with its corresponding image, to this SpriteType
     * object.
     *
     * @param stateName the state to use in registering the image.
     *
     * @param img the Image to use for rendering when a given SpriteType is set
     * with stateName as its state
     */
    public void addState(String stateName, BufferedImage img)
    {
        if (states.isEmpty())
        {
            width = img.getWidth(null);
            height = img.getHeight(null);
        }
        states.put(stateName, img);
    }
}