import TurkishDictParser.YapimEki;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Atakan Arıkan on 22.04.2016.
 */
@SuppressWarnings("Duplicates")
public class Core {
    public static ArrayList<YapimEki> yapımEkleri;
    public static ArrayList<String> cekimEkleri;

    public static void main(String[] args) throws IOException {
        yapımEkleri = new ArrayList<>();
        cekimEkleri = new ArrayList<>();
        readSuffixes("yapımekleri.txt", "çekimekleri.txt");
        System.out.println(cekimEkleri.toString());

    }

    private static void readSuffixes(String filepath1, String filepath2) throws IOException {
        BufferedReader read = new BufferedReader(new FileReader(new File(filepath1)));
        String str;
        while ((str = read.readLine()) != null) {
            String[] temp2 = str.split(" \\* ");
            String[] ekler = temp2[0].trim().split(", ");
            String[] actions = temp2[1].trim().split(" ");
            for (int i = 0; i < ekler.length; i++) {
                YapimEki ek = new YapimEki();
                ek.setFrom(actions[0]);
                ek.setTo(actions[1]);
                ek.setEk(ekler[i]);
                yapımEkleri.add(ek);
            }
        }
        read.close();
        read = new BufferedReader(new FileReader(new File(filepath2)));
        while ((str = read.readLine()) != null) {
            cekimEkleri.add(str);
        }
        read.close();
    }
}
