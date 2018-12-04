package com.lukyanov.giev.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import static com.google.common.base.Preconditions.checkState;
import static com.lukyanov.giev.util.CoordinatesExecutor.getCitiesCoords;
import lombok.Builder;
import lombok.Getter;


@Builder
public class SimpleGiev3 {

   private static final int ITEMS_COUNT = 20000;
   final int populationSize;
   final int generationCount;

   final double crossingOverP;
   final double mutationP;

   @Getter
   volatile int currentPopulationNumber = 0;
   List<Individ> population;


   final Supplier<Individ> individGenerator = () -> {
      List<Chromosome> citiesList = getCitiesCoords();
      Collections.shuffle(citiesList);
      return Individ.builder()
            .chromosomes(citiesList)
            .build();
   };

   public Double getMin() {
      return population.stream()
            .mapToDouble(Individ::executeFunctionValue)
            .min()
            .getAsDouble();
   }

   public Double getAverage() {
      return population.stream()
            .mapToDouble(Individ::executeFunctionValue)
            .average()
            .getAsDouble();
   }


   public boolean hasNext() {
      return currentPopulationNumber < generationCount;
   }


   public List<Chromosome> generateNextPopulation() {
      if (currentPopulationNumber == 0) {
         population = Stream.generate(individGenerator)
               .limit(populationSize)
               .collect(Collectors.toList());
      }
      else {
         population = evolutionPopulation(population);
      }
      currentPopulationNumber++;


      checkState(population.stream().allMatch(i -> i.chromosomes.stream().distinct().collect(Collectors.toList()).size() == 76));

      return population.stream()
            .min(Comparator.comparing(Individ::executeFunctionValue))
            .get()
            .chromosomes;
   }





   private List<Individ> evolutionPopulation(List<Individ> population) {
      checkState(population.size() == populationSize,
            String.format("Actual size is %s", population.size()));

      List<Individ> intermediatePopulation = generateIntermediatePopulation(population);

      checkState(intermediatePopulation.size() == populationSize,
            String.format("Actual size is %s", intermediatePopulation.size()));

      List<Individ> newPopulation = selectAndCrossingOver(intermediatePopulation);

      checkState(newPopulation.size() == populationSize,
            String.format("Actual size is %s", newPopulation.size()));

      return mutate(newPopulation);
   }

   private List<Individ> generateIntermediatePopulation(List<Individ> population) {
      final Double maxFValue = population.stream()
            .mapToDouble(Individ::executeFunctionValue)
            .max()
            .getAsDouble();

      final Double sumFcVal = population.stream()
            .distinct()
            .mapToDouble(Individ::executeFunctionValue)
            .sum();

      Map<Individ, Double> pIndividMap = population.stream()
            .distinct()
            .map(i -> Pair.of(i, ((1 + maxFValue - i.executeFunctionValue()) / sumFcVal)))
            .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));


      double var = 0;
      Map<Double, Individ> rouletteMap = new HashMap<>();

      for (Map.Entry<Individ, Double> entry : pIndividMap.entrySet()) {
         var += entry.getValue();
         rouletteMap.put(var, entry.getKey());
      }

      //      checkState(rouletteMap.values().size() == populationSize,
      //            String.format("Actual size is %s", rouletteMap.size()));


      List<Individ> intermediateIndividList = new ArrayList<>();
      for (int i = 0; i < population.size(); i++) {
         double rnd = RandomUtils.nextDouble(0,
               rouletteMap.keySet().stream().max(Comparator.comparing(Double::doubleValue)).get());
         for (Double key : rouletteMap.keySet().stream().sorted().collect(Collectors.toList())) {
            if (rnd < key) {
               intermediateIndividList.add(rouletteMap.get(key));
               break;
            }
         }
      }

      return intermediateIndividList;
   }


   private List<Individ> selectAndCrossingOver(List<Individ> intermediatePopulation) {
      List<Individ> newPopulation = new ArrayList<>();
      List<Individ> elit = intermediatePopulation.stream().sorted(Comparator.comparing(Individ::executeFunctionValue)).limit(
            populationSize / 10)
            .collect(Collectors.toList());
      for (int i = 0; i < (intermediatePopulation.size() - elit.size()) / 2; i++) {
         int firstItemIndex = RandomUtils.nextInt(0, intermediatePopulation.size());
         int secondItemIndex = firstItemIndex;
         while (secondItemIndex == firstItemIndex) {
            secondItemIndex = RandomUtils.nextInt(0, intermediatePopulation.size());
         }
         if (RandomUtils.nextDouble(0, 1) < crossingOverP) {
            newPopulation.addAll(
                  crossOver(intermediatePopulation.get(firstItemIndex), intermediatePopulation.get(secondItemIndex)));
         }
         else {
            newPopulation.add(
                  Individ.builder().chromosomes(new ArrayList<>(intermediatePopulation.get(firstItemIndex).chromosomes))
                        .build());
            newPopulation.add(
                  Individ.builder().chromosomes(new ArrayList<>(intermediatePopulation.get(secondItemIndex).chromosomes))
                        .build());
         }
      }

      if (intermediatePopulation.size() % 2 != 0) {
         int rndIndex = RandomUtils.nextInt(0, intermediatePopulation.size());

         newPopulation.add(
               Individ.builder().chromosomes(new ArrayList<>(intermediatePopulation.get(rndIndex).chromosomes)).build());
      }

      return ImmutableList.<Individ> builder()
            .addAll(elit)
            .addAll(newPopulation.stream()
                  .sorted(Comparator.comparing(Individ::executeFunctionValue))
                  .limit(populationSize - elit.size())
                  .collect(Collectors.toList()))
            .build();
   }


   protected List<Individ> crossOver(Individ first, Individ second) {
      int firstMark = RandomUtils.nextInt(1, first.chromosomes.size() - 1);
      int secondmark = RandomUtils.nextInt(firstMark + 1, first.chromosomes.size());


      Pair<List<Chromosome>, List<Chromosome>> pair = crossOverInternal(first.chromosomes, second.chromosomes, firstMark,
            secondmark);

      return ImmutableList.of(Individ.builder().chromosomes(pair.getLeft()).build(),
            Individ.builder().chromosomes(pair.getRight()).build());
   }


   protected static Pair<List<Chromosome>, List<Chromosome>> crossOverInternal(List<Chromosome> firstChs,
         List<Chromosome> secondChs,
         int firstMark,
         int secondMark) {
      Chromosome[] newFirstChs = new Chromosome[firstChs.size()];
      Chromosome[] newSecondChs = new Chromosome[secondChs.size()];

      List<Chromosome> newFirstSublist = new Vector<>(secondChs.subList(firstMark, secondMark));
      List<Chromosome> newSecondSublist = new Vector<>(firstChs.subList(firstMark, secondMark));


      for (int i = firstMark; i < firstMark + newFirstSublist.size(); i++) {
         newFirstChs[i] = newFirstSublist.get(i - firstMark);
         newSecondChs[i] = newSecondSublist.get(i - firstMark);
      }


      resolveNewChromosomeList(firstChs, secondChs, firstMark, secondMark, newFirstChs, newFirstSublist);
      resolveNewChromosomeList(secondChs, firstChs, firstMark, secondMark, newSecondChs, newSecondSublist);

      checkState(newFirstChs.length == firstChs.size());
      checkState(newSecondChs.length == secondChs.size());


      return Pair.of(Arrays.asList(newFirstChs), Arrays.asList(newSecondChs));
   }

   protected static void resolveNewChromosomeList(final List<Chromosome> parentList, final List<Chromosome> donorList,
         final int firstMark,
         final int secondMark, final Chromosome[] listToInsert, final List<Chromosome> newListSublist) {
      for (int i = 0; i < firstMark || (i >= secondMark && i < parentList.size()); ) {
         if (!newListSublist.contains(parentList.get(i))) {
            listToInsert[i] = parentList.get(i);
         }
         else {
            Chromosome duplicatedChromosome = parentList.get(i);
            do {
               int indexOfDuplicatedElement = donorList.indexOf(duplicatedChromosome);
               listToInsert[i] = parentList.get(indexOfDuplicatedElement);

               duplicatedChromosome = listToInsert[i];
            }
            while (newListSublist.contains(duplicatedChromosome));
         }

         // @formatter:off
         // exclude changed part of list
         if(i!=(firstMark - 1)){i++;}else{i=secondMark;}
         // @formatter:on
      }
   }




   private List<Individ> mutate(List<Individ> population) {
      for (Individ individ : population) {

         List<Chromosome> chromosomes = individ.chromosomes;
         double rnd = RandomUtils.nextDouble(0, 1);

         if (rnd < mutationP) {
            int rndFrstI = RandomUtils.nextInt(0, chromosomes.size() - 1);
            int rndSecI = rndFrstI;

            while (rndSecI == rndFrstI) {
               rndSecI = RandomUtils.nextInt(rndFrstI + 1, chromosomes.size());
            }
            List<Chromosome> sublist = chromosomes.subList(rndFrstI, rndSecI);

            sublist = Lists.reverse(sublist);

            individ.setChromosomes(
                  ImmutableList.<Chromosome> builder()
                        .addAll(chromosomes.subList(0, rndFrstI))
                        .addAll(sublist)
                        .addAll(chromosomes.subList(rndSecI, chromosomes.size()))
                        .build()
            );

            //            Chromosome chromosome1 = chromosomes.get(rndFrstI);
            //            Chromosome chromosome2 = chromosomes.get(rndSecI);
            //
            //
            //            chromosomes.set(rndFrstI, chromosome2);
            //            chromosomes.set(rndSecI, chromosome1);

         }
      }

      return population;
   }
}
