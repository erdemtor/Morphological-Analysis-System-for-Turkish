import java.util.ArrayList;

/**
 * Created by Atakan Arıkan on 22.04.2016.
 */
public class WordDetail {
    String root = "";
    ArrayList<Ek> ekler = new ArrayList<>();

    public WordDetail(String root, ArrayList<Ek> ekler) {
        this.root = root;
        this.ekler = ekler;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public ArrayList<Ek> getEkler() {
        return ekler;
    }

    public void setEkler(ArrayList<Ek> ekler) {
        this.ekler = ekler;
    }

    @Override
    public int hashCode() {
        int hash = 7*this.getRoot().hashCode();
        hash += 17*this.getEkler().hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode();
    }

    @Override
    public String toString() {
        return " " + root + " (root) " + ekler.toString() + " ";
    }
}
