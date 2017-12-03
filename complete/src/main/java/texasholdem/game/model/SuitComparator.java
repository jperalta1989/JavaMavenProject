package texasholdem.game.model;

import java.util.Comparator;

/**
 * Created by Ethan on 11/13/2017.
 */
class SuitComparator implements Comparator<Card> {
    @Override
    public int compare(Card a, Card b){
        int ret = b.getSuitValue() - a.getSuitValue();
        return ret == 0 ? b.getValue() - a.getValue(): ret;
    }
}
