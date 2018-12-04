package com.lukyanov.giev.algorithm;

import java.util.List;

import com.google.common.base.Objects;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class Individ {
   List<Chromosome> chromosomes;

   public Double executeFunctionValue() {
      Chromosome prevChromosome = chromosomes.get(0);

      double distance = 0;
      for (int i = 1; i < chromosomes.size(); i++) {
         Chromosome chromosome = chromosomes.get(i);
         distance += sqrt(pow(chromosome.xValue - prevChromosome.xValue, 2) + pow(chromosome.yValue - prevChromosome.yValue, 2));

         prevChromosome = chromosome;
      }

      // TODO should we add last distance;
      distance += sqrt(pow(chromosomes.get(0).xValue - chromosomes.get(chromosomes.size() - 1).xValue, 2) + pow(
            chromosomes.get(0).yValue - chromosomes.get(chromosomes.size() - 1).yValue, 2));

      return distance;
   }



}
