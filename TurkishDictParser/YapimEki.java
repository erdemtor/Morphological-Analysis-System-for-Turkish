package TurkishDictParser; /**
 * Created by Erdem on 3/11/2016.
 */
/**
 * Created by Erdem on 3/11/2016.
 *
 *
 *
 */

public class YapimEki {

    String ek;
    String from;
    String to;

    public YapimEki() {
    }


    public String getEk() {

        return ek;
    }

    public void setEk(String ek) {
        this.ek = ek;
    }

    public YapimEki(String ek, String from, String to) {
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
    /**
     * If the content of two instance is the same, they are the same derivational suffix
     */
    public boolean equals(Object y) {
        boolean sameSame = false;
        if (y != null && y instanceof  YapimEki ) {
            YapimEki yReal = (YapimEki) y;
            sameSame = this.getEk().equals(yReal.getEk());
        }
        return sameSame;
    }


}
