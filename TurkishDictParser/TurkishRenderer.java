package TurkishDictParser;


import java.io.*;
import java.util.*;

public class TurkishRenderer {
    public static HashSet<YapimEki> yapımEkleri;
    public static ArrayList<Word> turkish;
    public static HashMap<Character, Character> fix;
    public static HashMap<Integer, String> alphabet;
    public static ArrayList<String> renderedTurkish;

    public static void main(String[] args) throws IOException {



        renderedTurkish = new ArrayList<String>();
        yapımEkleri = new HashSet<YapimEki>();
        turkish = new ArrayList<Word>();
        fix = new HashMap<Character, Character>();
        alphabet = new HashMap<Integer, String>();
        createAlphabet();
        fix.put('þ', 'ş');
        fix.put('ð', 'ğ');
        fix.put('ý', 'ı');
        readArticles("yapımekleri.txt", "C:\\Users\\Erdem\\Desktop\\NLP\\Sozluk");
        System.out.println(yapımEkleri.size());
        renderTurkish();

        System.out.println(yapımEkleri.size());
        System.out.println(turkish.size());
        System.out.println(renderedTurkish.size());
        System.out.println(renderedTurkish.toString());

        PrintWriter writer = new PrintWriter("renderedTurkish.txt", "UTF-8");
        for (String stem: renderedTurkish) {
            writer.println(stem);

        }


        writer.close();





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

/*    public static void addTurkish(String word) {
        if (Collections.binarySearch(turkish, word) < 0) {
            turkish.add(word);
        }
    }*/

    public static void addRenderedTurkish(String word) {
        if (!renderedTurkish.contains(word)) {
            renderedTurkish.add(word);
        }
    }

    public static void renderTurkish() {
        for (int i = 0; i < turkish.size(); i++) { // her kelime ixin
            Word word = turkish.get(i);
            boolean isFound = false;
            String currentWord = word.getContent();
            for (int j = 1; j < currentWord.length(); j++) { // her harf in th e word
                if (turkish.contains(new Word(currentWord.substring(0, j),word.getType()))) { // we found the word
                    if (isEklerProducable(currentWord.substring(j), turkish.get(turkish.indexOf(new Word(currentWord.substring(0, j),word.getType()))))) {
                        /* bu kelime bir yapım eki serisi ile oluşmuş, kök değil, kök versiyonunu eklemeye çalışıyoruz*/
                        addRenderedTurkish(currentWord.substring(0, j));
                        isFound = true;
                        break;
                    }
                }
            }
            if(!isFound) addRenderedTurkish(currentWord);
        }
    }

    public static boolean isEklerProducable(String ekler , Word w) {
        if(yapımEkleri.contains(ekler)) return true;
        for (int i = 0; i < ekler.length(); i++) {
            //if(isEklerProducable(ekler.substring(0, i)) && isEklerProducable(ekler.substring(i))) {
                //    if(!yapımEkleri.contains(ekler)) yapımEkleri.add(ekler);// // TODO: 3/11/2016 BURADA TYPE ILE ILGILI BIR DURUM VAR, KELIME CLASSI YARAT, xml to json to object,
                return true;
        //    }
        }
        return false;
    }


    public static void readArticles(String filepath1, String filepath2) throws IOException {
        BufferedReader read = new BufferedReader(new FileReader(new File(filepath1)));
        String str;
        int count = 0;
        int type = 0;
        while ((str = read.readLine()) != null) {
            String temp = tokenize(str.trim());
            String[] temp2 = temp.split(" ");
            for (int i = 0; i < temp2.length; i++) {
                YapimEki ek = new YapimEki();
                ek.setAction(type);
                ek.setEk(temp2[i]);
                yapımEkleri.add(ek);
            }
            type++;
        }
        read.close();
        for (int i = 0; i < 1; i++) { //for each of the files
            String currentFileName = filepath2 + "\\" + alphabet.get(i) + "\\";
            str = "";
            if (i < 15) {
                currentFileName += "HARF_" + alphabet.get(i) + ".xml";
            } else {
                currentFileName += "liste3_" + alphabet.get(i) + ".txt";
            }
            InputStream bytes = new FileInputStream(currentFileName);
            Reader chars = new InputStreamReader(bytes);
            read = new BufferedReader(chars);
            while ((str = read.readLine()) != null) {

                if (str.contains("<name>")){
                    String afterNameTag = str.substring(str.indexOf("<name>")+7, str.indexOf("</name>")).trim();
                    if(!afterNameTag.contains(" ")){
                        System.out.print("."+afterNameTag+" ");
                        while ((str = read.readLine()) != null && !str.contains("<lex_class>")) {

                        }
                        if(str == null){
                            str = "";
                        }
                        String lexClass = str.substring(str.indexOf("<lex_class>")+11, str.indexOf("</lex_class>")).trim();
                        int wordType = 0;
                        if (lexClass.contains("isim")){
                            wordType = 0;
                        }
                        else if (lexClass.contains("fiil")){
                            wordType=1;
                        }
                        else if (lexClass.contains("sıfat")){
                            wordType=2;
                        }
                        else if (lexClass.contains("zarf")){
                            wordType=3;
                        }

                        Word word = new Word(afterNameTag,wordType);
                        turkish.add(word);

                    }

                }

/*
                count++;
                Scanner scanWord = new Scanner(str);
                if(str.split(" ").length < 2){


                str = scanWord.next().toLowerCase();
                scanWord.close();
                if (str.contains("þ") || str.contains("ý") || str.contains("ð")) {
                    str = str.replace('þ', 'ş');
                    str = str.replace('ý', 'ı');
                    str = str.replace('ð', 'ğ');
                }
                addTurkish(str);

                }*/
            }
            read.close();
        }
        System.out.println(count);
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
