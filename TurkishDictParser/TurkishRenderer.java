package TurkishDictParser;


import java.io.*;
import java.util.*;

public class TurkishRenderer {
    public static ArrayList<YapimEki> yapımEkleri;
    public static ArrayList<String> cekimEkleri;
    public static ArrayList<Word> turkish;
    public static HashMap<Character, Character> fix;
    public static HashMap<Integer, String> alphabet;
    public static SortedSet<String> renderedTurkish;
    public static ArrayList<FullWord> fullwords;
    public static void main(String[] args) throws IOException {

        fullwords = new ArrayList<>();
        renderedTurkish = new TreeSet<>();
        yapımEkleri = new ArrayList<>();
        cekimEkleri = new ArrayList<>();
        turkish = new ArrayList<Word>();
        fix = new HashMap<Character, Character>();
        alphabet = new HashMap<Integer, String>();
        createAlphabet();
        fix.put('þ', 'ş');
        fix.put('ð', 'ğ');
        fix.put('ý', 'ı');
        readArticles("yapımekleri.txt", "C:\\Users\\Atakan Arıkan\\Desktop\\Stuff\\NLP\\Sozluk", "çekimekleri.txt");
        BufferedReader read = new BufferedReader(new FileReader(new File("TurkishRoots.txt")));
        String str;
        while ((str = read.readLine()) != null) {
            if (turkish.contains(new Word(str, ""))) {
                for (Word w :
                        turkish) {
                    if (w.getContent().equals(str)) {
                        System.out.println(w.getContent() + " " + w.getType());
                    }
                }
            }
        }
        read.close();
        //renderTurkish();

    }

    public static void createAlphabet() {
        alphabet.put(0, "a");
        alphabet.put(1, "b");
        alphabet.put(2, "c");
        alphabet.put(3, "ç");
        alphabet.put(4, "d");
        alphabet.put(5, "e");
        alphabet.put(6, "f");
        alphabet.put(7, "g");
        alphabet.put(8, "h");
        alphabet.put(9, "ı");
        alphabet.put(10, "i");
        alphabet.put(11, "j");
        alphabet.put(12, "k");
        alphabet.put(13, "l");
        alphabet.put(14, "m");
        alphabet.put(15, "n");
        alphabet.put(16, "o");
        alphabet.put(17, "ö");
        alphabet.put(18, "p");
        alphabet.put(19, "r");
        alphabet.put(20, "s");
        alphabet.put(21, "ş");
        alphabet.put(22, "t");
        alphabet.put(23, "u");
        alphabet.put(24, "ü");
        alphabet.put(25, "v");
        alphabet.put(26, "y");
        alphabet.put(27, "z");
    }


    public static void addRenderedTurkish(String word) {
        if (!renderedTurkish.contains(word)) {
            renderedTurkish.add(word);
        }
    }

    public static void renderTurkish() throws FileNotFoundException, UnsupportedEncodingException {

        boolean isFound;
        for (int i = 0; i < turkish.size(); i++) { // her kelime icin
            Word word = turkish.get(i);
            System.out.println(word.getContent());
            isFound = false;
            String currentWord = word.getContent();

            for (int j = 1; j < currentWord.length(); j++) { // her harf in the word


                if (!isFound)
                    if (turkish.contains(new Word(currentWord.substring(0, j), word.getType()))) { // we found the potential root word

                        FullWord suspected = isEklerProducable(currentWord.substring(j), new Word(currentWord.substring(0, j), word.getType()));
                        if (suspected != null && PostFiltering.isFitsTurkish(suspected)) { // we add the root to renderedTurkish
                            addRenderedTurkish(suspected.getRoot());// we add the root to renderedTurkish
                            isFound = true;
                        }
                    }
                if (isFound)
                    if (renderedTurkish.contains(currentWord.substring(0, j))) { // we found the potential root word
                        FullWord suspected = isEklerProducable(currentWord.substring(j), new Word(currentWord.substring(0, j), word.getType()));
                        if (suspected != null && PostFiltering.isFitsTurkish(suspected)) { // if ekler are producable and it can pass the filter
                            addRenderedTurkish(suspected.getRoot()); // we add the root to renderedTurkish
                            isFound = true;
                        }
                    }
            }
            if (!isFound) {
                addRenderedTurkish(currentWord);

            }
        }
    }

    public static FullWord isEklerProducable(String ekler, Word w) {
        if (yapımEkleri.contains(new YapimEki(ekler, "", ""))) {
            ArrayList<String> eklerForWord = new ArrayList<>();
            eklerForWord.add(ekler);
            return new FullWord(w.getContent(), eklerForWord);
        }
        for (int i = 1; i < ekler.length(); i++) {
            FullWord w1 = isEklerProducable(ekler.substring(0, i), w);
            FullWord w2 = isEklerProducable(ekler.substring(i), w);
            if (w1 != null && w2 != null) {
                ArrayList<String> eks = new ArrayList<>();
                eks.addAll(w1.getEkler());
                eks.addAll(w2.getEkler());
                FullWord wResult = new FullWord(w.getContent(), eks);
                return wResult;
            }
        }
        return null;
    }


    public static void readArticles(String filepath1, String filepath2, String filepath3) throws IOException {
        BufferedReader read = new BufferedReader(new FileReader(new File(filepath1)));
        String str;
        int count = 0;
        int type = 0;
        while ((str = read.readLine()) != null) {
            //  String temp = tokenize(str.trim());
            String[] temp2 = str.split(" \\* ");
            String[] ekler = temp2[0].trim().split(" ");
            String[] actions = temp2[1].trim().split(" ");
            for (int i = 0; i < ekler.length; i++) {
                YapimEki ek = new YapimEki();
                ek.setFrom(actions[0]);
                ek.setTo(actions[1]);
                ek.setEk(ekler[i]);
                yapımEkleri.add(ek);
            }
            type++;
        }
        read.close();
        for (int i = 0; i < 28; i++) { //for each of the files
            String currentFileName = filepath2 + "\\" + alphabet.get(i) + "\\";
            currentFileName += "HARF_" + alphabet.get(i) + ".xml";

            InputStream bytes = new FileInputStream(currentFileName);
            Reader chars = new InputStreamReader(bytes);
            read = new BufferedReader(chars);
            while ((str = read.readLine()) != null) {

                if (str.contains("<name>")) {
                    String afterNameTag = str.substring(str.indexOf("<name>") + 7, str.indexOf("</name>")).trim();
                    if(afterNameTag.length()> 16) continue;

                    if (!afterNameTag.contains(" ")) {
                        boolean isok = true;
                        while ((str = read.readLine()) != null && !str.contains("<lex_class>")) {

                        }
                        if (str == null) {
                            str = "";
                        }
                        String lexClass = str.substring(str.indexOf("<lex_class>") + 11, str.indexOf("</lex_class>")).trim();
                        String wordType = "";
                        if (lexClass.contains("isim")) {
                            wordType = "isim";
                        } else if (lexClass.contains("fiil")) {
                            wordType = "fiil";
                        } else if (lexClass.contains("sıfat")) {
                            wordType = "sıfat";
                        } else if (lexClass.contains("zarf")) {
                            wordType = "zarf";
                        } else {
                            isok = false;
                        }
                        if (isok) {
                            Word word = new Word(afterNameTag.toLowerCase(), wordType);
                            while ((str = read.readLine()) != null && !str.contains("<meaning_text>")) {

                            }
                            if (str == null) {
                                str = "";
                            }
                            String meaning_text = str.substring(str.indexOf("<meaning_text>") + 14, str.indexOf("</meaning_text>")).trim();
                            if (word.getContent().startsWith("yaba")){
                                int a ;
                            }
                            if (meaning_text.contains(" işi") && (word.getContent().endsWith("ma") || word.getContent().endsWith("me"))) {
                                word = new Word(afterNameTag.substring(0, afterNameTag.length()-2).toLowerCase(), "fiil");
                            }
                            turkish.add(word);
                        }

                    }

                }
            }
            read.close();
        }
        read = new BufferedReader(new FileReader(new File(filepath3)));
        while ((str = read.readLine()) != null) {
            cekimEkleri.add(str);
        }
        read.close();
    }

    public static String tokenize(String line) {
        String[] prettyTokens = line.toLowerCase().split(" "); // split the lowercase string by whitespace
        StringBuilder bigBuilder = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < prettyTokens.length; i++) {
            String active = prettyTokens[i];
            if (active.length() > 0) { // parseDouble thinks "1980." a double. avoid that.
                if (!Character.isDigit(active.charAt(active.length() - 1)) && !Character.isLetter(active.charAt(active.length() - 1))) {
                    active = active.substring(0, active.length() - 1);
                }
            }
            try {
                double x = Double.parseDouble(active);
                bigBuilder.append(active);
                bigBuilder.append(" ");
            } catch (Exception e) {  // so this is not a double value
                String activeTemp = active.replace(',', '.'); //try again
                try {
                    double x = Double.parseDouble(activeTemp);
                    bigBuilder.append(activeTemp);
                    bigBuilder.append(" ");

                } catch (Exception e1) {
                    if (active.contains("&lt;")) { // handle words including this
                        active = active.substring(0, active.indexOf("&lt;")) + active.substring(active.indexOf("&lt;") + 3);
                    }
                    for (int j = 0; j < active.length(); j++) {
                        if (Character.isDigit(active.charAt(j)) || Character.isLetter(active.charAt(j))) {
                            stringBuilder.append(active.charAt(j));
                        }
                    }
                    bigBuilder.append(stringBuilder.toString());
                    bigBuilder.append(" ");
                    stringBuilder.setLength(0);
                }
            }
        }
        return bigBuilder.toString();

    }


}
