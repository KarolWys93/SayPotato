package GUI;

import com.wyskocki.karol.dsp.Spectrum;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.*;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYZDataset;

import java.awt.*;
import java.util.ArrayList;

public class SpectrumView {

    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private NumberAxis scaleAxis;

    private PaintScale scale;
    private PaintScaleLegend legend;

    private XYZDataset dataset;
    private XYPlot plot;

    private JFreeChart chart;

    private XYBlockRenderer renderer;

    public SpectrumView(){
        xAxis = new NumberAxis("time [s]");
        xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        xAxis.setLowerMargin(0.0);
        xAxis.setUpperMargin(0.0);
        xAxis.setAxisLinePaint(Color.white);
        xAxis.setTickMarkPaint(Color.white);

        yAxis = new NumberAxis("frequency [Hz]");
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        yAxis.setLowerMargin(0.0);
        yAxis.setUpperMargin(0.0);
        yAxis.setAxisLinePaint(Color.white);
        yAxis.setTickMarkPaint(Color.white);

        renderer = new XYBlockRenderer();
        scale = new GrayPaintScale();//-2.0, 1.0);
        renderer.setPaintScale(scale);



        scaleAxis = new NumberAxis("Scale");
        scaleAxis.setAxisLinePaint(Color.white);
        scaleAxis.setTickMarkPaint(Color.white);
        scaleAxis.setTickLabelFont(new Font("Dialog", Font.PLAIN, 7));

        legend = new PaintScaleLegend(new GrayPaintScale(),
                scaleAxis);
        legend.setStripOutlineVisible(false);
        legend.setSubdivisionCount(20);
        legend.setAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        legend.setAxisOffset(5.0);
        legend.setMargin(new RectangleInsets(5, 5, 5, 5));
        //legend.setFrame(new BlockBorder(Color.red));
        legend.setPadding(new RectangleInsets(10, 10, 10, 10));
        legend.setStripWidth(10);
        legend.setPosition(RectangleEdge.LEFT);


        plot = new XYPlot(dataset, xAxis, yAxis, renderer);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5, 5, 5, 5));
        //plot.setOutlinePaint(Color.blue);

        chart = new JFreeChart(plot);
        chart.removeLegend();

        //legend.setBackgroundPaint(new Color(120, 120, 180));
        chart.addSubtitle(legend);

        //chart.setBackgroundPaint(new Color(180, 180, 250));
//        ChartUtilities.applyCurrentTheme(chart);
    }

    public void setSpectrum(ArrayList<Spectrum> spectrumData, double timeSpace){
        dataset = createDataSet(spectrumData, timeSpace);
        plot.setDataset(dataset);
        renderer.setBlockHeight(spectrumData.get(0).getFreqDelta());
    }

    public JFreeChart getChart(){
        return chart;
    }

    private XYZDataset createDataSet(final ArrayList<Spectrum> spectrumData, final double timeSpace){

        XYZDataset xyzDataset = new XYZDataset() {

            double spectrumDuration = timeSpace;
            int spectrumSize = spectrumData.get(0).getSpectrumData().length;
            int spectrumsCount = spectrumData.size();

            @Override
            public Number getZ(int i, int i1) {
                return new Double(getZValue(i, i1));
            }

            @Override
            public double getZValue(int i, int i1) {
                int spectrumNum = i1/spectrumSize;
                int sampleNum = i1%spectrumSize;
                return spectrumData.get(spectrumNum).getSpectrumData(sampleNum);
            }

            @Override
            public DomainOrder getDomainOrder() {
                return DomainOrder.ASCENDING;
            }

            @Override
            public int getItemCount(int i) {
                System.out.println("Spectogram length: "+ spectrumsCount * spectrumDuration + " s");
                return spectrumsCount*spectrumSize;
            }

            @Override
            public Number getX(int i, int i1) {
                return new Double(getXValue(i, i1));
            }

            @Override
            public double getXValue(int i, int i1) {
                int spectrumNum = i1/spectrumSize;
                return spectrumDuration*spectrumNum;
            }

            @Override
            public Number getY(int i, int i1) {
                return new Double(getYValue(i, i1));
            }

            @Override
            public double getYValue(int i, int i1) {
                int sampleNum = i1%spectrumSize;
                return spectrumData.get(0).getFrequency(sampleNum);
            }

            @Override
            public int getSeriesCount() {
                return 1;
            }

            @Override
            public Comparable getSeriesKey(int i) {
                return null;
            }

            @Override
            public int indexOf(Comparable comparable) {
                return 0;
            }

            @Override
            public void addChangeListener(DatasetChangeListener datasetChangeListener) {

            }

            @Override
            public void removeChangeListener(DatasetChangeListener datasetChangeListener) {

            }

            @Override
            public DatasetGroup getGroup() {
                return null;
            }

            @Override
            public void setGroup(DatasetGroup datasetGroup) {}
        };
        return xyzDataset;
    }
}
