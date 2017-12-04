package texasholdem.game.model;

import java.util.Comparator;

/**
 * Comparator for the Card class. This compares Cards by Suit, then, if Suits are equal, by value.
 * @see java.util.Comparator
 * @see ValueComparator
 */
public class SuitComparator implements Comparator<Card> {
    /**
     * This compares Cards by Suit, then, if Suits are equal, by value.
     * @param a Card
     * @param b Card
     * @return positive int when a > b, negative int when a < b, or 0 when a equals b.
     */
    @Override
    public int compare(Card a, Card b){
        int ret = b.getSuitValue() - a.getSuitValue();
        return ret == 0 ? b.getValue() - a.getValue(): ret;
    }
}
