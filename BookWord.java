/** COMP 10205 - Assignment 3 - Objects, Hashing and Searching
 *  @author Marko Vockic, 000350323, Mohawk College
 *  February 22 2023
 *  BookWord class that creates a bookword object
 */
public class BookWord implements Comparable<BookWord> {
    
    /* a word from a datafile */
    private String text;

    /* the count of the word from the datafile */
    private int count;

    /**
     * Bookword constructor
     * @param wordtext
     */
    public BookWord(String wordtext){
        this.text = wordtext;
        this.count = 1;
    }

    /**
     * getters for each attribute
     */
    public String getText(){return this.text;}
    public int getCount(){return this.count; }


    /**
     * increments the count each time a word is added
     */
    public void incrementCount(){
        this.count++;
    }

    /**
     * compares two objects of the bookword class
     * @returns true or false if equal
     */
    @Override
    public boolean equals(Object wordToCompare){

        if (wordToCompare == this) {
            return true;
        }
        if (!(wordToCompare instanceof BookWord)) {
            return false;
        }
        BookWord other = (BookWord) wordToCompare;
        return this.text.equals(other.text);
    }

    /**
     * checks if text is empty, if not, hash the text
     * @returns hash code
     */ 
    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + count;
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        return result;
    }

    /**
     * @returns a string about each bookword class, the word and the count
     */
    @Override
    public String toString(){
        return String.format("Word:%s Count:%d", text, count);
    }

    /**
     * compares two strings
     * @returns true or false
     */
    @Override
    public int compareTo(BookWord other) {
        if (this.count < other.count) {
            return 1;
        } else if (this.count > other.count) {
            return -1;
        } else {
            return this.text.compareTo(other.text);
        }
    }


}
