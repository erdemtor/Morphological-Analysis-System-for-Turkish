package TurkishDictParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erdem on 4/1/2016.
 */
public class PostFiltering {

    static ArrayList<Word> turkish = TurkishRenderer.turkish;
    static ArrayList<YapimEki> yapımEkleri = TurkishRenderer.yapımEkleri;

    public static Word getWordByContent(String content) {
        for (Word tw : turkish) {
            if (tw.getContent().equals(content)) {
                return tw;
            }

        }
        return null;
    }

    public static ArrayList<YapimEki> getYapimEkleriList(String ek, String state) {
        ArrayList<YapimEki> yapimeks = new ArrayList<>();
        for (YapimEki y : yapımEkleri) {
            if (y.getEk().equals(ek) && y.getFrom().equals(state)) {
                yapimeks.add(y);
            }
        }
        return yapimeks;
    }

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


    public static boolean isFitsTurkish(FullWord suspected) {
        Word currentWord = getWordByContent(suspected.getRoot());
        String state = currentWord.getType();
        return checkEklerRecursively(suspected.getEkler(), state);
    }


}
