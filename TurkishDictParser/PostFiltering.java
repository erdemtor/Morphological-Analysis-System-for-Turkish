package TurkishDictParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erdem on 4/1/2016.
 */
public class PostFiltering {

    static ArrayList<Word> turkish = TurkishRenderer.turkish;
    static ArrayList<YapimEki> yapımEkleri = TurkishRenderer.yapımEkleri;

    /**
     * Given the parameter content, returns the corresponding instance of Word in the turkish list.
     * @param content content of a word
     * @return an instance of Word
     */
    public static Word getWordByContent(String content) {
        for (Word tw : turkish) {
            if (tw.getContent().equals(content)) {
                return tw;
            }

        }
        return null;
    }

    /**
     * Returns all the matching instances of YapımEki given the suffix and the source state.
     *
     * There could be two derivational suffixes that have the same content and the same source state but two different target states.
     * This method returns both.
     * @param ek content of the derivational suffix.
     * @param state source state
     * @return List of YapımEki
     */
    public static ArrayList<YapimEki> getYapimEkleriList(String ek, String state) {
        ArrayList<YapimEki> yapimeks = new ArrayList<>();
        for (YapimEki y : yapımEkleri) {
            if (y.getEk().equals(ek) && y.getFrom().equals(state)) {
                yapimeks.add(y);
            }
        }
        return yapimeks;
    }

    /**
     *  Checks the given word's suffix list and root's lexical class and returns true if the word obeys the rules of Turkish.
     *
     * Recursively checks if there's an error with the currentState and the target state of the first element of the suspected suffix list.
     * If yes, returns false, else calls itself for the next element in the list, and sets the current state as the target state of the currently considered
     * derivational suffix.
     *
     * @param suspected list of derivational suffixes
     * @param currentState lexical class of the generated word so far.
     * @return false if there's an error, true if not.
     */
    public static boolean checkEklerRecursively(List<String> suspected, String currentState) {
        if (suspected.size() == 0)
            return true;

        ArrayList<YapimEki> candidateEks = getYapimEkleriList(suspected.get(0), currentState);
        if (candidateEks.size() == 0) {
            return false;
        }

        for (YapimEki y : candidateEks) {
            if (checkEklerRecursively(suspected.subList(1, suspected.size()), y.getTo())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper function for checkEklerRecursively()
     *
     * @param suspected an instance of FullWord
     * @return true if given FullWord obeys the rules of Turkish.
     */
    public static boolean isFitsTurkish(FullWord suspected) {
        Word currentWord = getWordByContent(suspected.getRoot());
        String state = currentWord.getType();
        return checkEklerRecursively(suspected.getEkler(), state);
    }


}
