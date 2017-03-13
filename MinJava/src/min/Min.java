/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package min;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author student
 */
public class Min {

    private static final int innerCount = 100000000;//ilość porównań, zalecana bardzo duża, 10milionów do 1miliarda
    private static final int outerCount = 1;//może zostać 1 i tak ma mały sens
    private static final int testCount = 100;//ilość powtórzeń testu, aby uzyskać jakąś statystykę 100 jest w miarę przyzwoite, 1000  byłoby lepsze, ale też chodzi o czas

    public static int[] values = new int[innerCount];
    private static int[] randomTab = new int[innerCount];
    private static Random random = new Random();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here

                generateValues();
                
                PrintWriter pw = new PrintWriter("our_test_results.txt");
                for (int i = 0; i < testCount; i++) {
                    TestOurImplemenation(pw);
                }
                pw.close();

                PrintWriter pw2 = new PrintWriter("native_test_results.txt");
                for (int i = 0; i < testCount; i++) {
                    TestNativeImplementation(pw2);
                }
                pw2.close();
        
      /*  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int option = 3;

        System.out.println("Wybierz opcje. 1-generacja danych. 2-eksport danych do C/C++. 3-test wydajności Javy:");
        try {
            option = Integer.parseInt(br.readLine());
        } catch (NumberFormatException nfe) {
            System.err.println("Invalid Format!");
        }

        switch (option) {
            case 1:
                generaterandomValuesToFile();
                break;

            case 2:
                loadValues();
                exportValuesToCpp();
                break;

            default:
                loadValues();
                PrintWriter pw = new PrintWriter("our_test_results.txt");
                for (int i = 0; i < testCount; i++) {
                    TestOurImplemenation(pw);
                }
                pw.close();

                PrintWriter pw2 = new PrintWriter("native_test_results.txt");
                for (int i = 0; i < testCount; i++) {
                    TestNativeImplementation(pw2);
                }
                pw2.close();
                break;
        }*/
    }

    public static void generaterandomValuesToFile() throws FileNotFoundException {

        Random rnd = new Random();

        for (int i = 0; i < innerCount; i++) {
            values[i] = rnd.nextInt();
            //pw.write(",");
        }

        try {
            FileOutputStream fileOut
                    = new FileOutputStream("values.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(values);
            out.close();
            fileOut.close();

            /* PrintWriter pw = new PrintWriter("values.txt");
             // fop.write(@"public static int[] values = {");
             pw.write("public static int[] values = {");
            
             Random rnd = new Random();
            
             for(int i=0;i<innerCount;i++)
             {
             pw.write(Integer.toString(rnd.nextInt()));
             pw.write(",");
             }
             pw.write("};");
             pw.close();*/
        } catch (IOException ex) {
            Logger.getLogger(Min.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    public static int min(int a, int b) {
        if (a <= b) {
            return a;
        } else {
            return b;
        }
    }

    private static void TestNativeImplementation(PrintWriter pw) {

        int jTest = 0;

        //1. pomiar czasu ile sama petla trwa
        long startTime1 = System.nanoTime();
        for (int j = 0; j < outerCount; j++) {
            for (int i = 0; i < innerCount - 1; i++) {
                jTest++;
                //randomTab[i] = random.nextInt();
                randomTab[i]+=jTest;
            }
        }
        long endTime1 = System.nanoTime();

        //  System.out.println(jTest);
        int jTest2 = 0;

        long startTime2 = System.nanoTime();
        //2. pomiar czasu ile petla z funkcja trwa
        for (int j = 0; j < outerCount; j++) {
            for (int i = 0; i < innerCount - 1; i++) {
                jTest2++;

                //randomTab[i] = random.nextInt();
                randomTab[i]+=Math.min(values[i], values[i + 1]);
            }
        }
        long endTime2 = System.nanoTime();

        long resultTime = Math.abs(endTime2 - startTime2) - Math.abs(endTime1 - startTime1);

        pw.write(Long.toString(resultTime) + '\n');
        // System.out.println(jTest2);
        System.out.println(randomTab[random.nextInt(innerCount)]);

    }

    private static void TestOurImplemenation(PrintWriter pw) throws FileNotFoundException {
        int jTest = 0;

        //1. pomiar czasu ile sama petla trwa
        long startTime1 = System.nanoTime();
        for (int j = 0; j < outerCount; j++) {
            for (int i = 0; i < innerCount - 1; i++) {
                jTest++;

               // randomTab[i] = random.nextInt();
                randomTab[i]+=jTest;
            }

        }
        long endTime1 = System.nanoTime();

        //  System.out.println(jTest);
        int jTest2 = 0;

        long startTime2 = System.nanoTime();
        //2. pomiar czasu ile petla z funkcja trwa
        for (int j = 0; j < outerCount; j++) {
            for (int i = 0; i < innerCount - 1; i++) {
                jTest2++;

                //randomTab[i] = random.nextInt();
                randomTab[i]+=min(values[i], values[i + 1]);
            }
        }
        long endTime2 = System.nanoTime();

        long resultTime = Math.abs(endTime2 - startTime2) - Math.abs(endTime1 - startTime1);

        pw.write(Long.toString(resultTime) + '\n');
        // System.out.println(jTest2);
        System.out.println(randomTab[random.nextInt(innerCount)]);
    }

    private static void loadValues() {

        try {
            FileInputStream fileIn = new FileInputStream("values.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            values = (int[]) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException ex) {
            Logger.getLogger(Min.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Min.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void exportValuesToCpp() {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter("values_cpp.txt");
            pw.write("int[] values = {");
            for (int i = 0; i < innerCount; i++) {
                pw.write(Integer.toString(values[i]));
                if (i < innerCount - 1) {
                    pw.write(",");
                }
            }
            pw.write("};");
            pw.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Min.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            pw.close();
        }
    }

    private static void generateValues() {
        for (int i = 0; i < innerCount; i++) {
            values[i] = random.nextInt();
        }
    }

}
