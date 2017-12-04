package texasholdem.game.model;

/**
 * This class represents a playing card with a number value and a suit character symbol. This class also
 * contains a Suit enum.
 */
public class Card {

    /**
     * Enum Suit represent the Suits of playing cards, with each Suit containing a name, numeric value, and
     * character symbol.
     */
    public enum Suit {
        DIAMONDS("D", 1, '\u2666'),
        CLUBS("C", 2, '\u2663'),
        HEARTS("H", 3, '\u2764'),
        SPADES("S", 4, '\u2660');

        private final String suitName;
        private final Integer suitValue;
        private final Character suitCharacter;

        Suit(String suit, Integer suitValue, Character suitCharacter) {
            this.suitName = suit;
            this.suitValue = suitValue;
            this.suitCharacter = suitCharacter;
        }

        /**
         * Returns a string containing the first letter of the name of the suit. This only contains the first
         * letter for the purpose of getting and displaying the correct image in the web-app.
         * @return a string containing the first letter of the name of the suit
         */
        public String getSuitName() {
            return suitName;
        }

        /**
         * Returns an Integer representing the number value of the suit. This goes from 1 - 4 in the order of
         * diamonds, clubs, hearts, spades.
         * @return an Integer representing the number value of the suit
         */
        public Integer getSuitValue() {
            return suitValue;
        }

        /**
         * Returns a Character for the symbol of the suit.
         * @return a Character for the symbol of the suit
         */
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
    private String cardImg;

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

    public String getCardImg(){
        return cardImg;
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
        this.cardImg = value + suitName + ".png";
    }

    /**
     * {@inheritDoc}
     * Returns string displaying a visual representation of the card, in the format of a pipe character, the numerical
     * value, the suit symbol, and another pipe.
     * @return String
     */
    @Override
    public String toString() {
        return "| " +value + suitCharacter + " |";
    }
}
