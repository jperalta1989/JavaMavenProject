package texasholdem.game.model;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.List;

/**
 * Table represents a poker table with a deck of cards where players sit at. Has methods to simulate players playing a
 * game.
 */
public class Table {
    /**
     * Stage is an enum representing the current stage of the game. Each stage is assigned an int indicating
     * how many cards need to be dealt to the table during that stage.
     */
    enum Stage {
        // each number value represents number of cards that should be dealt now
        /**
         * Given int of 0.
         */
        PRE_FLOP(0),
        /**
         * Given int of 3.
         */
        FLOP(3),
        /**
         * Given int of 1.
         */
        TURN(1),
        /**
         * Given int of 1.
         */
        RIVER(1),
        /**
         * Given int of 0.
         */
        SHOWDOWN(0);

        private int numberOfCardsToDeal;

        /**
         * Constructor for Stage.
         * @param numberOfCardsToDeal int representing how many Cards need to be dealt to the Table.
         */
        // needed for giving each stage a number value
        Stage(int numberOfCardsToDeal) {
            this.numberOfCardsToDeal = numberOfCardsToDeal;
        }

        /**
         * Returns int representing how many cards need to be dealt to the table during this Stage.
         * @return int representing how many cards need to be dealt to the table during this Stage
         */
        // Returns number of cards that should be on the table this round
        public int getNumberOfCardsToDeal() {
            return numberOfCardsToDeal;
        }
    }

    private final int TABLE_MINIMUM_BET = 10;
    private final int TABLE_SEATS = 6;

    private int pot;
    private Deck deck;
    private Stack<Card> communityCards;
    private List<Player> players;
    private boolean tableIsFull;
    private int dealerIndex;
    private int smallBlindIndex;
    private int bigBlindIndex;
    private int firstToBetIndex;
    private int activePlayers;
    private int amountToCall;


    /**
     * Initialize values.
     */
    public Table() {
        this.init();
    }

    private void init() {
        pot = 0;
        deck = new Deck();
        communityCards = new Stack<>();
        players = new ArrayList<>(TABLE_SEATS);
        tableIsFull = false;
        dealerIndex = smallBlindIndex = bigBlindIndex = -1;
        activePlayers = 0;
        amountToCall = 0;
    }

    /**
     * Returns count of all active players.
     * @return count of all active players
     */
    public int getActivePlayers() {
        return activePlayers;
    }

    /**
     * Return int representing the collective amount of money bet by all playeres in a match.
     * @return int representing the collective amount of money bet by all playeres in a match.
     */
    public int getPot() {
        return pot;
    }

    /**
     * Return the Table's Deck.
     * @return the Table's Deck.
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Returns Stack of Cards dealt to the Table.
     * @return Stack of Cards dealt to the Table
     */
    public Stack<Card> getCommunityCards() {
        return communityCards;
    }

    /**
     * Returns list containing all Players seated at Table.
     * @return list containing all Players seated at Table
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Returns true if table is full and cannot fit in another Player.
     * @return true if table is full and cannot fit in another Player
     */
    public boolean isTableIsFull() {
        return tableIsFull;
    }

    /**
     * Returns the index of the Player in Table's list of Players whose turn it is to deal.
     * @return the index of the Player in Table's list of Players whose turn it is to deal
     */
    public int getDealerIndex() {
        return dealerIndex;
    }

    /**
     * Returns the index of the Player in Table's list of Players whose turn it is to pay small blind.
     * @return the index of the Player in Table's list of Players whose turn it is to pay small blind
     */
    public int getSmallBlindIndex() {
        return smallBlindIndex;
    }

    /**
     * Returns the index of the Player in Table's list of Players whose turn it is to pay big blind.
     * @return the index of the Player in Table's list of Players whose turn it is to pay big blind
     */
    public int getBigBlindIndex() {
        return bigBlindIndex;
    }

    /**
     /**
     * Returns the index of the Player in Table's list of Players will be the first to bet.
     * @return the index of the Player in Table's list of Players will be the first to bet
     */
    public int getFirstToBetIndex() {
        return firstToBetIndex;
    }

    /**
     * Returns current amount of money bet and needed to call.
     * @return current amount of money bet and needed to call
     */
    public int getAmountToCall() {
        return amountToCall;
    }

    /**
     * Adds a Player to the table.
     * @param player Player to add into the Table's game.
     */
    public void playerJoinsGame(Player player) {
        if (!tableIsFull) {
            players.add(player);
            activePlayers++;
            if (players.size() == TABLE_SEATS) {
                tableIsFull = true;
            }

            return;
        }

        // Only prints out if table is full.
        System.out.println("Table is Full " + player.getUserName() + " cannot join game.\n");
    }

    /**
     * Play each round of poker, dealing cards at the appropriate times to the appropriate locations, until
     * all but 1 players folds or all rounds have been completed, in which case each Player's hands are evaluated
     * and compared to find who won.
     */
    public void playMatch() {
        activePlayers = players.size();
        for (Stage stage : Stage.values()) {
            callStageMethod(stage);
            // if all but 1 folded
            if (activePlayers == 1)
                break;
        }
    }

    /**
     * Calls the appropriate method for the current Stage.
     * @param stage the Stage the Table is currently in the middle of.
     * @see Stage
     */
    private void callStageMethod(Stage stage) {
        switch (stage) {
            case PRE_FLOP:
                stagePreFlop();
                break;
            case FLOP:
                dealingStage(stage);
                break;
            case TURN:
                dealingStage(stage);
                break;
            case RIVER:
                dealingStage(stage);
                break;
            case SHOWDOWN:
                stageShowDown();
                break;
            default:
                //throw
        }
    }

    public  void getStageMethodData(String stage) {
        switch (stage) {
            case "pre-flop":
                stagePreFlop();
                break;
            case "flop":
                dealToTable(Stage.FLOP.getNumberOfCardsToDeal());
                break;
            case "turn":
                dealToTable(Stage.TURN.getNumberOfCardsToDeal());
                break;
            case "river":
                dealToTable(Stage.RIVER.getNumberOfCardsToDeal());
                break;
            case "showdown":
                stageShowDown();
                break;
            default:
                //throw
        }
    }

    /**
     * Simulates the preflop stage of poker. This includes dealing a card to each player twice, determining which players are
     * dealer, small blind, and big blind, determining which player goes first, and collects blinds.
     */
    private void stagePreFlop() {

        //System.out.println("Stage: " + stage.toString());
        setDealerAndBlinds();
        int numberOfCardsPerPlayer = 2;
        for (int i = 0; i < numberOfCardsPerPlayer; i++)
            dealEachPlayerOneCard();
        requestSmallBlind();
        requestBigBlind();
        //goThroughRoundOfBetting();
        //System.out.println(this);
    }

    /**
     * Simulates a stage of poker in which the Table is dealt a card(s). This includes dealing as many cards as
     * appropriate for the current round to the Table. The number of cards to deal is received from Stage received.
     * @param stage the stage the Table is currently in the middle of.
     * @see Stage
     */
    private void dealingStage(Stage stage) {
        System.out.println("Stage: " + stage.toString());
        dealToTable(stage.getNumberOfCardsToDeal());
        //System.out.println(this);
        for (Card c : communityCards)
            System.out.print(c);
        System.out.printf("%n");
    }

    /**
     * For each player that did not fold, evaluate Player's hand, then compare each hand and find which hand(s) are
     * greatest. This method then awards the pot to the winner(s). If 2 or more players share the same hand, the
     * pot is split between them.
     */
    private void stageShowDown() {
        //System.out.println("Stage: Showdown:");

        // evaluate each active player's hand
        for (Player p: players) {
            if (!p.getIsFolded())
                p.evaluateHand(communityCards);
        }

        // keep track of winner(s)
        List<Player> winners = new ArrayList<>();

        // find out who won
        for (int i = 0; i < players.size(); i++){
            if (!players.get(i).getIsFolded()) {
                // mark first player found as winner, then compare next player with this one
                if (winners.size() < 1) {
                    winners.add(players.get(i));
                    continue;
                }
                int comparedHandsValue = winners.get(0).compareHandTo(players.get(i));
                if (comparedHandsValue < 0){
                    winners = new ArrayList<>();
                    winners.add(players.get(i));
                }
                else if (comparedHandsValue == 0)
                    winners.add(players.get(i));
            }
        }
        //System.out.println(this);
        giveWinnersPot(winners);
    }

    /**
     * Divides the pot for however many Players, representing winners, are given before distributing the winnings
     * to each player. If the pot could not be split evenly, the first Player in the list of winners recievers the
     * left overs (should be 1).
     * @param winners List of Players. Used with stageShowDown, these are the Players with the highest handValue.
     */
    public void giveWinnersPot(List<Player> winners){
        int splitPotValue = pot/winners.size();

        for (Player p : winners){
            p.collectPot(splitPotValue);
            pot -= splitPotValue;
        }
        if (pot > 0)
            winners.get(0).collectPot(pot);
        pot = 0;
        //System.out.println("Winner(s): ");
        //for (Player winner : winners)
        //    System.out.println(winner);
    }

    /**
     * Add to a Player's balance the value of the pot, then sets the pot to 0.
     * @param winner Player who won. This should represent the last player to have not folded.
     */
    public void giveWinnerPot(Player winner){
        winner.collectPot(pot);
        pot = 0;
        //System.out.println("Winner: ");
        //System.out.println(winner);
    }

    //TODO: make this safe against miscalculation when current dealer is taken out of player list
    private void setDealerAndBlinds() {
        dealerIndex = (dealerIndex + 1) % players.size();
        smallBlindIndex = getNextValidatedPlayerIndex(dealerIndex);
        bigBlindIndex = getNextValidatedPlayerIndex(smallBlindIndex);
        firstToBetIndex = getNextValidatedPlayerIndex(bigBlindIndex);
    }

    private void dealEachPlayerOneCard() {
        System.out.println("Dealing Cards To Players...\n");
            for (Player player : players) {
                player.givePlayerCard(deck.dealCard());
            }
    }

    private void requestSmallBlind() {
        pot += players.get(smallBlindIndex).bet(TABLE_MINIMUM_BET / 2);
    }

    private void requestBigBlind() {
        pot += players.get(bigBlindIndex).bet(TABLE_MINIMUM_BET);
        amountToCall = TABLE_MINIMUM_BET;
    }

    // TODO: Still needs further testing for edge cases. ie: firstToBetIndex folds.
    private void goThroughRoundOfBetting() {
        boolean continueBetting = true;
        int currentTurnIndex = firstToBetIndex;
        int betValue = 0;
        int lastPlayerToRaiseIndex = currentTurnIndex;

        while (continueBetting) {
            Player currentPlayer = players.get(currentTurnIndex);

            if(currentPlayer.getIsFolded()){
                currentTurnIndex = getNextValidatedPlayerIndex(currentTurnIndex);
                continue;
            }

            for (Card c : currentPlayer.getHoleCards())
                System.out.print(c);
            System.out.println("Balance: " + currentPlayer.getBalance());
            if (getAndDoActionForPlayer(currentPlayer))
                lastPlayerToRaiseIndex = currentTurnIndex;

            currentTurnIndex = getNextValidatedPlayerIndex(currentTurnIndex);

            if (activePlayers == 1){
                for (Player p : players){
                    if (!p.getIsFolded()) {
                        giveWinnerPot(p);
                        break;
                    }
                }
                continueBetting = false;
            }

            // if its now about to be the turn of the last person to have raised, then we finished betting.
            if(currentTurnIndex == lastPlayerToRaiseIndex){// && players.get(firstToBetIndex).getCurrentBet() == amountToCall){
                continueBetting = false;
            }
        }
    }

    // returns true if player raised
    private boolean getAndDoActionForPlayer(Player currentPlayer) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Possible Actions:");
        currentPlayer.printPossibleActions(amountToCall);

        boolean actionNotYetMade = true;
        while (actionNotYetMade) {
            actionNotYetMade = false;
            String action = scanner.nextLine().toUpperCase();
            int betValue;
            switch (action) {
                case "CH":
                    pot += currentPlayer.check();
                    break;
                case "C":
                    pot += currentPlayer.call(amountToCall);
                    break;
                case "R":
                    currentPlayer.call(amountToCall);
                    betValue = currentPlayer.raise();
                    amountToCall += betValue;
                    pot += betValue;
                    return true;
                case "J":
                    currentPlayer.call(amountToCall);
                    betValue = currentPlayer.jam();
                    amountToCall += betValue;
                    pot += betValue;
                case "F":
                    currentPlayer.fold();
                    activePlayers--;
                    break;
                default:
                    System.out.println("Incorrect action, try again...");
                    actionNotYetMade = true;
                    break;
            }
        }
        return false;
    }

    private int getNextValidatedPlayerIndex(int index){
        int newIndex = (index + 1) % players.size();
        return newIndex;
    }

    private void dealToTable(int numberOfCardsToDeal) {
        while (numberOfCardsToDeal != 0) {
            communityCards.add(deck.dealCard());
            numberOfCardsToDeal--;
        }
    }

    /**
     * Resets deck, sets pot to 0, clears Cards from the table, and reset each player to prepare for new game.
     */
    public void newGame(){
        pot = 0;
        deck.resetDeck();
        communityCards = new Stack<>();

        for (Player player : players) {
            player.resetPlayer();
        }
    }

    /**
     * Returns string indicating the Table's data and the status of each Player.
     * @return
     */
    @Override
    public String toString() {
        StringBuilder tableInformation = new StringBuilder("Table Information...\n\n");

        tableInformation.append("Current pot = ").append(pot).append("\n");

        tableInformation.append("Community Cards\n\n");
        for (Card card : communityCards) {
            tableInformation.append(card.toString());
        }
        tableInformation.append("\n");

        tableInformation.append("Player Info\n\n");
        for (Player player : players) {
            tableInformation.append(player.toString());
            tableInformation.append("\n");
        }
        return tableInformation.toString();
    }

    /**
     * Entry point for our game.
     *
     * @param args
     */
    public static void main(String[] args) {

        Table table = new Table();

        table.playerJoinsGame(new Player("Kay"));
        table.playerJoinsGame(new Player("Rob"));
        table.playerJoinsGame(new Player("Vanessa"));
        table.playerJoinsGame(new Player("David"));


       /* while (table.getActivePlayers() != 1){
            table.playMatch();
        }*/

        table.playMatch();
    }
}
