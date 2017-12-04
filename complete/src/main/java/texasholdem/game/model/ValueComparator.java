package texasholdem.game.model;

import java.util.Comparator;

/**
 * Comparator for the Card class. This compares Cards by Card value alone.
 * @see java.util.Comparator
 * @see SuitComparator
 */
public class ValueComparator implements Comparator<Card> {
    /**
     * This compares Cards by Card value alone.
     * @param a Card
     * @param b Card
     * @return positive int when a > b, negative int when a < b, or 0 when a equals b.
     */

    @Override
    public int compare(Card a, Card b){
        return b.getValue() - a.getValue();
    }
}

