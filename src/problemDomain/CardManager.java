package problemDomain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class CardManager {

    private Card[] deck;
    private int currentCard;
    public transient BufferedImage allCardsImage;
    public transient BufferedImage tempCardImage;
    public transient BufferedImage cardBackImage;
    public LinkedList<Card> p1;
    public LinkedList<Card> p2;
    public LinkedList<Card> p1WinPile;
    public LinkedList<Card> p2WinPile;
    public LinkedList<Card> pot = new LinkedList<Card>();

    public CardManager() {

        createCards();
        shuffle();
        dealDeck();

        //displayDeck();
    }

    public void createCards() {

        String[] faces = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
        String[] suits = {"Clubs", "Spades", "Hearts", "Diamonds"};

        deck = new Card[52];
        currentCard = 0;

        final int height = 96;
        final int width = 72;
        final int rows = 4;
        final int columns = 13;

        try {
            allCardsImage = ImageIO.read(new File("res/images/allCards.png"));
            cardBackImage = ImageIO.read(new File("res/images/cardBack.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int suit = 0; suit < rows; suit++) {

            for (int faceNum = 0; faceNum < columns; faceNum++) {

                tempCardImage = allCardsImage.getSubimage(
                        faceNum * width + faceNum,
                        suit * height + (suit * 2),
                        width,
                        height);

                deck[(faceNum + (suit * 13))] = new Card(
                        suits[suit],
                        faces[faceNum],
                        (faceNum + 1),
                        tempCardImage);
            }
        }

        for (Card card : deck) { //get this dumb ass for loop out of my face, kevin

            if (card.getFaceName().equalsIgnoreCase("Ace")) {
                card.setFaceValue(14);
            }
        }
    }

    public void displayDeck() {

        for (Card card : deck) { //who are you trying to impress just use a regular ass for loop
            System.out.println(card);
        }

        System.out.println("\nPlayer 1 Cards: " + p1.size());
        for (int i = 0; i < p1.size(); i++) {
            System.out.println(p1.get(i));
        }
        System.out.println("\nPlayer 2 Cards: " + p2.size());
        for (int i = 0; i < p1.size(); i++) {
            System.out.println(p2.get(i));
        }
    }

    public void dealDeck() {

        p1 = new LinkedList<Card>();
        p2 = new LinkedList<Card>();

        for (int i = 0; i < deck.length; i++) {
            p1.add(deck[i]);
            i++;
            p2.add(deck[i]);
        }
    }

    public void shuffle() {

        SecureRandom randomNumber = new SecureRandom();

        for (int first = 0; first < deck.length; first++) {

            int second = randomNumber.nextInt(52);

            Card temp = deck[first];
            deck[first] = deck[second];
            deck[second] = temp;
        }
    }
}
