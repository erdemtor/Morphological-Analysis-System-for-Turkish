
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    static ArrayList<HashMap<String, Integer>> turkissh = new ArrayList<>();
    static ArrayList<String> inputWords = new ArrayList<>();
    public static void main(String[] args) throws FileNotFoundException {
        readFromFile();
        readAndFillInput();
        System.out.println(correctMisspelling());
    }


    public static int minDistance(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();

        // len1+1, len2+1, because finally return dp[len1][len2]
        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }

        //iterate though, and check last char
        for (int i = 0; i < len1; i++) {
            char c1 = word1.charAt(i);
            for (int j = 0; j < len2; j++) {
                char c2 = word2.charAt(j);

                //if last two chars equal
                if (c1 == c2) {
                    //update dp value for +1 length
                    dp[i + 1][j + 1] = dp[i][j];
                } else {
                    int replace = dp[i][j] + 1;
                    int insert = dp[i][j + 1] + 1;
                    int delete = dp[i + 1][j] + 1;

                    int min = replace > insert ? insert : replace;
                    min = delete > min ? min : delete;
                    dp[i + 1][j + 1] = min;
                }
            }
        }

        return dp[len1][len2];
    }
    public static void readFromFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("turkish words database.txt"));
        Scanner s = new Scanner(System.in);

        for (int i = 0; i < 40 ; i++) {
            turkissh.add(new HashMap<>());
        }
        while (scanner.hasNextLine()){
            String word = scanner.nextLine();
            String[] wordAndFreq = word.split(" ");
            if(wordAndFreq[0].length()>2)
                turkissh.get(wordAndFreq[0].length() - 2).put(wordAndFreq[0],Integer.parseInt(wordAndFreq[1]));
        }
    }
    public static void readAndFillInput() throws FileNotFoundException {
        Scanner s = new Scanner(new File("input.txt"));

        while (s.hasNextLine()){
            String WORD = s.nextLine();
            String[] words = WORD.split(" ");
            for (String word:words) {
                inputWords.add(word);
            }
        }





    }
    public static String correctMisspelling(){
        String result ="";
        String  finalResult="";
        for (int i = 0; i <inputWords.size() ; i++) {
            //int min = Integer.MAX_VALUE;
            boolean cont = true;
            ArrayList<String> mins = new ArrayList<>();
            for (String x: turkissh.get(inputWords.get(i).length()-2).keySet()) {
                int distance = minDistance(x,inputWords.get(i).toLowerCase());
                if(distance== 1){
                    mins.add(x);
                }
                else{
                    if (distance == 0){
                        cont=false;
                        result = x;
                        break;
                    }
                }
            }
            if(cont){
                for (String x: turkissh.get(inputWords.get(i).length()-1).keySet()) {
                    int distance = minDistance(x,inputWords.get(i).toLowerCase());
                    if(distance== 1){
                        mins.add(x);
                    }
                    else{
                        if (distance == 0){
                            cont=false;
                            result = x;
                            break;
                        }
                    }
                }
                if(inputWords.get(i).length() > 2 && cont) {
                    for (String x: turkissh.get(inputWords.get(i).length()-3).keySet()) {
                        int distance = minDistance(x,inputWords.get(i).toLowerCase());
                        if(distance== 1){
                            mins.add(x);
                        }
                        else{
                            if (distance == 0){
                                cont=false;
                                result = x;
                                break;
                            }
                        }
                    }
                }
                int maxFreq = Integer.MIN_VALUE;
                result = inputWords.get(i);
                for (String w:mins) {
                    if(turkissh.get(w.length()-2).get(w) > maxFreq){
                        result = w;
                        maxFreq=turkissh.get(w.length()-2).get(w);
                    }
                }
            }

            finalResult+= result+" ";
        }
        return finalResult;
    }
}
