import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A graph that encodes word ladders.
 *
 * The class does not store the full graph in memory, just a dictionary of words.
 * The edges are then computed on demand.
 */
public class WordLadder implements DirectedGraph<String> {

    private final Set<String> dictionary;
    private final Set<Character> alphabet;

    /**
     * Creates a new empty graph.
     */
    public WordLadder() {
        dictionary = new HashSet<>();
        alphabet = new HashSet<>();
    }

    /**
     * Adds the {@code word} to the dictionary if it only contains letters.
     * The word is converted to lowercase.
     * @param word  the word
     */
    public void addWord(String word) {
        if (word.matches("\\p{L}+")) {
            word = word.toLowerCase();
            dictionary.add(word);
            for (char c : word.toCharArray())
                alphabet.add(c);
        }
    }

    /**
     * Creates a new word ladder graph from the given dictionary file.
     * The file should contain one word per line, except lines starting with "#".
     * @param file  path to a text file
     */
    public WordLadder(String file) throws IOException {
        this();
        Files.lines(Paths.get(file))
            .filter(line -> !line.startsWith("#"))
            .map(String::trim)
            .forEach(this::addWord);
    }

    @Override
    public Set<String> nodes() {
        return Collections.unmodifiableSet(dictionary);
    }

    /**
     * @param  w  a graph node (a word)
     * @return a list of the graph edges that originate from {@code w}
     */
    @Override
    public List<DirectedEdge<String>> outgoingEdges(String w) {
        /****************
         * TODO: Task 2 *
         * Change here. *
         ****************/
        LinkedList<DirectedEdge<String>> neighbors = new LinkedList<>();

        //iterera ??ver alla bokst??ver i ordet. ex: "Hej", 3 bokst??ver som vi m??ste iterera ??ver
        for (int i = 0; i < w.length(); i++) {

            if (i == 0) {
                for (Character c : alphabet) {
                    String ladder = c + w.substring(i + 1);
                    if (dictionary.contains(ladder) && !ladder.equals(w)) {
                        neighbors.add(new DirectedEdge<String>(w, ladder));
                    }
                }
            } else {
                for (Character c : alphabet) {
                    String ladder = w.substring(0, i) + c + w.substring(i + 1);
                    if (dictionary.contains(ladder) && !ladder.equals(w)) {
                        neighbors.add(new DirectedEdge<String>(w, ladder));
                    }
                }
            }
        }

        return neighbors;
    }

    /**
     * @param  w  one node/word
     * @param  u  another node/word
     * @return the guessed best cost for getting from {@code w} to {@code u}
     * (the number of differing character positions)
     */
    @Override
    public double guessCost(String w, String u) {
        /****************
         * TODO: Task 4 *
         * Change here. *
         ****************/
        double cost = 0;
        if (w.length()==u.length()){
            for (int i=0;i<w.length();i++){
                //number of positions where the characters don't match. Optimistic because it assumes that the characters don't match by only 1 step.
                if (w.charAt(i)!=u.charAt(i)){
                    cost+=1;
                }
            }
        }
        return cost;
    }

    @Override
    public String parseNode(String w) {
        return w;
    }

    /**
     * @return a string representation of the graph
     */
    @Override
    public String toString() {
        StringWriter buffer = new StringWriter();
        PrintWriter w = new PrintWriter(buffer);
        w.println("Word ladder graph with " + numNodes() + " words");
        w.println("Alphabet: " + alphabet.stream().map(Object::toString).collect(Collectors.joining()));
        w.println();

        w.println("Random example words with ladder steps:");
        DirectedEdge.printOutgoingEdges(w, this, null);
        return buffer.toString();
    }

}
