package texasholdem;

import java.util.*;

/**
 * Created by Kailie on 10/17/2017.
 */

public class Player {

    private final int DEFAULT_PLAYER_BALANCE = 2000;

    private String username;
    private int balance;
    private Stack<Card> holeCards;
    private int currentBet;
    private boolean isFolded;
    private Scanner scanner;
    private String action;

    Player(String username) {
        this.username = username;
        this.balance = DEFAULT_PLAYER_BALANCE;
        init();
    }

    Player(String username, int balance) {
        this.username = username;
        this.balance = balance;
        init();
    }

    private void init() {
        currentBet = 0;
        holeCards = new Stack<>();
        isFolded = false;
        scanner = new Scanner(System.in);
        action = "";
    }

    protected void displayPlayerInfo() {
        System.out.println(this);
    }

    protected int getBalance() {
        return balance;
    }

    public String getAction(){
        return action;
    }

    public int getCurrentBet(){
        return currentBet;
    }

    public boolean getIsFolded(){
        return isFolded;
    }

    protected boolean canPlay() {
        return getBalance() > 0;
    }

    protected void givePlayerCard(Card card) {
        this.holeCards.push(card);
    }

    protected String getUserName() {
        return username;
    }

    public int check() {
        System.out.println(getUserName() + " checks.");
        action = "CH";

        return 0;
    }

    public int fold() {
        isFolded = true;
        System.out.println(getUserName() + " folds.");
        action = "F";

        return 0;
    }

    public int call(int amountToCall) {
        int difference = amountToCall - currentBet;
        action = "C";

        return bet(difference);
    }

    public int raise() {
        System.out.println("Raise by how much?");
        int raise = scanner.nextInt();
        if (raise > getBalance()) {
            System.err.println("You don't have this much money.");
            System.out.println("Your current balance = " + getBalance());
            raise();
            return 0;
        }

        action = "R";
        return bet(raise);
    }

    public int bet(int amount) {
        balance = balance - amount;
        System.out.println(getUserName() + " bets " + amount);
        currentBet += amount;

        return amount;
    }

    public int getPlayerBetValue(int amountToCall) {
        System.out.println("Possible Actions:");
        printPossibleActions(amountToCall);
        System.out.println(getUserName() + " what would you like to do?");
        String action = scanner.nextLine();

        switch (action) {
            case "CH":
                return check();
            case "F":
                return fold();
            case "C":
                return call(amountToCall);
            case "R":
                return raise();
            default:
                System.out.println("Incorrect action, try again...");
                return getPlayerBetValue(amountToCall);
        }
    }

    private String printPossibleActions(int amountToCall) {
        String possibleActions = "";
        int difference = 0;

        if (amountToCall == currentBet) {
            System.out.println("(CH)eck");
        } else {
            difference = amountToCall - currentBet;
            System.out.println("(C)all: Bet $" + difference);
        }

        System.out.println("(R)aise");

        System.out.println("(F)old");

        return possibleActions;
    }

    @Override
    public String toString() {
        StringBuilder playerInfo = new StringBuilder("username = " + username + "\n");

        playerInfo.append(username).append("'s Balance is $").append(getBalance()).append("\n");
        if (canPlay()) {
            playerInfo.append(username).append(" is still able to play.");
        } else {
            playerInfo.append(username).append(" is no longer able to play.");
        }

        playerInfo.append("\n").append(username).append("'s cards:");

        for (Card card : holeCards) {
            playerInfo.append(card.toString());
        }

        playerInfo.append("\n");

        return playerInfo.toString();
    }

    public static void main(String[] args) {
        Deck deck = new Deck();

        Stack<Player> players = new Stack<>();

        players.push(new Player("Kay"));
        players.push(new Player("Rob", -1000));
        players.push(new Player("Vanessa", 0));
        players.push(new Player("David", 251));

        System.out.println("Dealing Cards To Players...\n");

        for (Player player : players) {
            player.givePlayerCard(deck.dealCard());
        }
        for (Player player : players) {
            player.givePlayerCard(deck.dealCard());
        }

        System.out.println("Players Information...\n");

        for (Player player : players) {
            System.out.println(player);
        }

    }
}
