package texasholdem;

/**
 * Created by Ethan on 9/27/2017.
 */
public class Card {

    /**
     * Suit enum class preset with appropriate string and int values.
     */
    enum Suit {

        DIAMONDS("Diamonds", 1, '\u2666'), CLUBS("Clubs", 2, '\u2663'), HEARTS("Hearts", 3, '\u2764'), SPADES("Spades", 4, '\u2660');

        private final String suitName;
        private final Integer suitValue;
        private final Character suitCharacter;

        Suit(String suit, Integer suitValue, Character suitCharacter) {
            this.suitName = suit;
            this.suitValue = suitValue;
            this.suitCharacter = suitCharacter;
        }

        public String getSuitName() {
            return suitName;
        }

        public Integer getSuitValue() {
            return suitValue;
        }

        public Character getSuitCharacter() {
            return suitCharacter;
        }
    }

    /**
     * Card member variables.
     */
    private String suitName;
    private Character suitCharacter;
    private int suitValue;
    private int value;

    /**
     * Return suitName value.
     *
     * @return String
     */
    public String getSuitName() {
        return suitName;
    }

    /**
     * Return suitValue value.
     *
     * @return int
     */
    public int getSuitValue() {
        return suitValue;
    }

    /**
     * Return value value.
     *
     * @return int
     */
    public int getValue() {
        return value;
    }

    /**
     * Card constructor.
     *
     * @param suit  Suit
     * @param value int
     */
    Card(Suit suit, int value) {
        this.suitName = suit.getSuitName();
        this.suitValue = suit.getSuitValue();
        this.suitCharacter = suit.getSuitCharacter();
        this.value = value;
    }

    /*@Override
    public String toString() {
        return "The " + value + " of " + suitName + "\n";
    }*/

    @Override
    public String toString() {
        return "| " +value + suitCharacter + " |";
    }
}
