import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

class ReadFromFileTest {
    public static void main(String[] args) {
        try {
            File file = new File("Commands.txt");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
            fr.close();
        }  catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }
    }
}


