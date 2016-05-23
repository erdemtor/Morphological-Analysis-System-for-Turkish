/**
 * Created by Atakan Arıkan on 22.04.2016.
 */
public class CekimEki extends Ek{
    String from;

    public CekimEki(String ek, String from) {
        this.ek = ek;
        this.from = from;
    }

    public String getEk() {
        return ek;
    }

    public void setEk(String ek) {
        this.ek = ek;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    /**
     * If the content of two instance is the same, they are the same.
     * If not, checks their hashCode.
     * returns true if their hashCode are the same.
     */
    public boolean equals(Object y) {
        boolean sameSame = false;
        if (y != null && y instanceof  CekimEki ) {
            CekimEki yReal = (CekimEki) y;
            sameSame = this.getEk().equals(yReal.getEk());
        }
        return sameSame || this.hashCode() == y.hashCode();
    }
    @Override
    public int hashCode() {
        return 3*this.getEk().hashCode() + 17*this.getFrom().hashCode();
    }
    @Override
    public String toString() {
        return "CekimEki{" +
                "from='" + from + '\'' +
                ", ek='" + ek + '\'' +
                '}';
    }
}
