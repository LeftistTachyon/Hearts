package hearts;

import java.util.ArrayList;

/**
 * An Interface representing a player.
 * @author GuiGuy
 * @version 1.1.8
 */

public interface IPlayer {
    Card play();
    
    boolean isValidPlay(Card c);
    
    ArrayList<Card> pass();
    
    int getPoints();
    
    void addPoints(int i);
    
    void notifyOfLastPlayedCard(Card c);
    
    int cards();
    
    void addCard(Card c);
    
    void addCards(Card[] cc);
    
    void addCards(ArrayList<Card> cc);
    
    String getName();
    
    Hand getHand();
    
    boolean hasCards();
    
    void notifyFirst();
    
    void notifyNotFirst();
    
    void resetPlayedCards();
    
    void notifyOfFirstPlayedCard(Card c);
    
    int getScore();
    
    void notifyHeartsBroken();
}
