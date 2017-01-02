package hearts;

import java.util.ArrayList;

/**
 * Models the CPU playing against you.
 * @author GuiGuy
 * @version 1.4.3
 */

public class CPU implements IPlayer{
    private boolean first, heartsBroken;
    private int points;
    private Card firstPlayed, highestPlayed;
    private final Hand hand;
    private final String name;
    
    public CPU(Hand h, String name) {
        points = 0;
        firstPlayed = null;
        hand = h;
        this.name = name;
        heartsBroken = false;
    }

    @Override
    public boolean isValidPlay(Card c) {
        if(c == null) return true;
        if(c.getSuit() == Card.HEART && !heartsBroken) return false;
        if(firstPlayed == null || first) return true;
        if(hand.containsSuit(c.getSuit())) {
            return (c.getSuit() == firstPlayed.getSuit());
        } else return true;
    }

    @Override
    public ArrayList<Card> pass() {
        ArrayList<Card> toPass = new ArrayList<>();
        int pass = 3;
        if(hand.contains(new Card(2, Card.CLUB))) {
            hand.removeCard(new Card(2, Card.CLUB));
            toPass.add(new Card(2, Card.CLUB));
            pass--;
        }
        ArrayList<Card> cards = hand.getCards();
        Algorithms.bubbleCardsByValue(cards);
        int counter = cards.size() - 1;
        while(pass != 0 || toPass.size() != 3) {
            toPass.add(cards.get(counter));
            hand.removeCard(cards.get(counter));
            counter--;
            pass--;
        }
        //System.out.println(name  + ": " + hand.getCards().size() + " + " + toPass.size() + " = 13(?)");
        return toPass;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public void addPoints(int i) {
        points += i;
    }

    @Override
    public Card play() {
        Algorithms.sortHand(hand);
        if(hand.contains(new Card(2, Card.CLUB))) {
            hand.removeCard(new Card(2, Card.CLUB));
            return new Card(2, Card.CLUB);
        } else {
            boolean isNull = firstPlayed == null && first;
            if (isNull) {
                return wasteSuit(hand.suitLeast());
            } else {
                if (hand.howManySuits() == 4 && firstPlayed.getSuit() == hand.suitLeast()) {
                    return wasteSuit(firstPlayed.getSuit());
                } else {
                    return asHighAsPossible();
                }
            }
        }
    }
    
    private Card wasteSuit(int suit) {
        //System.out.print("w");
        ArrayList<Card> cards = Algorithms.sortedCardsOfSuit(hand.getCards(), suit);
        boolean notEmpty = !cards.isEmpty();
        assert notEmpty : "\n" + name + " needs to have at least one card to waste a suit. Cards: " + cards.size();
        Algorithms.shuffleList(cards);
        for(Card c:cards) {
            if(isValidPlay(c)) {
                hand.removeCard(c);
                return c;
            }
        }
        return asHighAsPossible();
    }
    
    //problematic
    private Card asHighAsPossible() {
        //System.out.print("h");
        ArrayList<Card> cards = hand.getCards();
        if(firstPlayed == null) {
            return wasteSuit((int) Math.round(Math.random() * 4));
        } else {
            if(hand.containsSuit(firstPlayed.getSuit())) {
                ArrayList<Card> sortedCardsOfSuit = Algorithms.sortedCardsOfSuit(cards, firstPlayed.getSuit());
                for(int i = 0;i<sortedCardsOfSuit.size();i++) {
                    if(sortedCardsOfSuit.get(i).getValue() < highestPlayed.getValue() && isValidPlay(sortedCardsOfSuit.get(i))) {
                        Card toPlay = sortedCardsOfSuit.remove(i);
                        hand.removeCard(toPlay);
                        return toPlay;
                    }
                }
                hand.removeCard(sortedCardsOfSuit.get(sortedCardsOfSuit.size() - 1));
                return sortedCardsOfSuit.get(sortedCardsOfSuit.size() - 1);
            } else {
                Algorithms.bubbleCardsByValue(cards);
                for(int i = cards.size() - 1;i>=0;i--) {
                    if(isValidPlay(cards.get(i))) {
                        Card toPlay = cards.remove(i);
                        hand.removeCard(toPlay);
                        return toPlay;
                    }
                }
            }
        }
        if(hand.containsSuit(firstPlayed.getSuit())) {
            return wasteSuit(firstPlayed.getSuit()); //offending
        } else {
            return wasteSuit(hand.suitLeast());
        }
    }

    @Override
    public void notifyOfLastPlayedCard(Card c) {
        if(highestPlayed == null || firstPlayed == null || c == null) highestPlayed = c;
        else if(c.getSuit() == firstPlayed.getSuit() && c.getValue() > highestPlayed.getSuit()) highestPlayed = c;
    }

    @Override
    public int cards() {
        return hand.getCards().size();
    }
    
    @Override
    public void addCard(Card c) {
        hand.receiveCard(c);
    }

    @Override
    public void addCards(Card[] cc) {
        for(Card c:cc) addCard(c);
    }

    @Override
    public void addCards(ArrayList<Card> cc) {
        cc.stream().forEach((c) -> addCard(c));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Hand getHand() {
        return hand;
    }

    @Override
    public boolean hasCards() {
        return cards() != 0;
    }

    @Override
    public void notifyFirst() {
        first = true;
    }

    @Override
    public void notifyNotFirst() {
        first = false;
    }
    
    @Override
    public void resetPlayedCards() {
        firstPlayed = null;
    }

    @Override
    public void notifyOfFirstPlayedCard(Card c) {
        firstPlayed = c;
    }

    @Override
    public int getScore() {
        return points;
    }

    @Override
    public void notifyHeartsBroken() {
        heartsBroken = true;
    }
}
