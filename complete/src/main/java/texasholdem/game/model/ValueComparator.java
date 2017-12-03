package texasholdem.game.model;

import java.util.Comparator;

/**
 * Created by Ethan on 11/13/2017.
 */
public class ValueComparator implements Comparator<Card> {
    @Override
    public int compare(Card a, Card b){
        return b.getValue() - a.getValue();
    }
}

