import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.lang.Math;


public class GraphPanel extends JPanel {

    private int width = 600;
    private int heigth = 300;
    private int padding = 25;
    private int labelPadding = 25;
    private Color pointColor = new Color(84, 142, 250, 250);
    private Color pointColor2 = new Color(250, 60, 84, 250);
    private Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private int pointWidth = 2;
    private int numberYDivisions = 10;
    private int numberXDivisions = 20;
    private List<Float> scores;
    private List<Float> abscisse;
    private List<Float> scores2 = new ArrayList<Float>();

    public GraphPanel(List<Float> scores, List<Float> abscisse) {
        this.scores = scores;
        this.abscisse = abscisse;
    }

    public GraphPanel(List<Float> scores, List<Float> scores2, List<Float> abscisse) {
        this.scores = scores;
        this.abscisse = abscisse;
        this.scores2 = scores2;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        float xScale = ((float) getWidth() - (2 * padding) - labelPadding) / (getMaxAbscisse() - getMinAbscisse());
        float yScale = ((float) getHeight() - 2 * padding - labelPadding) / (getMaxScore() - getMinScore());

        List<Point> graphPoints = new ArrayList<>();
        for (int i = 0; i < scores.size(); i++) {
            int x1 = (int) (abscisse.get(i) * xScale + padding + labelPadding);
            int y1 = (int) ((getMaxScore() - scores.get(i)) * yScale + padding);
            graphPoints.add(new Point(x1, y1));
        }

        if (scores2.size() > 0){
          for (int i = 0; i < scores2.size(); i++) {
            int x1 = (int) (abscisse.get(i) * xScale + padding + labelPadding);
            int y1 = (int) ((getMaxScore() - scores2.get(i)) * yScale + padding);
            graphPoints.add(new Point(x1, y1));
          }
        }


        // draw white background
        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLACK);

        // create hatch marks and grid lines for y axis.
        for (int i = 0; i < numberYDivisions + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
            int y1 = y0;
            if (scores.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                g2.setColor(Color.BLACK);
                String yLabel = ( ((getMinScore() + (getMaxScore() - getMinScore()) * ((i * 1.0) / numberYDivisions)) * 100)) / 100.0 + "";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        // and for x axis
        for (int i = 0; i < numberXDivisions + 1; i++) {
            int x0 = i * (getWidth() - padding * 2 - labelPadding) / (numberXDivisions) + padding + labelPadding;
            int x1 = x0;
            int y0 = getHeight() - padding - labelPadding;
            int y1 = y0 - pointWidth;
            if (scores.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                g2.setColor(Color.BLACK);
                String xLabel = (((getMinAbscisse() + (getMaxAbscisse() - getMinAbscisse()) * ((i * 1.0) / numberXDivisions)) * 100)) / 100.0 + "";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(xLabel);
                g2.drawString(xLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) + 7);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        // create x and y axes
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);


        Stroke oldStroke = g2.getStroke();

        g2.setStroke(oldStroke);
        g2.setColor(pointColor);
        for (int i = 0; i < graphPoints.size()/2; i++) {
            int x = graphPoints.get(i).x - pointWidth / 2;
            int y = graphPoints.get(i).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);
        }

        if (scores2.size() != 0){
          g2.setColor(pointColor2);
        }
        for (int i = graphPoints.size()/2; i < graphPoints.size(); i++) {
            int x = graphPoints.get(i).x - pointWidth / 2;
            int y = graphPoints.get(i).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);
        }
    }

//    @Override
//    public Dimension getPreferredSize() {
//        return new Dimension(width, heigth);
//    }
    private float getMinScore() {
        float minScore = Float.MAX_VALUE;
        for (Float score : scores) {
            minScore = Math.min(minScore, score);
        }
        return minScore;
    }

    private float getMaxScore() {
        float maxScore = Float.MIN_VALUE;
        for (Float score : scores) {
            maxScore = Math.max(maxScore, score);
        }
        return maxScore;
    }

    private float getMinAbscisse() {
        float minScore = Float.MAX_VALUE;
        for (Float abscisse : abscisse) {
            minScore = Math.min(minScore, abscisse);
        }
        return minScore;
    }

    private float getMaxAbscisse() {
        float maxScore = Float.MIN_VALUE;
        for (Float abscisse : abscisse) {
            maxScore = Math.max(maxScore, abscisse);
        }
        return maxScore;
    }

    public void setScores(List<Float> scores) {
        this.scores = scores;
        invalidate();
        this.repaint();
    }

    public List<Float> getScores() {
        return scores;
    }

    private static void createAndShowGui(List<Float> scores, List<Float> abscisse) {
        GraphPanel mainPanel = new GraphPanel(scores, abscisse);
        mainPanel.setPreferredSize(new Dimension(800, 600));
        JFrame frame = new JFrame("DrawGraph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void show(GraphPanel mainPanel){
        mainPanel.setPreferredSize(new Dimension(800, 600));
        JFrame frame = new JFrame("DrawGraph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
