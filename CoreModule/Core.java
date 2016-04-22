import TurkishDictParser.Word;
import TurkishDictParser.YapimEki;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by Atakan Arıkan on 22.04.2016.
 */
@SuppressWarnings("Duplicates")
public class Core {
    public static ArrayList<YapımEki> yapımEkleri;
    public static ArrayList<CekimEki> cekimEkleri;
    public static HashSet<Word> turkish;

    public static void main(String[] args) throws IOException {
        yapımEkleri = new ArrayList<>();
        cekimEkleri = new ArrayList<>();
        turkish = new HashSet<>();
        readSuffixes("yapımekleri.txt", "çekimekleri.txt", "TurkishRoots.txt");
        Scanner scan = new Scanner(System.in);
        String input = "";
        while(!input.equals("q")) {
            input = scan.next();
            testWords(input);
        }

    }

    private static void testWords(String input) {

        for (int i = 1; i < input.length(); i++) {
            if (turkish.contains(new Word(input.substring(0, i), ""))) {
                ArrayList<WordDetail> w1 = isEklerProducable(input.substring(i), new Word(input.substring(0, i), ""),input.substring(i));
                System.out.println();
            }
        }
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

    public static ArrayList<WordDetail> isEklerProducable(String ekler, Word w, String initial) {
        ArrayList<WordDetail> resultWordDetails = new ArrayList<WordDetail>();
        Boolean found = false;
        if (cekimEkleri.contains(new CekimEki(ekler, ""))) {
            ArrayList<Ek> eks = new ArrayList<>();
            eks.add(new CekimEki(ekler, ""));

            resultWordDetails.add(new WordDetail(w.getContent(), eks));
            found = true;
        }
        if (yapımEkleri.contains(new YapımEki(ekler, "", ""))) {
            ArrayList<Ek> eks = new ArrayList<>();
            eks.add(new YapımEki(ekler, "", ""));

            resultWordDetails.add(new WordDetail(w.getContent(), eks));
            found = true;
        }
        if (found) return resultWordDetails;
        for (int i = 1; i < ekler.length(); i++) {
            String candidate1 = ekler.substring(0, i);
            String candidate2 = ekler.substring(i);
            ArrayList<WordDetail> w1 = isEklerProducable(candidate1, w, initial);
            ArrayList<WordDetail> w2 = isEklerProducable(candidate2, w,initial);
            if (w1 != null && w2 != null) {
                for (WordDetail wd:
                        w1) {
                    for (WordDetail wdIn:
                            w2) {

                       WordDetail listElement = new WordDetail(w.getContent(), null);
                        listElement.setEkler(wd.getEkler());
                        listElement.getEkler().addAll(wdIn.getEkler());
                        String tot = "";
                        for (Ek e:listElement.getEkler()) {
                            tot +=e.getEk();
                        }
                        if(tot.length() <= initial.length())
                        resultWordDetails.add(listElement);
                    }
                }
                return resultWordDetails;
            }
        }

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
            }
        }
        read.close();
        read = new BufferedReader(new FileReader(new File(filepath3)));
        while ((str = read.readLine()) != null) {
            String[] temp2 = str.split(" ");
            String kök = temp2[0].trim();
            String type = temp2[1].trim();
            Word myWord = new Word(kök, type);
            turkish.add(myWord);
        }
        read.close();
    }
}
