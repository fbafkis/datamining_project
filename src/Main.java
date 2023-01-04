
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws Exception {
        int counter = 0;
        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\fb\\Desktop\\province_ca.csv"));
        List<String> lines = new ArrayList<>();
        String line = null;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }

        System.out.println(lines.get(1));

        String [] splitted = lines.get(1).split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");

        System.out.println(splitted[1]);



        //Create the user ids

        int [] userIds = new int[500];
        for (int i=0; i<500; i++){

            userIds[i]=i+1;
        }


    }
}


