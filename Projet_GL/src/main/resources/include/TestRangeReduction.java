import java.lang.Math;



public class TestRangeReduction{
  public static void main(String args[]){

     //drawFunction(-30, 30, 10000);
    // drawFunction(1000000, 1000100, 10000);
      // drawError(-30, 30, 100000);
     drawError(1000000, 1000100, 100000);

    // drawErrorUlp(-30, 30, 100000);
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
        data[1][i] = MathForDeca.reduction3(x);
    }
    final ScatterPlot demo = new ScatterPlot("Custom reduction3", data);
    demo.show(demo);
  }

  public static void drawError(float a, float b, int nbValues){
    float[][] data = new float[2][nbValues];
    float step = (b-a) / (float) nbValues;
    for (int i = 0; i < data[0].length; i++) {
        float x = ( (float) i) * step + a;
        data[0][i] = x;
        data[1][i] = Math.abs(MathForDeca.reduction3(x) - (float) (x % (Math.PI/4)));
    }
    final ScatterPlot demo = new ScatterPlot("Absolute error", data);
    demo.show(demo);
  }

  public static void drawErrorUlp(float a, float b, int nbValues){
    float[][] data = new float[2][nbValues];
    float step = (b-a) / (float) nbValues;
    for (int i = 0; i < data[0].length; i++) {
        float x = ( (float) i) * step + a;
        data[0][i] = x;
        float trueValue = (float) (x % (Math.PI/4));
        data[1][i] = Math.abs(MathForDeca.reduction3(x) - trueValue)/Math.ulp(trueValue);
        System.out.println(data[1][i]);
    }
    final ScatterPlot demo = new ScatterPlot("Error in Ulp", data);
    demo.show(demo);
  }

  public static void displaySomeValues(float[] abscisses){
    System.out.println("Values java.lang.Math: ");
    for (int i = 0; i < abscisses.length; i++){
      System.out.print((float) (abscisses[i] % (Math.PI/4)));
      System.out.print('\t');
    }
    System.out.println();
    System.out.println("Values custom algorithm: ");
    for (int i = 0; i < abscisses.length; i++){
      System.out.print(MathForDeca.reduction3(abscisses[i]));
      System.out.print('\t');
    }
    System.out.println();
  }


}
