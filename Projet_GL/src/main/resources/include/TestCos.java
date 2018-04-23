import java.lang.Math;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class TestCos{
  public static void main(String args[]){

     // drawFunction(-30, 30, 100000);
    // drawFunction(1000000, 1000100, 10000);
     drawError(-30, 30, 100000);
     drawError(1000000, 1000100, 100000);

     drawErrorUlp(-30, 30, 100000);
     // writeResToTextfile(-10, 10, 100000);
    // drawErrorUlp(1000000, 1000100, 10000);
     // float[] abscisses = {0.1f, 1.57f, 3.14f};
     // displaySomeValues(abscisses);

  }

  public static void drawFunction(float a, float b, int nbValues){
    float[][] data = new float[2][nbValues];
    float step = (b-a) / (float) nbValues;
    for (int i = 0; i < data[0].length; i++) {
        float x = ( (float) i) * step + a;
        data[0][i] = x;
        data[1][i] = MathForDeca.cos(x);
    }
    final ScatterPlot demo = new ScatterPlot("Custom Cosinus", data);
    demo.show(demo);
  }

  public static void drawError(float a, float b, int nbValues){
    float[][] data = new float[2][nbValues];
    float step = (b-a) / (float) nbValues;
    for (int i = 0; i < data[0].length; i++) {
        float x = ( (float) i) * step + a;
        data[0][i] = x;
        data[1][i] = Math.abs(MathForDeca.cos(x) - (float) Math.cos(x));
    }
    final ScatterPlot demo = new ScatterPlot("Absolute error", data);
    demo.show(demo);
  }

  public static void drawErrorUlp(float a, float b, int nbValues){
    float[][] data = new float[2][nbValues];
    float step = (b-a) / (float) nbValues;
    int compteur = 0;
    for (int i = 0; i < data[0].length; i++) {
        float x = ( (float) i) * step + a;
        data[0][i] = x;
        float trueValue = (float) Math.cos(x);
        data[1][i] = Math.abs(MathForDeca.cos(x) - trueValue)/Math.ulp(trueValue);
        if (data[1][i] < 2){ //  compte le nombre de fois ou on est a 1 ou 0 ulp
          compteur++;
        }
    }
    System.out.println( (float)  (compteur) / data[0].length);
    final ScatterPlot demo = new ScatterPlot("Error in Ulp", data);
    demo.show(demo);
  }

  public static void writeResToTextfile(float a, float b, int nbValues){
    try{
      PrintWriter writer = new PrintWriter("outFromCos.txt");
      PrintWriter writer2 = new PrintWriter("outAbscisses.txt");
      float step = (b-a) / (float) nbValues;
      writer.print("[");
      writer2.print("[");
      for (int i = 0; i < nbValues; i++){
        float x = ( (float) i) * step + a;
        writer.print(MathForDeca.cos(x));
        writer2.print(x);
        if( i != nbValues -1){
          writer.print(", ");
          writer2.print(", ");
        }
      }
      writer.print("]");
      writer2.print("]");
      writer.close();
      writer2.close();
    }catch(FileNotFoundException e){
      e.printStackTrace();
    }catch(SecurityException e) {
      e.printStackTrace();
    }

  }

  public static void displaySomeValues(float[] abscisses){
    System.out.println("Values java.lang.Math: ");
    for (int i = 0; i < abscisses.length; i++){
      System.out.print((float) Math.cos(abscisses[i]));
      System.out.print('\t');
    }
    System.out.println();
    System.out.println("Values custom algorithm: ");
    for (int i = 0; i < abscisses.length; i++){
      System.out.print(MathForDeca.cos(abscisses[i]));
      System.out.print('\t');
    }
    System.out.println();
  }


}
