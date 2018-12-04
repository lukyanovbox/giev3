package com.lukyanov.giev.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.lukyanov.giev.algorithm.Chromosome;
import com.lukyanov.giev.algorithm.SimpleGiev3;

import lombok.Getter;
import lombok.Setter;



public class SimpleGievGraphics {

   private XYSeriesCollection dataset;
   private ChartPanel chartPanel;

   @Getter
   @Setter
   private boolean state = false;

   @Getter
   @Setter
   private List<XYSeries> seriesList = new ArrayList<>();

   @Getter
   @Setter
   private List<Double> averageList = new ArrayList<>();

   @Getter
   @Setter
   private List<Double> minList = new ArrayList<>();


   @Getter
   @Setter
   private List<List<Integer>> pathList = new ArrayList<>();


   public SimpleGievGraphics(XYSeriesCollection dataset, final ChartPanel chartPanel) {
      this.dataset = dataset;
      this.chartPanel = chartPanel;
   }

   public void run(final int generationCount, final int populationSize, final double crossingOverP,
         final double mutationP,
         final double from, final double to) {

      SimpleGiev3 simpleGiev = SimpleGiev3.builder()
            .generationCount(generationCount)
            .populationSize(populationSize)
            .crossingOverP(crossingOverP)
            .mutationP(mutationP)
            .build();


      while (simpleGiev.hasNext()) {
         List<Chromosome> population = simpleGiev.generateNextPopulation();

         XYSeries chromosomeSeries = new XYSeries("chromosomes~" + simpleGiev.getCurrentPopulationNumber(),false, true);


         for (Chromosome chromosome : population) {
            chromosomeSeries.add(chromosome.getXValue(), chromosome.getYValue());
         }


         seriesList.add(chromosomeSeries);
         averageList.add(simpleGiev.getAverage());
         minList.add(simpleGiev.getMin());
         pathList.add(population.stream().map(Chromosome::getId).collect(Collectors.toList()));
      }
      state = true;
   }
}
