package hearts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Represents the playing board in Hearts.
 * @author GuiGuy
 * @version 1.3.3
 */

public class Board {
    
    private ArrayList<Card> played;
    private ArrayList<IPlayer> players;
    private int round = 0;
    private boolean heartsBroken;
    
    public Board(int players) {
        played = new ArrayList<>(4);
        this.players = new ArrayList<>();
        heartsBroken = false;
    }
    
    public boolean acceptCard(Card c) {
        return played.add(c);
    }
    
    public void clearBoard() {
        played.clear();
    }
    
    public int highestValueOfFirstSuit() {
        int value = 0;
        for(Card c:played) {
            if(c.getValue() > value) {
                value = c.getValue();
            }
        }
        return value;
    }
    
    public void addPlayer(IPlayer player) {
        players.add(player);
    }
    
    public void addAllPlayers(ArrayList<IPlayer> players) {
        players.stream().forEach((ip) -> this.players.add(ip));
    }
    
    public void startRound() {
        boolean _continue_, overScore;
        Scanner input = new Scanner(System.in);
        do {
            round++;
            pass();
            startPlay();
            showScores();
            _continue_ = doContinue(input);
            overScore = anyScoresAbove100();
        } while(_continue_ || !overScore);
        System.out.println("The game has ended.");
        
    }
    
    private void showRankings() {
        
    }
    
    private boolean anyScoresAbove100() {
        boolean output = false;
        for(IPlayer player:players) {
            if(player.getScore() >= 100) output = true;
        }
        return output;
    }
    
    private synchronized void pass() {
        round %= players.size();
        if(round != 0) {
            LinkedList<ArrayList<Card>> ll = new LinkedList<>();
            players.stream().forEach((player) -> ll.add(player.pass()));
            for(int i = 0;i<players.size();i++) {
                players.get((i+round)%players.size()).addCards(ll.get(i));
            }
        } else {
            System.out.println("No passing");
        }
    }
    
    private boolean doContinue(Scanner s) {
        System.out.println("Do you want to do another round? (Yes/No)");
        String answer = s.nextLine();
        switch(answer) {
            case "Yes":
            case "yes":
            case "YES":
            case "Yes.":
            case "yes.":
            case "YES.":
                return true;
            case "No":
            case "no":
            case "NO":
            case "No.":
            case "no.":
            case "NO.":
                return false;
            default:
                System.out.println("This is a yes or no question.");
                return doContinue(s);
        }
    }
    
    private synchronized void startPlay() {
        String starter = whoHas2Clubs();
        while(hasCards()) {
            HashMap<String, Card> played = new HashMap<>();
            int starterSuit = playMiniRound(played, starter);
            starter = taker(played, starterSuit);
        }
    }
    
    private int playMiniRound(HashMap<String, Card> played, String starter) {
        players.stream().forEach((player) -> player.resetPlayedCards());
        int starterPlace = whoIsStarter(starter);
        for(int i = starterPlace;i<starterPlace+players.size();i++) {
            int truePlace = i%players.size();
            if(i == starterPlace) players.get(truePlace).notifyFirst();
            else players.get(truePlace).notifyNotFirst();/////////////////////CHECK HERE//////////////////
            played.put(players.get(truePlace).getName(), players.get(truePlace).play());
            String middle = "You".equals(players.get(truePlace).getName())?" play the ":" plays the ";
            System.out.println(players.get(truePlace).getName() + middle + played.get(players.get(truePlace).getName()).toString());
            players.stream().forEach((player) -> player.notifyOfLastPlayedCard(played.get(players.get(truePlace).getName())));
            if(i == starterPlace) players.stream().forEach((player) -> player.notifyOfFirstPlayedCard(played.get(players.get(truePlace).getName())));
            if(played.get(players.get(truePlace).getName()).getSuit() == Card.HEART && !heartsBroken) {
                players.stream().forEach((player) -> player.notifyHeartsBroken());
                heartsBroken = true;
            }
        }
        System.out.println();
        return played.get(players.get(starterPlace).getName()).getSuit();
    }
    
    private int whoIsStarter(String starter) {
        for(int i = 0;i<players.size();i++) {
            if(players.get(i).getName() == null ? starter == null : players.get(i).getName().equals(starter)) return i;
        }
        return -1;
    }
    
    private String taker(HashMap<String, Card> played, int starterSuit) {
        ArrayList<String> names = new ArrayList<>();
        players.stream().forEach((player) -> names.add(player.getName()));
        ArrayList<Card> cardsPlayed = new ArrayList<>();
        names.stream().forEach((s) -> cardsPlayed.add(played.get(s)));
        Card largest = new Card(Integer.MIN_VALUE, starterSuit);
        for(Card c: cardsPlayed) {
            if(c.getSuit() == starterSuit && c.getValue() > largest.getValue()) {
                largest = c;
            }
        }
        for(String s: played.keySet()) {
            if(played.get(s).getSortingValue() == largest.getSortingValue()) return s;
        }
        return null;
    }
    
    private boolean hasCards() {
        boolean b = false;
        int cards = players.get(0).cards();
        for(IPlayer player:players) {
            boolean assertion = player.cards() == cards;
            String assertionMessage = "";
            for(IPlayer ip:players) {
                assertionMessage += "\n" + ip.getName() + ": " + ip.cards() + " cards.";
                if(ip.getHand().contains(new Card(2, Card.CLUB))) assertionMessage += "(Has 2♣)";
            }
            assert assertion: "\nAll players should have the same amount of cards." + assertionMessage;
            if(player.hasCards()) b = true;
        }
        return b;
    }
    
    private String whoHas2Clubs() {
        for (IPlayer player : players) {
            if (player.getHand().contains(new Card(2, Card.CLUB))) {
                return player.getName();
            }
        }
        return null;
    }
    
    private void showScores() {
        System.out.println("\nEnd of round.");
        System.out.println("Scores: ");
        for(IPlayer player:players) {
            System.out.println();
        }
    }
    
    public ArrayList<IPlayer> getPlayers() {
        return players;
    }
}