import java.awt.RenderingHints;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.FastScatterPlot;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/*

Utility for scatter Plot

 */
public class ScatterPlot extends ApplicationFrame {

    /** The data. */
    private float[][] data;

    /**
     * Creates a new fast scatter plot demo.
     *
     * @param title  the frame title.
     */
    public ScatterPlot(final String title, float[][] data) {

        super(title);
        this.data = data;
        final NumberAxis domainAxis = new NumberAxis("X");
        domainAxis.setAutoRangeIncludesZero(false);
        final NumberAxis rangeAxis = new NumberAxis("Y");
        rangeAxis.setAutoRangeIncludesZero(false);
        final ExtendedFastScatterPlot plot = new ExtendedFastScatterPlot(this.data, domainAxis, rangeAxis, 2);
        final JFreeChart chart = new JFreeChart(title, plot);
//        chart.setLegend(null);

        // force aliasing of the rendered content..
        chart.getRenderingHints().put
            (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final ChartPanel panel = new ChartPanel(chart, true);
        panel.setPreferredSize(new java.awt.Dimension(500, 270));
  //      panel.setHorizontalZoom(true);
    //    panel.setVerticalZoom(true);
        panel.setMinimumDrawHeight(10);
        panel.setMaximumDrawHeight(2000);
        panel.setMinimumDrawWidth(20);
        panel.setMaximumDrawWidth(2000);

        setContentPane(panel);

    }

    public static void show(ScatterPlot demo){
      demo.pack();
      RefineryUtilities.centerFrameOnScreen(demo);
      demo.setVisible(true);
    }


}
