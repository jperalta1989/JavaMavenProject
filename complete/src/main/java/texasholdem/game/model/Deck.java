package texasholdem.game.model;

import java.util.Collections;
import java.util.Stack;


/**
 * The Deck class represents a stack of 52 Cards, each unique. This class includes methods to reset the deck, shuffle
 * the deck, and to deal a card. This class also keeps track of what was dealt out to be able to reset quickly.
 * @see Card
 */
public class Deck {

    private static final int CARDS_PER_SUIT  = 13;

    private Stack<Card> deck;
    private Stack<Card> usedCards;

    /**
     * Deck constructor fills the stack with 52 unique cards.
     */
    public Deck(){
        deck = new Stack<>();
        usedCards = new Stack<>();
        this.init();
        this.shuffle();
    }

    /**
     * Initialize deck with 52 cards.
     * 13 for each suit declared in Card class.
     */
    private void init(){
        for(Card.Suit suit : Card.Suit.values()){
            for (int j = 2; j < CARDS_PER_SUIT + 2; j++){
                deck.push(new Card(suit, j));
            }
        }
    }

    /**
     * Shuffles the deck if cards are missing from the stack.
     */
    public void shuffle() {

        if(!usedCards.isEmpty()) {
            resetDeck();
        }
    	
        Collections.shuffle(deck);
    }

    /**
     * Refills the deck by placing used cards back into deck stack.
     */
    public void resetDeck(){
        while(!usedCards.isEmpty()) {
            deck.push(usedCards.pop());
        }
    }

    /**
     * Pops and returns a card from the stack.
     *
     * @return Card
     */
    public Card dealCard(){
    	Card deal = deck.pop();
    	usedCards.push(deal);
    	return deal;    	
    }

    @Override
    public String toString(){
        StringBuilder deckValues = new StringBuilder("\n\nCards In Deck: \n");

        for (Card card : deck) {
            deckValues.append(card.toString());
        }

        return deckValues.toString();
    }

    // Use this to test that I am initializing the deck correctly.
    /*
    public static void main(String[] args) {
        Deck deck = new Deck();

        System.out.print(deck.dealCard());
        System.out.print(deck.dealCard());
        System.out.print(deck.dealCard());
        System.out.print(deck.dealCard());
        System.out.print(deck.dealCard());
        System.out.print(deck.dealCard());
        System.out.print(deck.dealCard());
        System.out.print(deck.dealCard());
        System.out.print(deck.dealCard());
        System.out.print(deck.dealCard());

        System.out.print(deck);
    }
    */
}
