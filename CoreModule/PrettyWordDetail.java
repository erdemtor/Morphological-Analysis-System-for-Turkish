import java.util.ArrayList;

/**
 * Created by Atakan Arıkan on 29.05.2016.
 */
public class PrettyWordDetail {
    String root = "";
    ArrayList<String> ekler = new ArrayList<>();

    public PrettyWordDetail(String root, ArrayList<String> ekler) {
        this.root = root;
        this.ekler = ekler;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public ArrayList<String> getEkler() {
        return ekler;
    }

    public void setEkler(ArrayList<String> ekler) {
        this.ekler = ekler;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrettyWordDetail that = (PrettyWordDetail) o;

        if (root != null ? !root.equals(that.root) : that.root != null) return false;
        return ekler != null ? ekler.equals(that.ekler) : that.ekler == null;

    }

    @Override
    public int hashCode() {
        int result = root != null ? root.hashCode() : 0;
        result = 31 * result + (ekler != null ? ekler.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return " " + root + " (root) " + ekler.toString() + " ";
    }
}
