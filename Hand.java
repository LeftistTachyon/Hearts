package hearts;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Models a hand of cards. 
 * @author GuiGuy
 * @version 1.2.1
 */

public class Hand {
    private ArrayList<Card> cards;
    
    public Hand(ArrayList<Card> cc) {
        cards = cc;
    }
    
    public Hand() {
        cards = new ArrayList<>();
    }
    
    public void receiveCard(Card c) {
        cards.add(c);
    }
    
    public ArrayList<Card> getCards() {
        return cards;
    }
    
    public void clearCards() {
        cards.clear();
    }
    
    public boolean useCard(Card c) {
        boolean b = contains(c);
        if(b) {
            int index = cards.indexOf(c);
            if(index == -1) return false;
            cards.remove(c);
            cards.add(index, null);
        }
        return b;
    }
    
    public boolean removeCard(Card c) {
        int at = -1;
        for(Card card:cards) {
            if(card.getSortingValue() == c.getSortingValue()) at = cards.indexOf(card);
        }
        return cards.remove(cards.get(at));
    }
    
    public boolean containsSuit(int suit) {
        return cards.stream().anyMatch((c) -> (c.getSuit() == suit));
    }
    
    public int howManySuits() {
        ArrayList<Integer> suitsContained = new ArrayList<>();
        cards.stream().forEach((c) -> {
            boolean contained = false;
            for(int suit:suitsContained) {
                if(c.getSuit() == suit) contained = true;
            }
            if (!contained) {
                suitsContained.add(c.getSuit());
            }
        });
        return suitsContained.size();
    }
    
    public int suitLeast() {
        int whichSuit = 0, howMuch = 0;
        for(int i = 0;i<4;i++) {
            ArrayList<Card> ofSuit = new ArrayList<>();
            for(Card c:cards) {
                if(c.getSuit() == i) {
                    ofSuit.add(c);
                }
            }
            if(ofSuit.size() > howMuch) whichSuit = i;
        }
        return whichSuit;
    }
    
    @Override
    public String toString() {
        String output = "";
        for(int i = 0;i<cards.size();i++) {
            output += (i + 1) + ": " + cards.get(i).toString() + "\n";
        }
        return output;
    }
    
    private String spaces(int howMany) {
        String output = "";
        for(int i = 0;i<howMany;i++) 
            output += " ";
        return output;
    }
    
    public boolean contains(Card c) {
        boolean b = false;
        for (Card card : cards) {
            if(card == null) continue;
            if(c.getSortingValue() == card.getSortingValue()) b = true;
        }
        return b;
    }
    
    public Card getCard(int at) {
        return cards.get(at);
    }
    
    public boolean purgeNulls() {
        return cards.removeIf(Objects::isNull);
    }
    
    public boolean hasSuit(int suit) {
        return cards.stream().anyMatch((c) -> (c.getSuit() == suit));
    }
}