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
    public static ArrayList<String> allEks = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        yapımEkleri = new ArrayList<>();
        cekimEkleri = new ArrayList<>();
        turkish = new HashSet<>();

        readSuffixes("yapımekleri.txt", "çekimekleri.txt", "TurkishRoots.txt");

        Scanner scan = new Scanner(System.in);
        String input = "";
        while (!input.equals("q")) {
            input = scan.next();
            testWords(input);
            int x = 1;
        }

    }

    private static void testWords(String input) {

        for (int i = 1; i < input.length(); i++) {
            if (turkish.contains(new Word(input.substring(0, i), ""))) {
                ArrayList<String> answer = allProducable(input.substring(i));
                ArrayList<WordDetail> wds = markSuffixes(answer, input.substring(0, i));
                ArrayList<WordDetail> filtered = filter(wds);

                System.out.println();
            }
        }
    }
    public static ArrayList<WordDetail> filter(ArrayList<WordDetail> wds){
        ArrayList<WordDetail> filtered = new ArrayList<>();
        for (WordDetail wd : wds) {
            Boolean isOkay = true;
            ArrayList<Ek> ekler = wd.getEkler();
            String currentState = getWord(wd.getRoot()).getType();
            for (Ek ek : ekler) {
                if (ek instanceof YapımEki) {
                    YapımEki yek = (YapımEki) ek;
                    if (!currentState.equals(yek.getFrom())) {
                        isOkay = false;
                        break;
                    } else {
                        currentState = yek.getTo();
                    }
                }
                if (ek instanceof CekimEki) {
                    CekimEki cek = (CekimEki) ek;
                    if (!currentState.equals(cek.getFrom())) {
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

    public static WordDetail isEklerProducable(String ekler, Word w, String initial) {

        if (cekimEkleri.contains(new CekimEki(ekler, ""))) {
            ArrayList<Ek> eks = new ArrayList<>();
            eks.add(new CekimEki(ekler, ""));

            return new WordDetail(w.getContent(), eks);

        }
        if (yapımEkleri.contains(new YapımEki(ekler, "", ""))) {
            ArrayList<Ek> eks = new ArrayList<>();
            eks.add(new YapımEki(ekler, "", ""));

            return (new WordDetail(w.getContent(), eks));

        }
        for (int i = 1; i < ekler.length(); i++) {
            String candidate1 = ekler.substring(0, i);
            String candidate2 = ekler.substring(i);
            WordDetail w1 = isEklerProducable(candidate1, w, initial);
            if (w1 != null) {
                WordDetail w2 = isEklerProducable(candidate2, w, initial);
                if (w1 != null && w2 != null) {
                    WordDetail listElement = new WordDetail(w.getContent(), null);
                    listElement.setEkler(w1.getEkler());
                    listElement.getEkler().addAll(w2.getEkler());
                    return listElement;
                }
            }

        }

        return null;
    }

    public static ArrayList<WordDetail> markSuffixes(ArrayList<String> outputs, String root) {
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
                if (yapEks != null) {
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
                                temp.add(wd);

                            }
                        }
                    }
                }
                if (cekEks != null) {
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
                                temp.add(wd);
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

    public static ArrayList<String> allProducable(String sequence) {
        ArrayList<String> allPoss = new ArrayList<>();
        String res = "";
        if (allEks.contains(sequence)) allPoss.add(sequence);
        for (int i = 1; i < sequence.length(); i++) {
            String left = sequence.substring(0, i);
            String right = sequence.substring(i);
            if (allEks.contains(left) && allEks.contains(right)) {
                allPoss.add(left + "-" + right);
            } else {
                ArrayList<String> leftPoss = allProducable(left);
                ArrayList<String> rightPoss = allProducable(right);
                if (leftPoss != null && rightPoss != null) {
                    for (String leftPos : leftPoss) {
                        for (String rightPos : rightPoss) {
                            allPoss.add(leftPos + "-" + rightPos);
                        }

                    }
                }
            }

        }
        if (allPoss.size() > 0) return allPoss;
        return null;
    }

    public static String producable(String sequence) {
        if (allEks.contains(sequence)) {
            return sequence;
        }
        for (int i = 1; i < sequence.length(); i++) {
            String temp1 = producable(sequence.substring(0, i));
            String temp2 = producable(sequence.substring(i));
            if (temp1 != null && temp2 != null) {
                return temp1 + "-" + temp2;
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
                allEks.add(ek.getEk());
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
                allEks.add(ek);
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
