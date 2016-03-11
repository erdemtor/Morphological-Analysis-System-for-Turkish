package TurkishDictParser; /**
 * Created by Erdem on 3/11/2016.
 */
/**
 * Created by Erdem on 3/11/2016.
 */
public class Word   {
    String content;
    int type;

    public Word(String content, int type) {
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object w)
    {
        boolean sameSame = false;

        if (w != null && w instanceof  Word )
        {
            Word wReal = (Word) w;
            sameSame = this.content.equals(wReal.getContent());
        }

        return sameSame;
    }

}
