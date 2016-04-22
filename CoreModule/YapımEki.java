/**
 * Created by Atakan Arıkan on 22.04.2016.
 */


public class YapımEki extends Ek {

    String from;
    String to;

    public String getEk() {

        return ek;
    }

    public void setEk(String ek) {
        this.ek = ek;
    }

    public YapımEki(String ek, String from, String to) {
        this.ek = ek;
        this.from = from;
        this.to = to;
    }

    public String getFrom() {

        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }



    @Override
    public boolean equals(Object y) {
        boolean sameSame = false;
        if (y != null && y instanceof  YapımEki ) {
            YapımEki yReal = (YapımEki) y;
            sameSame = this.getEk().equals(yReal.getEk());
        }
        return sameSame;
    }


}
