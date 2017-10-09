package problemDomain;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Card implements Serializable{

    private String suit;
    private String faceName;
    private int faceValue;
    transient BufferedImage cardImage;
    
    private static final long serialVersionUID = -6635341076366698126L;

    /**
     *
     * @param suit
     * @param faceName
     * @param faceValue
     * @param cardImage
     */
    public Card(String suit, String faceName, int faceValue, BufferedImage cardImage) {

        this.suit = suit;
        this.faceName = faceName;
        this.faceValue = faceValue;
        this.cardImage = cardImage;
    }

    /**
     * Return the suit of the card
     *
     * @return suit
     */
    public String getSuit() {
        return suit;
    }

    /**
     *
     * @param suit
     */
    public void setSuit(String suit) {
        this.suit = suit;
    }

    /**
     * Return the faceName of the card
     *
     * @return faceName
     */
    public String getFaceName() {
        return faceName;
    }

    /**
     * Set the faceName
     *
     * @param faceName faceName of Card
     */
    public void setFaceName(String faceName) {
        this.faceName = faceName;
    }

    /**
     * Return the faceValue of the card
     *
     * @return faceValue faceValue of Card
     */
    public int getFaceValue() {
        return faceValue;
    }

    /**
     * Get the faceValue
     *
     * @param faceValue faceValue of card
     */
    public void setFaceValue(int faceValue) {
        this.faceValue = faceValue;
    }

    /**
     * Return the image of the card
     *
     * @return image image of Card
     */
    public BufferedImage getCardImage() {
        return cardImage;
    }

    /**
     * Set the card Image
     *
     * @param cardImage
     */
    public void setCardImage(BufferedImage cardImage) {
        this.cardImage = cardImage;
    }

    @Override
    public String toString() {
        return faceName + " Of " + suit + ": FaceValue: " + faceValue;
    }

}
