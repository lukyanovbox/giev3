package com.lukyanov.giev;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.lukyanov.giev.graphics.SimpleGievGraphics;
import com.lukyanov.giev.util.CoordinatesExecutor;

//import com.lukyanov.giev.graphics.SimpleGievGraphics;


public class Application {

   private static final String TITLE = "traveling salesman";
   private static final int FROM = -100;
   private static final int TO = 100;

   private JButton runAlgorithm;
   private JPanel panelMain;
   private JTextField populationSize;
   private JTextField crossingOverP;
   private JTextField mutationP;
   private JTextField generationsCount;
   private JPanel chartPanelWrapper;
   private JLabel averageLabel;
   private JLabel minLabel;
   private JList minInTourList;
   private JList etalonList;
   private JLabel minFromFile;

   private XYSeriesCollection dataset;

   private SimpleGievGraphics simpleGievGraphics;


   public Application() {
      runAlgorithm.addActionListener(e -> {
         simpleGievGraphics
               .run(Integer.valueOf(generationsCount.getText()),
                     Integer.valueOf(populationSize.getText()),
                     Double.valueOf(crossingOverP.getText()),
                     Double.valueOf(mutationP.getText()),
                     FROM,
                     TO);
      });
   }


   public static void main(String[] args) throws InterruptedException {
      JFrame frame = new JFrame("Giev1");
      Application app = new Application();
      app.etalonList.setListData(CoordinatesExecutor.getEthalonIdList().toArray());
      app.minFromFile.setText(String.format("%.2f", CoordinatesExecutor.optimalPathIndivid().executeFunctionValue()));
      frame.setContentPane(app.panelMain);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.pack();
      frame.setVisible(true);

      while (true) {
         if (app.simpleGievGraphics.isState()) {
            int i = 0;
            for (XYSeries series : app.simpleGievGraphics.getSeriesList()) {
               if (app.dataset.getSeriesCount() > 0) {
                  app.dataset.removeSeries(0);
               }

               app.dataset.addSeries(series);

               app.averageLabel.setText(String.format("%.2f", app.simpleGievGraphics.getAverageList().get(i)));
               app.minLabel.setText(String.format("%.2f", app.simpleGievGraphics.getMinList().get(i)));
               app.minInTourList.setListData(app.simpleGievGraphics.getPathList().get(i).toArray());
               i++;
               Thread.sleep(100);
            }
            app.simpleGievGraphics.setState(false);
            app.simpleGievGraphics.setSeriesList(new ArrayList<>());
            app.simpleGievGraphics.setAverageList(new ArrayList<>());
            app.simpleGievGraphics.setMinList(new ArrayList<>());
            app.simpleGievGraphics.setPathList(new ArrayList<>());
         }
         else {
            Thread.sleep(1000);
         }
      }
   }

   private XYSeriesCollection createDataset() {
      XYSeriesCollection dataset = new XYSeriesCollection();

      XYSeries chromosomes = new XYSeries("chromosomes", false, true);
      //      chromosomes.add(3600, 2300);
      //      chromosomes.add(3100, 3300);
      //      chromosomes.add(4700, 5750);
      //      chromosomes.add(5400, 5750);
      //      chromosomes.add(5608, 7103);
      //      chromosomes.add(4493, 7102);
      //      chromosomes.add(3600, 6950);
      //      chromosomes.add(3100, 7250);
      //      chromosomes.add(4700, 8450);
      //      chromosomes.add(14200, 5750);
      //      chromosomes.add(16150, 2300);
      //      chromosomes.add(14900, 1600);
      //      chromosomes.add(19800, 800);
      //      chromosomes.add(19800, 10000);
      //      chromosomes.add(19800, 11900);
      //      chromosomes.add(15110, 10053);
      //      chromosomes.add(13992, 10052);
      //      chromosomes.add(13100, 10800);
      //      chromosomes.add(12600, 10950);
      //      chromosomes.add(19800, 12200);
      //      chromosomes.add(200, 12200);
      //      chromosomes.add(5400, 8450);
      //      chromosomes.add(5610, 10053);
      //      chromosomes.add(4492, 10052);
      //      chromosomes.add(3600, 10800);
      //      chromosomes.add(3100, 10950);
      //      chromosomes.add(4700, 11650);
      //      chromosomes.add(5400, 11650);
      //      chromosomes.add(6650, 10800);
      //      chromosomes.add(7300, 10950);
      //      chromosomes.add(7300, 7250);
      //      chromosomes.add(6650, 6950);
      //      chromosomes.add(7300, 3300);
      //      chromosomes.add(6650, 2300);
      //      chromosomes.add(5400, 1600);
      //      chromosomes.add(8350, 2300);
      //      chromosomes.add(7850, 3300);
      //      chromosomes.add(9450, 5750);
      //      chromosomes.add(10150, 5750);
      //      chromosomes.add(10358, 7103);
      //      chromosomes.add(9243, 7102);
      //      chromosomes.add(8350, 6950);
      //      chromosomes.add(7850, 7250);
      //      chromosomes.add(14900, 8450);
      //      chromosomes.add(14200, 11650);
      //      chromosomes.add(14900, 11650);
      //      chromosomes.add(16150, 10800);
      //      chromosomes.add(9450, 8450);
      //      chromosomes.add(14900, 5750);
      //      chromosomes.add(15108, 7103);
      //      chromosomes.add(13993, 7102);
      //      chromosomes.add(13100, 6950);
      //      chromosomes.add(12600, 7250);
      //      chromosomes.add(14200, 8450);
      //      chromosomes.add(16800, 10950);
      //      chromosomes.add(16800, 7250);
      //      chromosomes.add(16150, 6950);
      //      chromosomes.add(16800, 3300);
      //      chromosomes.add(10150, 8450);
      //      chromosomes.add(10360, 10053);
      //      chromosomes.add(9242, 10052);
      //      chromosomes.add(8350, 10800);
      //      chromosomes.add(7850, 10950);
      //      chromosomes.add(9450, 11650);
      //      chromosomes.add(10150, 11650);
      //      chromosomes.add(11400, 10800);
      //      chromosomes.add(12050, 10950);
      //      chromosomes.add(12050, 7250);
      //      chromosomes.add(13100, 2300);
      //      chromosomes.add(12600, 3300);
      //      chromosomes.add(200, 1100);
      //      chromosomes.add(200, 800);
      //      chromosomes.add(11400, 6950);
      //      chromosomes.add(12050, 3300);
      //      chromosomes.add(11400, 2300);
      //      chromosomes.add(10150, 1600);


      dataset.addSeries(chromosomes);

      return dataset;
   }

   private JFreeChart createChart(XYDataset dataset) {

      JFreeChart chart = ChartFactory.createXYLineChart(
            TITLE,
            "x",
            "y",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
      );

      XYPlot plot = chart.getXYPlot();

      XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, true);
      renderer.setSeriesPaint(0, Color.RED);

      plot.setRenderer(renderer);
      plot.setBackgroundPaint(Color.white);

      plot.setRangeGridlinesVisible(true);
      plot.setRangeGridlinePaint(Color.BLACK);

      plot.setDomainGridlinesVisible(true);
      plot.setDomainGridlinePaint(Color.BLACK);

      chart.getLegend().setFrame(BlockBorder.NONE);

      chart.setTitle(new TextTitle(TITLE,
                  new Font("Serif", java.awt.Font.BOLD, 18)
            )
      );

      return chart;

   }

   private void createUIComponents() {
      dataset = createDataset();


      JFreeChart chart = createChart(dataset);

      ChartPanel chartPanel = new ChartPanel(chart);
      chartPanel.setChart(chart);
      chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
      chartPanel.setBackground(Color.white);
      chartPanel.setSize(300, 300);
      chartPanel.setRefreshBuffer(true);

      simpleGievGraphics = new SimpleGievGraphics(dataset, chartPanel);

      chartPanelWrapper = chartPanel;

      // TODO: place custom component creation code here
   }
}
