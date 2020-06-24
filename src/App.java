import java.io.File;  
import java.io.FileNotFoundException;  
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        try {
            File inputfile = new File("inputfile.txt");
            System.out.println(new File(".").getAbsolutePath());
            Scanner reader = new Scanner(inputfile);
            while (reader.hasNextLine()) {
              String data = reader.nextLine();
              System.out.println(data);
            }
            reader.close();
          } catch (FileNotFoundException e) {
            System.out.println("Error occured when reading a file");
            e.printStackTrace();
          }
        
    }
}


