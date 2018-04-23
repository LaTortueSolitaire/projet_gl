import java.lang.Math;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class TestMathForDeca{
  public static void main(String args[]){

    /*Test power*/
    System.out.println("\nTest Power");
    float x1 = MathForDeca.pow(48.859f, 5);
    float x2 = MathForDeca.pow(48.859f, -5);
    float x3 = MathForDeca.pow(48.859f, 0);
    System.out.println("Value custom algorithm:");
    System.out.println(String.format("%f, %f, %f", x1, x2, x3));

    float y1 = (float) Math.pow(48.859f, 5);
    float y2 = (float) Math.pow(48.859f, -5);
    float y3 = (float) Math.pow(48.859f, 0);

    System.out.println("Value standard algorithm:");
    System.out.println(String.format("%f, %f, %f\n", y1, y2, y3));

    /* Test sin Taylor */
    System.out.println("Test sin Taylor");
    x1 = MathForDeca.sin(2.86f);
    x2 = MathForDeca.sin(-2.86f);
    x3 = MathForDeca.sin(5000f);
    System.out.println("Value custom algorithm:");
    System.out.println(String.format("%f, %f, %f", x1, x2, x3));

    y1 = (float) Math.sin(2.86f);
    y2 = (float) Math.sin(-2.86f);
    y3 = (float) Math.sin(50000f);

    System.out.println("Value standard algorithm:");
    System.out.println(String.format("%f, %f, %f\n", y1, y2, y3));

    /* Plot des courbes pour visualiser la différence du sin*/
    // List<Float> scores = new ArrayList<>();
    // List<Float> scores2 = new ArrayList<>();
    // List<Float> abscisse = new ArrayList<>();
    // float tmp = 0;
    // for (int i = 0; i < 10000; i++) {
    //     tmp = MathForDeca.abs((float)Math.sin(i*0.0001f) - MathForDeca.sin(i*0.0001f));
    //     System.out.println(tmp / MathForDeca.ulp((float)Math.sin(i*0.0001f))); // A combien d'ulp on est de la vrai valeur
    //     scores.add(tmp / MathForDeca.ulp((float)Math.sin(i*0.0001f)));
    //     abscisse.add(i*0.0001f);
    // }
    // GraphPanel mainPanel = new GraphPanel(scores, scores2, abscisse);
    // mainPanel.show(mainPanel);

    /******************** test cosHorner *****************/
    List<Float> scores = new ArrayList<>();
    List<Float> scores2 = new ArrayList<>();
    List<Float> abscisse = new ArrayList<>();
    float tmp = 0;
    float tmp2 = 0;
    for (int i = 0; i < 10000; i = i + 100) {
        tmp = MathForDeca.abs((float)Math.cos(i*0.0001f) - MathForDeca.cos(i*0.0001f));
        System.out.println(tmp / MathForDeca.ulp((float)Math.cos(i*0.0001f))); // A combien d'ulp on est de la vrai valeur
        scores.add(tmp / MathForDeca.ulp((float)Math.cos(i*0.0001f)));
        tmp2 += tmp / MathForDeca.ulp((float)Math.cos(i*0.0001f));
        abscisse.add(i*0.0001f);
    }
    System.out.println(tmp2);
    GraphPanel mainPanel = new GraphPanel(scores, scores2, abscisse);
    mainPanel.show(mainPanel);

    /* Test cos Taylor */
    System.out.println("Test cos Taylor");
    x1 = MathForDeca.cos(2.86f);
    x2 = MathForDeca.cos(-2.86f);
    x3 = MathForDeca.cos(50000f);
    System.out.println("Value custom algorithm:");
    System.out.println(String.format("%f, %f, %f", x1, x2, x3));

    y1 = (float) Math.cos(2.86f);
    y2 = (float) Math.cos(-2.86f);
    y3 = (float) Math.cos(50000f);

    System.out.println("Value standard algorithm:");
    System.out.println(String.format("%f, %f, %f\n", y1, y2, y3));

    /* Plot des courbes pour visualiser la différence du cos*/
    // List<Float> scores = new ArrayList<>();
    // List<Float> scores2 = new ArrayList<>();
    // List<Float> abscisse = new ArrayList<>();
    // for (int i = 0; i < 1200; i++) {
    //     scores.add((float)Math.cos(i*0.0125f));
    //     scores2.add((float)MathForDeca.cos(i*0.0125f));
    //     abscisse.add(i*0.0125f);
    // }
    // GraphPanel mainPanel = new GraphPanel(scores, scores2, abscisse);
    // mainPanel.show(mainPanel);

    /* Plot L'erreur entre math.cos et mathfordeca.cos*/
    // List<Float> scores = new ArrayList<>();
    // List<Float> scores2 = new ArrayList<>();
    // List<Float> abscisse = new ArrayList<>();
    // float tmp = 0;
    // for (int i = 0; i < 24000; i = i +1) {
    //     tmp = MathForDeca.abs((float)Math.cos(i*0.5f) - MathForDeca.cos(i*0.5f));
    //     scores.add(tmp);
    //     System.out.println(tmp / MathForDeca.ulp(tmp));
    //     abscisse.add(i*0.5f);
    // }
    // GraphPanel mainPanel = new GraphPanel(scores, scores2, abscisse);
    // mainPanel.show(mainPanel);

    /* Test SQRT */

    System.out.println("Test sqrt Babylonian method");
    x1 = MathForDeca.sqrt(0.05f);
    x2 = MathForDeca.sqrt(0.1f);
    x3 = MathForDeca.sqrt(0.15f);
    System.out.println("Value custom algorithm:");
    System.out.println(String.format("%f, %f, %f", x1, x2, x3));

    y1 = (float) Math.sqrt(0.05f);
    y2 = (float) Math.sqrt(0.1f);
    y3 = (float) Math.sqrt(0.15f);

    System.out.println("Value standard algorithm:");
    System.out.println(String.format("%f, %f, %f\n", y1, y2, y3));

    System.out.println(Float.MIN_VALUE);
    System.out.println(Float.MAX_VALUE);
    System.out.println(Math.pow(2.0f,-149));
    System.out.println(Math.pow(2.0f,128));

    /* Plot des courbes pour visualiser la différence de sqrt*/
    // List<Float> scores = new ArrayList<>();
    // List<Float> scores2 = new ArrayList<>();
    // List<Float> abscisse = new ArrayList<>();
    // for (int i = 0; i < 1200; i++) {
    //     scores.add((float)Math.sqrt(i*0.0125f));
    //     scores2.add((float)MathForDeca.sqrt(i*0.0125f));
    //     abscisse.add(i*0.05f);
    // }
    // GraphPanel mainPanel = new GraphPanel(scores, scores2, abscisse);
    // mainPanel.show(mainPanel);



    /******************* Test ulp ************************/

    System.out.println("Test ulp");
    x1 = MathForDeca.ulp(0.05f);
    x2 = MathForDeca.ulp(0.1f);
    x3 = MathForDeca.ulp(2.5f);
    System.out.println("Value custom algorithm:");
    System.out.println(String.format("%f, %f, %f", x1, x2, x3));

    y1 = (float) Math.ulp(0.05f);
    y2 = (float) Math.ulp(0.1f);
    y3 = (float) Math.ulp(2.5f);

    System.out.println("Value standard algorithm:");
    System.out.println(String.format("%f, %f, %f\n", y1, y2, y3));


    /* Plot des courbes pour visualiser la différence de ulp*/
    // List<Float> scores = new ArrayList<>();
    // List<Float> scores2 = new ArrayList<>();
    // List<Float> abscisse = new ArrayList<>();
    // float tmp = 0;
    // for (int i = 0; i < 300; i++) {
    //     tmp = MathForDeca.abs((float)Math.ulp(i*0.0125f) - MathForDeca.ulp(i*0.0125f));
    //     System.out.println(tmp / MathForDeca.ulp(tmp));
    //     scores.add(tmp);
    //     abscisse.add(i*0.0125f);
    // }
    // GraphPanel mainPanel = new GraphPanel(scores, scores2, abscisse);
    // mainPanel.show(mainPanel);

    /************ Test arctan  ************/
    System.out.println("Test arctan");

    /* Plot des courbes pour visualiser la différence de atan*/
    // List<Float> scores = new ArrayList<>();
    // List<Float> scores2 = new ArrayList<>();
    // List<Float> abscisse = new ArrayList<>();
    // for (int i = -5000; i < 5000; i++) {
    //     scores.add(MathForDeca.atan(i*0.001f));
    //     abscisse.add(i*0.001f);
    // }
    // GraphPanel mainPanel = new GraphPanel(scores, scores2, abscisse);
    // mainPanel.show(mainPanel);

    /* Plot L'erreur entre math.atan et mathfordeca.atan*/
    // List<Float> scores = new ArrayList<>();
    // List<Float> scores2 = new ArrayList<>();
    // List<Float> abscisse = new ArrayList<>();
    // float tmp = 0;
    // float sum = 0;
    // for (int i = 0; i < 10000; i++) {
    //     tmp = Math.abs((float)Math.atan(i*0.0001f) - MathForDeca.atan(i*0.0001f));
    //     sum += tmp;
    //     System.out.println(tmp / MathForDeca.ulp((float)Math.atan(i*0.0001f))); // A combien d'ulp on est de la vrai valeur
    //     scores.add(tmp);
    //     abscisse.add(i*0.0001f);
    // }
    // GraphPanel mainPanel = new GraphPanel(scores, scores2, abscisse);
    // mainPanel.show(mainPanel);
  }

}
