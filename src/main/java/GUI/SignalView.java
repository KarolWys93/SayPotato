package GUI;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;

public class SignalView {

    private XYDataset dataSet;
    private XYSeries data;
    private JFreeChart chart;
    private XYPlot plot;

    public SignalView() {
        XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
        renderer.setSeriesPaint(0, Color.blue);
        ValueAxis domainAxis = new NumberAxis("time [s]");
        domainAxis.setLowerMargin(0.0);
        domainAxis.setUpperMargin(0.02);

        //domainAxis.setFixedAutoRange(5);
        domainAxis.setAutoRange(true);

        ValueAxis rangeAxis = new NumberAxis("signal");
        plot = new XYPlot(dataSet, domainAxis, rangeAxis, renderer);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setDomainPannable(true);

        chart = new JFreeChart(plot);
        chart.removeLegend();
        chart.setAntiAlias(true);

    }

    public void setData(double[] signal, double samplingFrequency){
        data = new XYSeries("Audio signal");
        for (int i = 0; i < signal.length; i++) {
            data.add(i/samplingFrequency, signal[i]);
        }
        dataSet = new XYSeriesCollection(data);
        plot.setDataset(dataSet);
    }

    public void appendData(double[] signal, double samplingFrequency){
        double offset = data.getMaxX();

        for (int i = 0; i < signal.length; i++) {
            data.add((i/samplingFrequency)+offset, signal[i]);
        }
        dataSet = new XYSeriesCollection(data);
        plot.setDataset(dataSet);
    }

    public void setPointer(double position){
        plot.clearDomainMarkers();
        Marker marker = new ValueMarker(position);
        marker.setPaint(Color.RED);
        marker.setStroke(new BasicStroke(1.0f,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        plot.addDomainMarker(marker);
    }

    public JFreeChart getChart() {
        return chart;
    }

    public void clearPlots(){
        plot.clearDomainMarkers();
        plot.setDataset(null);
    }


}
