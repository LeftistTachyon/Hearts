package hearts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/**
 * Shuffles ArrayLists.
 * @author GuiGuy
 * @version 1.1.8
 */

public class Algorithms {
    private Algorithms(){}
    
    public static void shuffleList(ArrayList a) {
            int n = a.size();
            Random random = new Random();
            random.nextInt();
            for (int i = 0; i < n; i++) {
                int change = i + random.nextInt(n - i);
                swap(a, i, change);
            }
    }

    private static void swap(ArrayList a, int i, int change) {
            Object helper = a.get(i);
            a.set(i, a.get(change));
            a.set(change, helper);
    }
    
    public static Card[] generateDeckOfCards() {
        Card[] deck = new Card[52];
        int at = 0;
        for(int i = 0;i<4;i++) {
            for(int j = 2;j<=Card.ACE;j++) {
                deck[at] = new Card(j, i);
                at++;
            }
        }
        return deck;
    }
    
    public static ArrayList<Card>[] random4Hands() {
        ArrayList<Object> deck = new ArrayList<>(Arrays.asList(generateDeckOfCards()));
        shuffleList(deck);
        ArrayList[] hands = new ArrayList[4];
        for(int i = 0;i<4;i++) {
            hands[i] = new ArrayList();
            for(int j = i*13;j<(i+1)*13;j++) {
                hands[i].add(deck.get(j));
            }
            sortCardList(hands[i]);
        }
        return hands;
    }
    
    public static void sortHand(Hand h) {
        ArrayList<Card> hand = h.getCards();
        sortCardList(hand);
    }
    
    private static void sortCardList(ArrayList<Card> hand) {
        bubbleCards(hand);
    }
    
    // logic to sort the elements smallest to largest
    private static ArrayList<Card> bubbleCards(ArrayList<Card> list) {
        int n = list.size();
        int k;
        for (int m = n; m >= 0; m--) {
            for (int i = 0; i < n - 1; i++) {
                k = i + 1;
                if (list.get(i).getSortingValue()> list.get(k).getSortingValue()) {
                    if(list.get(i) == null||list.get(k) == null) continue;
                    swap(list, i, k);
                }
            }
        }
        return list;
    }
    
    public static ArrayList<Card> bubbleCardsByValue(ArrayList<Card> list) {
        int n = list.size();
        int k;
        for (int m = n; m >= 0; m--) {
            for (int i = 0; i < n - 1; i++) {
                k = i + 1;
                if (list.get(i).getValue()> list.get(k).getValue()) {
                    swap(list, i, k);
                }
            }
        }
        return list;
    }
    
    public static ArrayList<Card> sortedCardsOfSuit(ArrayList<Card> cards, int suit) {
        ArrayList<Card> ofSuit = new ArrayList<>();
        cards.stream().filter((c) -> (c.getSuit() == suit)).forEach((c) -> ofSuit.add(c));
        return bubbleCardsByValue(ofSuit);
    }
    
    public static int howManyofSuit(ArrayList<Card> cards, int suit) {
        int ofSuit = 0;
        return cards.stream().filter((c) -> (c.getSuit() == suit)).map((_item) -> 1).reduce(ofSuit, Integer::sum);
    }
    
    public static void checkIfBS() {
        Board board = Hearts.giveInfo();
        ArrayList<IPlayer> players = board.getPlayers();
        HashMap<Card, Integer> cardCount = new HashMap<>();
        Card[] deck = generateDeckOfCards();
        for (Card c: deck) cardCount.put(c, 0);
        for(IPlayer person:players) {
            if(person == null) continue;
            ArrayList<Card> cards = person.getHand().getCards();
            for(Card c:cards) {
                if(c == null || cardCount.get(c) == null) continue;
                int current = cardCount.get(c);
                cardCount.put(c, current + 1);
            }
        }
        boolean boo = false;
        for(int i = 0;i<cardCount.size();i++) {
            if(cardCount.get(deck[i]) > 1) {
                System.out.println("Uh Oh! The card " + deck[i].toString() + " now has " + cardCount.get(deck[i]) + " copies!");
                boo = true;
            }
        }
        if(!boo) {
            System.out.println("No, the deck isn\'t BS\'ed.");
        }
    }
    
    public static ArrayList<IPlayer> bubblePlayersByScore(ArrayList<IPlayer> players) {
        int n = players.size();
        int k;
        for (int m = n; m >= 0; m--) {
            for (int i = 0; i < n - 1; i++) {
                k = i + 1;
                if (players.get(i).getScore()> players.get(k).getScore()) {
                    swap(players, i, k);
                }
            }
        }
        return players;
    }
    
    public static int findKey(ArrayList<Card> cards, Card toFind) {
        return Collections.binarySearch(cards, toFind);
    }
}
