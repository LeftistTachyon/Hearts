package hearts;

/**
 * A class that represents a playing card.
 * @author GuiGuy
 * @version 1.3.0
 */

public class Card implements Comparable<Card>{
    
    private final int suit, value;
    
    public static final int CLUB = 0;
    
    public static final int DIAMOND = 1;
    
    public static final int SPADE = 2;
    
    public static final int HEART = 3;
    
    public static final int JACK = 11;
    
    public static final int QUEEN = 12;
    
    public static final int KING = 13;
    
    public static final int ACE = 14;
    
    public Card(int value, int suit) {
        this.suit = suit;
        this.value = value;
    }
    
    public int[] getAttributes() {
        return new int[]{value, suit};
    }
    
    @Override
    public String toString() {
        String output = "";
        if(value<=10) {
            if(value>=2) {
                output += Integer.toString(value);
            } else {
                return "Invalid card";
            }
        } else {
            switch(value) {
                case JACK:
                    output += "J";
                    break;
                case QUEEN:
                    output += "Q";
                    break;
                case KING:
                    output += "K";
                    break;
                case ACE:
                    output += "A";
                    break;
                default:
                    return "Invalid card";
            }
        }
        if(0<=suit||suit<=3) {
            switch(suit) {
                case HEART:
                    output += "\u2665"; //♥
                    break;
                case SPADE:
                    output += "\u2660"; //♠
                    break;
                case CLUB:
                    output += "\u2663"; //♣
                    break;
                case DIAMOND:
                    output += "\u2666"; //♦
                    break;
            }
        } else {
            return "Invalid card";
        }
        return output;
    }
    
    public int pointValue() {
         if(suit == Card.HEART) {
             return 1;
         } else {
             if(suit == Card.SPADE && value == Card.QUEEN) {
                 return 13;
             } else {
                 return 0;
             }
         }
    }
    
    public int getValue() {
        return value;
    }
    
    public int getSuit() {
        return suit;
    }
    
    public int getSortingValue() {
        return value + (15 * suit);
    }
    
    public static Card toCard(String card) {
        Card output;
        int suit, value;
        char suitChar;
        if(card.length() == 3 && card.contains("10")) {
            value = 10;
            suitChar = card.charAt(2);
        } else {
            if(Character.isDigit(card.charAt(0))) {
                value = Integer.parseInt(card.substring(0, 1));
            } else {
                switch(card.charAt(0)) {
                    case 'J':
                        value = Card.JACK;
                        break;
                    case 'Q':
                        value = Card.QUEEN;
                        break;
                    case 'K':
                        value = Card.KING;
                        break;
                    case 'A':
                        value = Card.ACE;
                        break;
                    default:
                        value = -1;
                        break;
                }
            }
            suitChar = card.charAt(1);
        }
        switch(suitChar) {
            case '\u2665': //♥
                suit = Card.HEART;
                break;
            case '\u2668': //♦
                suit = Card.DIAMOND;
                break;
            case '\u2663': //♣
                suit = Card.CLUB;
                break;
            case '\u2660': //♠
                suit = Card.SPADE;
                break;
            default:
                suit = -1;
                break;
        }
        output = new Card(value, suit);
        System.out.println(output.toString());
        return output;
    }

    @Override
    public int compareTo(Card c) {
        return getSortingValue() - c.getSortingValue();
    }
}