package hearts;

import java.util.ArrayList;

/**
 * An attempt to recreate Hearts (the card game) and a CPU that can play the game.
 * @author GuiGuy
 * @version 1.1.1
 */

public class Hearts {
    static Board playingBoard = new Board(4);
    
    public static void main(String[] args) {
        ArrayList<Card>[] hands = Algorithms.random4Hands();
        //for(ArrayList<Card> arc: hands) System.out.println(arc.size());
        Player you = new Player(new Hand(hands[0]), "You");
        playingBoard.addPlayer(you);
        ArrayList<CPU> cpus = new ArrayList<>();
        String[] names = new String[]{"West", "North", "East"};
        for(int i = 0;i<3;i++) {
            cpus.add(new CPU(new Hand(hands[i+1]), names[i]));
        }
        playingBoard.addAllPlayers(toListOfIPlayers(cpus));
        playingBoard.startRound();
    }
    
    public static ArrayList<IPlayer> toListOfIPlayers(ArrayList<CPU> cpus) {
        ArrayList<IPlayer> iplayers = new ArrayList<>();
        cpus.stream().forEach((cpu) -> iplayers.add(cpu));
        return iplayers;
    }
    
    static Board giveInfo() {
        return playingBoard;
    }
}