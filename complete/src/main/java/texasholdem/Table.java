package texasholdem;

import java.util.ArrayList;
import java.util.Stack;
import java.util.List;

/**
 * Created by Ethan on 10/17/2017.
 */
public class Table {
    enum Stage {
        PRE_FLOP, FLOP, TURN, RIVER, SHOWDOWN
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

    public int getActivePlayers() {
        return activePlayers;
    }

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

    public void playMatch() {
        for (Stage stage : Stage.values()) {
            callStageMethod(stage);
        }
    }

    private void callStageMethod(Stage stage) {
        switch (stage) {
            case PRE_FLOP:
                stagePreFlop();
                break;
            case FLOP:
                stageFlop();
                break;
            case TURN:
                stageTurn();
                break;
            case RIVER:
                stageRiver();
                break;
            case SHOWDOWN:
                stageShowDown();
                break;
        }
    }

    private void stagePreFlop() {
        System.out.println("Stage: Pre Flop:");
        setDealerAndBlinds();
        dealPlayersHoleCards();
        requestSmallBlind();
        requestBigBlind();
        goThroughRoundOfBetting();
        System.out.println(this);
    }

    private void stageFlop() {
        System.out.println("Stage: Flop:");
        dealToTable(3);
        System.out.println(this);
    }

    private void stageTurn() {
        System.out.println("Stage: Turn:");
        dealToTable(1);
        System.out.println(this);
    }

    private void stageRiver() {
        System.out.println("Stage: River:");
        dealToTable(1);
        System.out.println(this);
    }

    private void stageShowDown() {
        System.out.println("Stage: Showdown:");
        System.out.println(this);
    }

    private void setDealerAndBlinds() {
        dealerIndex++;
        dealerIndex = verifyIndexNotOutOfBounds(dealerIndex);

        smallBlindIndex = dealerIndex + 1;
        smallBlindIndex = verifyIndexNotOutOfBounds(smallBlindIndex);

        bigBlindIndex = smallBlindIndex + 1;
        bigBlindIndex = verifyIndexNotOutOfBounds(bigBlindIndex);

        firstToBetIndex = bigBlindIndex + 1;
        firstToBetIndex = verifyIndexNotOutOfBounds(firstToBetIndex);
    }

    private void dealPlayersHoleCards() {
        int timesToDeal = 2;

        System.out.println("Dealing Cards To Players...\n");

        while (timesToDeal != 0) {
            for (Player player : players) {
                player.givePlayerCard(deck.dealCard());
            }
            timesToDeal--;
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
        int betValue;

        while (continueBetting) {
            Player currentPlayer = players.get(currentTurnIndex);

            if(currentPlayer.getIsFolded()){
                currentTurnIndex++;
                currentTurnIndex = verifyIndexNotOutOfBounds(currentTurnIndex);
                continue;
            }

            betValue = currentPlayer.getPlayerBetValue(amountToCall);

            switch (currentPlayer.getAction()) {
                case "CH":
                    pot += betValue;
                    break;
                case "C":
                    pot += betValue;
                    break;
                case "R":
                    amountToCall += betValue;
                    pot += betValue;
                    break;
                default:
                    amountToCall += betValue;
                    break;
            }

            currentTurnIndex++;
            currentTurnIndex = verifyIndexNotOutOfBounds(currentTurnIndex);

            if(currentTurnIndex == firstToBetIndex && players.get(firstToBetIndex).getCurrentBet() == amountToCall){
                continueBetting = false;
            }
        }
    }

    private int verifyIndexNotOutOfBounds(int index){
        int indexLimit = players.size() - 1;

        if (index > indexLimit) {
            return 0;
        }

        return index;
    }

    private void dealToTable(int numberOfCardsToDeal) {
        while (numberOfCardsToDeal != 0) {
            communityCards.add(deck.dealCard());
            numberOfCardsToDeal--;
        }
    }

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
