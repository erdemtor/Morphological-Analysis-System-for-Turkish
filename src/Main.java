
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("turkish words database.txt"));
        Scanner s = new Scanner(System.in);

        ArrayList<String> turkish = new ArrayList<>();
        while (scanner.hasNextLine()){
            String word = scanner.nextLine();
            word = word.split(" ")[0];
            turkish.add(word);
        }

        while (true){
            String result ="";

            System.out.println("Kelime Gir");
            String WORD = s.nextLine();

            String[] words = WORD.split(" ");
            String  finalResult="";
            for (int i = 0; i <words.length ; i++) {
                int min = Integer.MAX_VALUE;
                for (String x: turkish) {
                    int distance = minDistance(x,words[i].toLowerCase());
                    if(distance<min){
                        min = distance;
                        result = x;
                    }
                }
                ArrayList<String> mins = new ArrayList<>();
                for (String x: turkish) {
                    int distance = minDistance(x,words[i].toLowerCase());
                    if(distance == min && x.length()>words[i].length()){
                        mins.add(x);
                    }

                }
                for (String w:mins) {
                    result = w;
                }

                finalResult+=result+" ";

            }

            System.out.println(finalResult);




        }





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
}
