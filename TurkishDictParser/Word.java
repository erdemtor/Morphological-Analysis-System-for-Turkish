package TurkishDictParser;
/**
 * Created by Erdem on 3/11/2016.
 */
public class Word   {
    String content;
    String type;

    public Word(String content, String type) {
        this.content = content;
        this.type = type;
    }

    public Word() {
    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object w) {
        boolean sameSame = false;

        if (w != null && w instanceof  Word )
        {
            Word wReal = (Word) w;
            sameSame = this.content.equals(wReal.getContent());
        }

        return sameSame;
    }

}
