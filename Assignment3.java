import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
/** COMP 10205 - Assignment 3 - Objects, Hashing and Searching
 *  @author Marko Vockic, 000350323, Mohawk College
 *  February 22 2023
 * 
 * The arraylist.contains took the shortest amount of time.
 * The binarysearch took the fastest time.
 * The simplehashset wouldve taken the fastest if buckets were arranged better.
 */

public class Assignment3{

    /**
     * The starting point of the application
     */
    public static void main(String[] args)
    {
        // File is stored in a resources folder in the project
        final String filename = "src/TheLordOfTheRIngs.txt";
        int count = 0;
        try {
            Scanner scanner = new Scanner(new File(filename));
            scanner.useDelimiter("\\s|\"|\\(|\\)|\\.|\\,|\\?|\\!|\\_|\\-|\\:|\\;|\\n");  // Filter - DO NOT CHANGE
            while (scanner.hasNext()) {
                String fileWord = scanner.next().toLowerCase();
                if (fileWord.length() > 0)
                {
                    // Just print to the screen for now - REMOVE once you have completed code
                    //System.out.printf("%s\n", fileWord);
                    count++;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Exception caught: " + e.getMessage());
        }

        System.out.println("There are " + count + " words in the file ");
        

        // ADD other code after here
        String bookFilePath = "src/TheLordOfTheRIngs.txt";
        String dicFilePath = "src/US-1.txt"; 
        int wordCount = 0;
        int uniqueWordCount = 0;
        // Populate ArrayList<BookWord> from the book
        ArrayList<BookWord> bookArrayList = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(bookFilePath))) {
            scanner.useDelimiter("\\s|\"|\\(|\\)|\\.|\\,|\\?|\\!|\\_|\\-|\\:|\\;|\\n");
            while (scanner.hasNext()) {
                String token = scanner.next().toLowerCase();
                if (!token.isEmpty()) {
                    BookWord bookWord = findBookWord(bookArrayList, token);
                    if (bookWord == null) {
                        bookWord = new BookWord(token);
                        bookArrayList.add(bookWord);
                        uniqueWordCount++;
                    } else {
                        bookWord.incrementCount();
                    }
                    wordCount++; // Increment the word count for each new word
                }
            }
        } catch (FileNotFoundException e) {
            System.err.format("FileNotFoundException: %s%n", e);
        }

        // Populate ArrayList<BookWord> and SimpleHashSet<BookWord> from dictionary text file
        ArrayList<BookWord> dicArrayList = new ArrayList<>();
        SimpleHashSet<BookWord> dictHashSet = new SimpleHashSet<>();
        try (Scanner scanner = new Scanner(new File(dicFilePath))) {
            scanner.useDelimiter("\\s|\"|\\(|\\)|\\.|\\,|\\?|\\!|\\_|\\-|\\:|\\;|\\n");
            while (scanner.hasNext()) {
                String token = scanner.next().toLowerCase();
                if (!token.isEmpty()) {
                    BookWord bookWord = findBookWord(bookArrayList, token);
                    if (bookWord == null) {
                        bookWord = new BookWord(token);
                        dicArrayList.add(bookWord);
                        dictHashSet.insert(bookWord);
                    } else {
                        bookWord.incrementCount();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.format("FileNotFoundException: %s%n", e);
        }

        System.out.println("-----------Assignment Starts Here-----------");
        System.out.println("Total number of words: " + wordCount);
        System.out.println("Total number of unique words: " + uniqueWordCount);

        bookArrayList.sort(Comparator.comparingInt(BookWord::getCount).reversed());
        System.out.println("Top 10 most frequent words:");
        for (int i = 0; i < Math.min(10, bookArrayList.size()); i++) {
            BookWord word = bookArrayList.get(i);
            System.out.println((i+1) + ". " + word.getText() + ": " + word.getCount());
        }

        System.out.println("Words that occur exactly 64 times:");
        for (BookWord word : bookArrayList) {
            if (word.getCount() == 64) {
                System.out.println(word.getText());
            }
        }
        
    
        // The number of words not contained in the dictionary using contains method of ArrayList
        long startTime = System.nanoTime();
        int notInbookArrayListCount1 = 0;
        for (BookWord word : dicArrayList) {
            if (!bookArrayList.contains(word)) {
                notInbookArrayListCount1++;
            }
        }
        long elapsedTime = System.nanoTime() - startTime;
        System.out.println("Number of words that are not contained in the dictionary (ArrayList.Contains): " + notInbookArrayListCount1);
        System.out.printf("Time required = %10d ns \n ", elapsedTime);

        // The number of words not contained in the dictionary using binarySearch method of Collections class
        startTime = System.nanoTime();
        int notInbookArrayListCount2 = 0;
        ArrayList<BookWord> sortedbookArrayList = new ArrayList<>(bookArrayList);
        Collections.sort(sortedbookArrayList);
        for (BookWord word : dicArrayList) {
            if (Collections.binarySearch(sortedbookArrayList, word) < 0) {
                notInbookArrayListCount2++;
            }
        }
        elapsedTime = System.nanoTime() - startTime;
        System.out.println("Number of words that are not contained in the dictionary (Collections.binarySearch): " + notInbookArrayListCount2);
        System.out.printf("Time required = %10d ns \n", elapsedTime);

        // The number of words not contained in the dictionary using contains method of SimpleHashSet
        startTime = System.nanoTime();
        SimpleHashSet<BookWord> set1 = new SimpleHashSet<>();
        for (BookWord word : bookArrayList) {
            set1.insert(word);
        }
        int notInbookArrayListCount3 = 0;
        for (BookWord word : dicArrayList) {
            if (!set1.contains(word)) {
                notInbookArrayListCount3++;
            }
        }
        elapsedTime = System.nanoTime() - startTime;
        System.out.println("Number of words that are not contained in the dictionary (SimpleHashSet.contains): " + notInbookArrayListCount3);
        System.out.printf("Time required = %10d ns \n", elapsedTime);

        
        // Find closest character to "ring" in wordList1
        startTime = System.nanoTime();
        String[] characters = {"frodo", "sam", "bilbo", "gandalf", "boromir", "aragorn", "legolas", "gollum", "pippin", "merry", "gimli", "sauron", "saruman", "faramir", "denethor", "treebeard", "elrond", "galadriel"};
        // Create a list of characters
        Map<String, Double> proximityMap = new HashMap<>();
        for (String character : characters) {
            double proximity = 0;
            int distance = Integer.MAX_VALUE;
            for (int i = 0; i < bookArrayList.size(); i++) {
                if (bookArrayList.get(i).getText().equals("ring")) {
                    int d = Math.abs(i - bookArrayList.indexOf(new BookWord(character)));
                    if (d < distance) {
                        distance = d;
                        proximity = (double) bookArrayList.get(i).getCount() / distance;
                    }
                }
            }
            if (proximity > 0 && distance <= 42) {
                proximityMap.put(character, proximity);
            }
        }
        
        // Print proximity distances for each character
        System.out.println("Proximity distances for characters closest to \"ring\":");
        for (Map.Entry<String, Double> entry : proximityMap.entrySet()) {
            System.out.printf("%s: %.2f\n", entry.getKey(), entry.getValue());
        }
        elapsedTime = System.nanoTime() - startTime;
        System.out.printf("Time required = %10d ns \n", elapsedTime);

        

    }      

    /**
     * finds word in bookword object
     * @param words an arraylist<bookword> object
     * @param wordText a word
     * @return word
     */
    private static BookWord findBookWord(ArrayList<BookWord> words, String wordText) {
        for (BookWord word : words) {
            if (word.getText().equals(wordText)) {
                return word;
            }
        }
        return null;
    }
        
}



