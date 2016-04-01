package TurkishDictParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erdem on 4/1/2016.
 */
public class FullWord {
    String root;
    List<String> ekler = new ArrayList<>();

    public FullWord(String root, List<String> ekler) {
        this.root = root;
        this.ekler = ekler;
    }

    public FullWord() {

    }

    public String getRoot() {

        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public List<String> getEkler() {
        return ekler;
    }

    public void setEkler(List<String> ekler) {
        this.ekler = ekler;
    }
}
