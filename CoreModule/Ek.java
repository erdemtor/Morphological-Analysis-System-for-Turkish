/**
 * Created by Atakan Arıkan on 22.04.2016.
 */
public class Ek {
    String ek, type;

    public Ek(String ek, String type) {
        this.ek = ek;
        this.type = type;
    }

    public Ek() {
    }

    @Override
    public String toString() {
        return "Ek{" +
                "ek='" + ek + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getEk() {
        return ek;
    }

    public void setEk(String ek) {
        this.ek = ek;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
