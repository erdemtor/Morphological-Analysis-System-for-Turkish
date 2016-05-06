import TurkishDictParser.Word;
import TurkishDictParser.YapimEki;
import sun.reflect.generics.tree.Tree;

import java.io.*;
import java.util.*;

/**
 * Created by Atakan Arıkan on 22.04.2016.
 */
@SuppressWarnings("Duplicates")
public class Core {
    public static ArrayList<YapımEki> yapımEkleri;
    public static ArrayList<CekimEki> cekimEkleri;
    public static HashSet<Word> turkishRoots;
    public static HashSet<Word> turkish;
    public static ArrayList<String> cekimEkleriStr = new ArrayList<>();
    public static ArrayList<String> yapimEkleriStr = new ArrayList<>();
    public static HashMap<Integer, String> alphabet;

    public static void main(String[] args) throws IOException {
        yapımEkleri = new ArrayList<>();
        cekimEkleri = new ArrayList<>();
        turkish = new HashSet<>();
        turkishRoots = new HashSet<>();
        alphabet = new HashMap<>();
        createAlphabet();
        readTurkish("D:\\NLP\\Sozluk");
        readSuffixes("yapımekleri.txt", "çekimekleri.txt", "TurkishRoots.txt");
        readMetuBankAndProcess("turkish_metu_sabanci_train.conll");
        Scanner scan = new Scanner(System.in);
        String input = "";
        while (!input.equals("q")) {
            input = scan.next();
            testWords(input);
            int x = 1;
        }

    }
    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException nfe) {}
        return false;
    }

    private static String tokenizeString(String potentialToken) {
        StringBuilder stringBuilder = new StringBuilder();
        if (potentialToken.length() > 0) { // parseDouble thinks "1980." a double. avoid that.
            if (!Character.isDigit(potentialToken.charAt(potentialToken.length() - 1)) && !Character.isLetter(potentialToken.charAt(potentialToken.length() - 1))) {
                potentialToken = potentialToken.substring(0, potentialToken.length() - 1);
            }
        }
        if(isDouble(potentialToken) && isDouble(potentialToken.replace(",","."))){

            return potentialToken;
        }
        else{
            for (int j = 0; j < potentialToken.length(); j++) {
                if (Character.isDigit(potentialToken.charAt(j)) || Character.isLetter(potentialToken.charAt(j))) {

                    stringBuilder.append(potentialToken.charAt(j));
                }
            }


        }
        return stringBuilder.toString();
    }
    private static void readMetuBankAndProcess(String filepath) throws IOException {
        BufferedReader read;
        String str;
        InputStream bytes = new FileInputStream(filepath);
        Reader chars = new InputStreamReader(bytes);
        read = new BufferedReader(chars);
        int cnt = 0;
        int correct = 0;
        while ((str = read.readLine()) != null) {
            if (str.contains("\t")) {
                String[] tokens = str.split("\t");
                if (tokens.length > 2 && !tokens[1].contains("_") && !tokens[2].contains("_") && !tokens[3].toLowerCase().equals("punc") && tokens[1].length() < 13) {
                    cnt++;
                    System.out.print("Results for " + tokens[1].toLowerCase() + " (" + tokens[2] + ") : " );
                    ArrayList<String> predictedRoots = testWords(tokenizeString(tokens[1]));
                    if (predictedRoots.get(predictedRoots.size()-1).equals(tokens[2])) {
                        correct++;
                    }
                    if (cnt == 5000) {
                        System.out.println("as");
                    }
                    System.out.println(predictedRoots.toString() + " " + correct + "/" + cnt);
                }
            }
        }
        System.out.println(correct + "/" + cnt + " = " + 100*correct/cnt +"%");
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

    public static void readTurkish(String filepath) throws IOException {
        BufferedReader read;
        String str;
        for (int i = 0; i < 28; i++) { //for each of the files
            String currentFileName = filepath + "\\" + alphabet.get(i) + "\\";
            currentFileName += "HARF_" + alphabet.get(i) + ".xml";

            InputStream bytes = new FileInputStream(currentFileName);
            Reader chars = new InputStreamReader(bytes);
            read = new BufferedReader(chars);
            while ((str = read.readLine()) != null) {

                if (str.contains("<name>")) {
                    String afterNameTag = str.substring(str.indexOf("<name>") + 7, str.indexOf("</name>")).trim();

                    if (!afterNameTag.contains(" ") || afterNameTag.contains("(I")) {
                        boolean isok = true;
                        afterNameTag = afterNameTag.split(" ")[0];
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
                            if (word.getContent().startsWith("yaba")) {
                                int a;
                            }
                            if (meaning_text.contains(" işi") && (word.getContent().endsWith("ma") || word.getContent().endsWith("me"))) {
                                word = new Word(afterNameTag.substring(0, afterNameTag.length() - 2).toLowerCase(), "fiil");
                            }
                            turkish.add(word);
                        }

                    }

                }
            }
        }
    }

    private static ArrayList<String> testWords(String input) {
        ArrayList<String> result = new ArrayList<>();
        boolean isAnyCekimEkiCombinationFound = false;
        for (int i = 1; i < input.length(); i++) {
            if (turkish.contains(new Word(input.substring(0, i), ""))) {
                ArrayList<String> answer = allProducable(input.substring(i), cekimEkleriStr); // if null it is not produce-able
                ArrayList<WordDetail> filtered = new ArrayList<>();
                if (answer != null) {
                    ArrayList<WordDetail> wds = markSuffixes(answer, input.substring(0, i), false, true);
                    filtered = filter(wds, false);
                } // 1
                if (filtered.size() != 0) {
                    checkYapimEkleri(input.substring(0, i), result);
                    isAnyCekimEkiCombinationFound = true;
                }
//                System.out.println("-----" +input.substring(0, i) + "------" );
            }
        }
        if (!isAnyCekimEkiCombinationFound) {
            checkYapimEkleri(input, result);
        }
        return result;
        //  System.out.println("bitti");
    }

    public static void checkYapimEkleri(String rootWithYapimEkleri, ArrayList<String> res) {
        // cekim eki kombinasyonu found at this point
        // possible root can be with yapim eks
        boolean anyYapimEkiCombinationFound = false;
        for (int j = 1; j < rootWithYapimEkleri.length(); j++) {
            String rootCandidateWithoutYapimEkis = rootWithYapimEkleri.substring(0, j);
            if (turkishRoots.contains(new Word(rootCandidateWithoutYapimEkis, ""))) {
                ArrayList<String> answer2 = allProducable(rootWithYapimEkleri.substring(j), yapimEkleriStr);
                ArrayList<WordDetail> filtered2 = new ArrayList<>();
                if (answer2 != null) {
                    ArrayList<WordDetail> wds2 = markSuffixes(answer2, rootCandidateWithoutYapimEkis, true, false);
                    filtered2 = filter(wds2, true);
                }
                if (filtered2.size() == 0) {

                } else {
                    res.add(rootCandidateWithoutYapimEkis);
                    anyYapimEkiCombinationFound = true;
                }
            }
        }
        if (!anyYapimEkiCombinationFound) {
            res.add(rootWithYapimEkleri);
        }
    }

    public static ArrayList<WordDetail> filter(ArrayList<WordDetail> wds, boolean isYapımEki) {
        ArrayList<WordDetail> filtered = new ArrayList<>();
        for (WordDetail wd : wds) {
            Boolean isOkay = true;
            ArrayList<Ek> ekler = wd.getEkler();
            String currentState = getWord(wd.getRoot()).getType();
            if (!isYapımEki && currentState.equals("sıfat")) {
                currentState = "isim";
            }
            for (Ek ek : ekler) {
                if (ek instanceof YapımEki) {
                    YapımEki yek = (YapımEki) ek;
                    if (!currentState.equals(yek.getFrom())) {
                        isOkay = false;
                        break;
                    } else {
                        currentState = yek.getTo();
                        if (!isYapımEki && currentState.equals("sıfat")) {
                            currentState = "isim";
                        }
                    }
                }
                if (ek instanceof CekimEki) {
                    CekimEki cek = (CekimEki) ek;
                    String from = cek.getFrom();
                    if (cek.getFrom().equals("sıfat")) {
                        from = "isim";
                    }
                    if (!currentState.equals(from)) {
                        isOkay = false;
                        break;
                    }
                }
            }
            if (isOkay) {
                filtered.add(wd);
            }
        }
        return filtered;
    }

    public static Word getWord(String content) {
        for (Word w : turkish) {
            if (w.getContent().equals(content))
                return w;

        }
        return null;
    }

    public static ArrayList<Ek> getYapımEkleri(String content) {
        ArrayList<Ek> result = new ArrayList<>();
        for (YapımEki ek :
                yapımEkleri) {
            if (content.equals(ek.getEk())) {
                result.add(ek);
            }
        }
        if (result.size() == 0) return null;
        return result;
    }

    public static ArrayList<Ek> getCekimEkleri(String content) {
        ArrayList<Ek> result = new ArrayList<>();
        for (CekimEki ek :
                cekimEkleri) {
            if (content.equals(ek.getEk())) {
                result.add(ek);
            }
        }
        if (result.size() == 0) return null;
        return result;
    }

    public static ArrayList<WordDetail> markSuffixes(ArrayList<String> outputs, String root, boolean isyapim, boolean iscekim) {
        ArrayList<WordDetail> res = new ArrayList<>();
        ArrayList<WordDetail> resAll = new ArrayList<>();
        ArrayList<WordDetail> trueRes = new ArrayList<>();
        String correctOutput = "";
        for (String possibleOutcome : outputs) {
            correctOutput = possibleOutcome.replaceAll("-", "");
            String[] eklerListesi = possibleOutcome.split("-");
            ArrayList<WordDetail> temp = new ArrayList<>();
            for (int i = 0; i < eklerListesi.length; i++) {
                String possEk = eklerListesi[i];
                ArrayList<Ek> yapEks = getYapımEkleri(possEk);
                ArrayList<Ek> cekEks = getCekimEkleri(possEk);
                temp = new ArrayList<>();
                if (yapEks != null && isyapim) {
                    for (int j = 0; j < yapEks.size(); j++) {
                        if (i == 0) {
                            ArrayList<Ek> asd = new ArrayList<>();
                            asd.add(yapEks.get(j));
                            WordDetail wd = new WordDetail(root, asd);
                            temp.add(wd);
                        } else {
                            for (WordDetail wdt : res) {
                                ArrayList<Ek> asd = (ArrayList<Ek>) wdt.getEkler().clone();
                                asd.add(yapEks.get(j));
                                WordDetail wd = new WordDetail(root, asd);

                                String ress = "";
                                for (int k = 0; k < wd.getEkler().size(); k++) {
                                    ress += wd.getEkler().get(k).getEk();
                                }
                                if (ress.length() < correctOutput.length() || ress.equals(correctOutput)) {
                                    temp.add(wd);
                                }
                            }
                        }
                    }
                }
                if (cekEks != null && iscekim) {
                    for (Ek e : cekEks) {
                        if (i == 0) {
                            ArrayList<Ek> asd = new ArrayList<>();
                            asd.add(e);
                            WordDetail wd = new WordDetail(root, asd);
                            temp.add(wd);
                        } else {
                            for (WordDetail wdt : res) {
                                ArrayList<Ek> asd = (ArrayList<Ek>) wdt.getEkler().clone();
                                asd.add(e);
                                WordDetail wd = new WordDetail(root, asd);
                                String ress = "";
                                for (int k = 0; k < wd.getEkler().size(); k++) {
                                    ress += wd.getEkler().get(k).getEk();
                                }
                                if (ress.length() < correctOutput.length() || ress.equals(correctOutput)) {
                                    if (ress.equals(correctOutput)) {
                                        if (wd.getEkler().size() * 2 <= correctOutput.length()) {
                                            temp.add(wd);
                                        }
                                    } else {
                                        temp.add(wd);
                                    }

                                }
                            }
                        }
                    }
                }
                res = temp;
            }
            resAll.addAll(res);

        }
        HashSet<WordDetail> tempos = new HashSet<>();
        for (WordDetail wd : resAll) {
            String ress = "";
            for (int i = 0; i < wd.getEkler().size(); i++) {
                ress += wd.getEkler().get(i).getEk();
            }
            if (ress.equals(correctOutput)) {
                tempos.add(wd);
            }
        }
        trueRes.addAll(tempos);
        return trueRes;
    }

    public static ArrayList<String> allProducable(String sequence, ArrayList<String> allEks) {
        ArrayList<String> allPoss = new ArrayList<>();
        String res = "";
        if (allEks.contains(sequence)) allPoss.add(sequence);
        for (int i = 1; i < sequence.length(); i++) {
            String left = sequence.substring(0, i);
            String right = sequence.substring(i);
            if (allEks.contains(left) && allEks.contains(right)) {
                allPoss.add(left + "-" + right);
            } else {
                ArrayList<String> leftPoss = allProducable(left, allEks);
                ArrayList<String> rightPoss = allProducable(right, allEks);
                if (leftPoss != null && rightPoss != null) {
                    for (String leftPos : leftPoss) {
                        for (String rightPos : rightPoss) {
                            allPoss.add(leftPos + "-" + rightPos);
                        }

                    }
                }
            }

        }
        TreeSet<String> temp = new TreeSet<>();
        for (String s : allPoss) {
            temp.add(s);
        }
        ArrayList<String> finalAllPos = new ArrayList<>();
        for (String s : temp) {
            finalAllPos.add(s);
        }
        if (finalAllPos.size() > 0) return finalAllPos;
        return null;
    }

    private static void readSuffixes(String filepath1, String filepath2, String filepath3) throws IOException {
        BufferedReader read = new BufferedReader(new FileReader(new File(filepath1)));
        String str;
        while ((str = read.readLine()) != null) {
            String[] temp2 = str.split(" \\* ");
            String[] ekler = temp2[0].trim().split(" ");
            String[] actions = temp2[1].trim().split(" ");
            for (int i = 0; i < ekler.length; i++) {
                YapımEki ek = new YapımEki("", "", "");
                ek.setFrom(actions[0]);
                ek.setTo(actions[1]);
                ek.setEk(ekler[i]);
                yapımEkleri.add(ek);
                yapimEkleriStr.add(ek.getEk());
            }
        }
        read.close();
        read = new BufferedReader(new FileReader(new File(filepath2)));
        while ((str = read.readLine()) != null) {
            String[] temp2 = str.split(" \\* ");
            String ek = temp2[0].trim();
            String[] types = temp2[1].trim().split(" ");
            for (String type :
                    types) {
                cekimEkleri.add(new CekimEki(ek, type));
                cekimEkleriStr.add(ek);
            }
        }
        read.close();
        read = new BufferedReader(new FileReader(new File(filepath3)));
        while ((str = read.readLine()) != null) {
            String[] temp2 = str.split(" ");
            String kök = temp2[0].trim();
            String type = temp2[1].trim();
            Word myWord = new Word(kök, type);
            turkishRoots.add(myWord);
        }
        read.close();
    }
}
