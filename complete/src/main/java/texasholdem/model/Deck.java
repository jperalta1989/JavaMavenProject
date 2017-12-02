package texasholdem.model;

import java.util.Collections;
import java.util.Stack;


/**
 * Created by Ethan on 9/27/2017.
 */
public class Deck {

    private static final int CARDS_PER_SUIT  = 13;

    private Stack<Card> deck;
    private Stack<Card> usedCards;

    /**
     * Deck constructor.
     * We create 2 empty stack collections.
     * We call init().
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
     * If used cards is empty we shuffle the deck collection.
     */
    public void shuffle() {

        if(!usedCards.isEmpty()) {
            resetDeck();
        }
    	
        Collections.shuffle(deck);
    }

    /**
     * Reset the deck by placing used cards back into deck stack.
     */
    public void resetDeck(){
        while(!usedCards.isEmpty()) {
            deck.push(usedCards.pop());
        }
    }

    /**
     * Pop card from deck collection, place it in used card collection.
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
}
