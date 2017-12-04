package texasholdem.game.model;

import java.util.*;

/**
 * This class holds data to represent a player, including a List of 2 Cards, a HandValue, and methods to simulate
 * actions of a player which modify the player's data.
 * @see Card
 * @see HandValue
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
    private HandValue handValue;

    /**
     * Default Player Constructor.
     */
    Player(){

    }

    /**
     * This constructor takes a string for the Player's username and assigns the Player the default const balance.
     * @param username String
     */
    Player(String username) {
        this.username = username;
        this.balance = DEFAULT_PLAYER_BALANCE;
        init();
    }

    /**
     * This constructor takes atring for the Player's username and and int for the Player's starting money.
     * @param username String
     * @param balance int for the Player's starting them money.
     */
    public Player(String username, int balance) {
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

    /**
     * Return the remaining balance of Player.
     * @return the remaining balance of Player
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Return string representing the last action done by Player.
     * @return string representing the last action done by Player
     */
    public String getAction(){
        return action;
    }

    /**
     * Return int representing the last bet made by the Player.
     * @return int representing the last bet made by the Player
     */
    public int getCurrentBet(){
        return currentBet;
    }

    /**
     * Return boolean representing whether or not Player folded.
     * @return boolean representing whether or not Player folded.
     */
    public boolean getIsFolded(){
        return isFolded;
    }

    /**
     * Return string representing Player's username.
     * @return string representing Player's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Return Stack of Cards belonging to Player.
     * @return Stack of Cards belonging to Player.
     */
    public Stack<Card> getHoleCards() {
        return holeCards;
    }

    /**
     * Return HandValue of Player.
     * @return HandValue of Player
     */
    public HandValue getHandValue() {
        return handValue;
    }

    /**
     * Return true if player has enough of a balance to continue playing, false if not.
     * @return true if player has enough of a balance to continue playing, false if not
     */
    protected boolean canPlay() {
        return getBalance() > 0;
    }

    /**
     * Gives a Player a card.
     * @param card dealt to player.
     */
    protected void givePlayerCard(Card card) {
        this.holeCards.push(card);
    }

    protected String getUserName() {
        return username;
    }

    /**
     * Simulate a Player checking. Record Player's last action as checking.
     * @return 0
     */
    public int check() {
        System.out.println(getUserName() + " checks.");
        action = "CH";

        return 0;
    }

    /**
     * Simulate Player folding, setting boolean isFolded to true. Record Player's last action as checking.
     * @return 0
     */
    public int fold() {
        isFolded = true;
        System.out.println(getUserName() + " folds.");
        action = "F";

        return 0;
    }

    /**
     * Simulate Player Calling and deduct the called amount from the Player's balance. Record Player's last
     * action as calling.
     * @param amountToCall int indicating how much Player calls/bets.
     * @return amount player paid, excluding what was previously paid.
     */
    public int call(int amountToCall) {
        int difference = amountToCall - currentBet;
        action = "C";

        return bet(difference);
    }

    /**
     * Simulate Player raising, deduct the raised amount from the Player's balance. Record Player's last
     * action as raising.
     * @return how much Player raised by.
     */
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

    /**
     * Bet the given amount, deducting the amount from Player's balance.
     * @param amount int indicating how much to bet by.
     * @return amount that was bet
     */
    public int bet(int amount) {
        balance = balance - amount;
        System.out.println(getUserName() + " bets " + amount);
        currentBet += amount;

        return amount;
    }

    /**
     * Bet all Player's balance. Record last action as jamming.
     * @return amount bet.
     */
    public int jam() {		
    	System.out.println(getUserName() + " decided to thorw all his/her money in!");
    	action = "J";
    	
    	return bet(balance);
    }
    /*
    public int getPlayerBetValue(int amountToCall) {
        for (Card c : holeCards)
            System.out.print(c);
        System.out.println("Balance: " + balance);

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
                call(amountToCall);
                return raise();
            case "J":
            	return jam();
            default:
                System.out.println("Incorrect action, try again...");
                return getPlayerBetValue(amountToCall);
        }
    }
    */

    /**
     * Simulate awarding a player money.
     * @param moneyWon int indicating how much was won.
     */
    public void collectPot(int moneyWon){
        if (moneyWon < 0)
            throw new IllegalArgumentException("ERROR: MONEY WON IS A NEGATIVE VALUE");
        balance += moneyWon;
    }

    /**
     * Instantiate a HandValue out of the player's cards and the community cards which finds and represents
     * the strength of the player's hand.
     * @see HandValue
     * @param communityCards List of Cards recieved from the Table.
     */
    public void evaluateHand(List<Card> communityCards){
        handValue = new HandValue(communityCards, holeCards);
    }

    // if this is greater than other, should return positive
    // if this is less than other, should return negative
    // if this is equal to other, should return 0

    /**
     * Compare's another Player with this Player and see's which is greater based on their respective HandValues.
     * @param other Player
     * @return positive if this Player is greater than other Player, negative if less than, 0 if equal.
     */
    public int compareHandTo(Player other){
        return this.handValue.compareTo(other.handValue);
    }

    /**
     * Print to console possible actions Player has depending on balance and if a bet was made.
     * @param amountToCall int indicating how much needs to be called, disregarding how much was already
     *                     called thus far.
     * @return string listing all possible actions.
     */
    public String printPossibleActions(int amountToCall) {
        String possibleActions = "";
        int difference = 0;

        if (amountToCall == currentBet) {
            System.out.println("(CH)eck");
            possibleActions += "(CH)eck" + "\n";
        } else {
            difference = amountToCall - currentBet;
            System.out.println("(C)all: Bet $" + difference);
            possibleActions += "(C)all: Bet $" + difference + "\n";
        }

        possibleActions += "(R)aise" + "\n";
        possibleActions += "(J)am"   + "\n";
        possibleActions += "(F)old"  + "\n";

        System.out.println("(R)aise");
        System.out.println("(J)am");
        System.out.println("(F)old");

        return possibleActions;
    }

    @Override
    public String toString() {
        StringBuilder playerInfo = new StringBuilder("username = " + username + "\n");

        playerInfo.append(username).append("'s Balance is $").append(getBalance()).append("\n");

        String canPlayString = canPlay() ? " has enough to play.\n" : " does not have enough to play.\n";
        playerInfo.append(username).append(canPlayString);

        String foldedString = isFolded ? " has folded.\n" : " has not folded.\n";
        playerInfo.append(username).append(foldedString);

        playerInfo.append(username).append("'s cards:");

        for (Card card : holeCards) {
            playerInfo.append(card.toString());
        }

        playerInfo.append("\n");

        if (handValue != null)
            playerInfo.append(handValue).append("\n");

        return playerInfo.toString();
    }

    /*
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
    */
}
