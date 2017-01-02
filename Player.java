package hearts;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles the interaction with the Player.
 * @author GuiGuy
 * @version 1.4.0
 */

public class Player implements IPlayer{
    private int points;
    private Card lastPlayed, firstPlayed;
    private final Hand myHand;
    private final String name;
    private boolean first, heartsBroken;
    
    public Player(Hand h, String name) {
        points = 0;
        lastPlayed = null;
        firstPlayed = null;
        myHand = h;
        this.name = name;
        heartsBroken = false;
    }

    @Override
    public boolean isValidPlay(Card c) {
        if(c == null) return true;
        if(c.getSuit() == Card.HEART && !heartsBroken) return false;
        if(firstPlayed == null || first) return true;
        if(myHand.containsSuit(c.getSuit())) {
            return (c.getSuit() == firstPlayed.getSuit());
        } else return true;
    }

    @Override
    public ArrayList<Card> pass() {
        Algorithms.sortHand(myHand);
        System.out.println("You have the following cards: \n" + myHand.toString());
        System.out.println("You need to pass 3 cards.");
        ArrayList<Card> pass = new ArrayList<>();
        ArrayList<Integer> used = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        for(int i = 0;i<3;i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.out.println("What card do you pass? (Type the number.)");
            String answer = input.nextLine();
            boolean isInteger = isInteger(answer);
            if(isInteger) {
                int cardPlace = Integer.parseInt(answer) - 1;
                if(cardPlace < 0 || cardPlace >= myHand.getCards().size()) {
                    System.err.println("Sorry, you don\'t have that card. (Yet.) Please try again.");
                    i--;
                } else if(used.contains(cardPlace)) {
                    System.err.println("Sorry, you have already passed that card. Please try again.");
                    i--;
                } else {
                    Card card = myHand.getCard(cardPlace);
                    used.add(cardPlace);
                    pass.add(card);
                    try {
                        myHand.useCard(card);
                    } catch (NullPointerException e) {
                        System.out.println("Oops! Null!");
                        used.remove(new Integer(cardPlace));
                        i--;
                    }
                }
            } else switch(answer) {
                case "BS":
                    Algorithms.checkIfBS();
                    i--;
                    break;
                case "print":
                    System.out.println(myHand.toString());
                    i--;
                    break;
                default:
                    System.err.println("Sorry, that isn\'t a number. Please try again.");
                    i--;
                    break;
            }
        }
        myHand.purgeNulls();
        return pass;
    }
    
    private boolean isInteger(String s) {
        if(s.charAt(0) == '-') s.replaceFirst("-", "");
        if("".equals(s)) return false;
        boolean isTrue = true;
        for(char c:s.toCharArray())
            if(!Character.isDigit(c)) isTrue = false;
        return isTrue;
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
        Algorithms.sortHand(myHand);
        if(myHand.contains(new Card(2, Card.CLUB))) {
            System.out.println("Automatically played the 2 of clubs.");
            myHand.removeCard(new Card(2, Card.CLUB));
            return new Card(2, Card.CLUB);
        } else {
            Scanner input = new Scanner(System.in);
            System.out.println("You have the following cards: \n" + myHand.toString());
            String answer;
            boolean _continue_ = true;
            do {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                System.out.println("What card do you play? (Type the number.)");
                answer = input.nextLine();
                boolean isInteger = isInteger(answer);
                if(isInteger) {
                    int cardPlace = Integer.parseInt(answer) - 1;
                    if(cardPlace < 0 || cardPlace >= myHand.getCards().size()) {
                        System.err.println("Sorry, you don\'t have that card. (Yet.) Please try again.");
                        _continue_ = false;
                    } else {
                        Card card = myHand.getCard(cardPlace);
                        if(!isValidPlay(card)) {
                            System.err.println("Sorry, that is not a legal play. Please try again.");
                            _continue_ = false;
                        } else {
                            myHand.removeCard(card);
                            return card;
                        }
                    }
                } else switch(answer) {
                    case "BS":
                        Algorithms.checkIfBS();
                        _continue_ = false;
                        break;
                    case "rigged":
                        ArrayList<Card> cards = myHand.getCards();
                        boolean rigged = true;
                        for(Card c:cards) if(isValidPlay(c)) rigged = false;
                        if(rigged) {
                            System.err.println("Check your card validation.");
                            System.exit(1);
                        } else {
                            System.out.println("No, it's fine.");
                            _continue_ = false;
                            break;
                        }
                    case "print":
                        System.out.println(myHand.toString());
                        _continue_ = false;
                        break;
                    default:
                        System.err.println("Sorry, that isn\'t a number. Please try again.");
                        _continue_ = false;
                        break;
                }
            } while (!_continue_);
            boolean notContinue = !_continue_;
            assert notContinue : "\nSomething\'s gone horribly wrong!";
        }
        return null;
    }

    @Override
    public void notifyOfLastPlayedCard(Card c) {
        lastPlayed = c;
    }

    @Override
    public int cards() {
        return myHand.getCards().size();
    }

    @Override
    public void addCard(Card c) {
        myHand.receiveCard(c);
    }

    @Override
    public void addCards(Card[] cc) {
        for(Card c:cc) addCard(c);
    }

    @Override
    public void addCards(ArrayList<Card> cc) {
        for(Card c:cc) addCard(c);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Hand getHand() {
        return myHand;
    }

    @Override
    public boolean hasCards() {
        return cards() != 0;
    }

    @Override
    public void notifyFirst() {
        System.out.println("You\'re first!");
        first = true;
    }

    @Override
    public void notifyNotFirst() {
        first = false;
    }

    @Override
    public void resetPlayedCards() {
        lastPlayed = null;
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
        System.out.println("Hearts have been broken!");
        heartsBroken = true;
    }
}
