package com.lukyanov.giev.algorithm;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

import static com.google.common.collect.ImmutableList.of;


class SimpleGiev3Test {

   @Test
   void crossOverInternal() {

      List<Chromosome> firstList = ImmutableList.<Chromosome> builder()
            .add(Chromosome.builder().id(1).build())
            .add(Chromosome.builder().id(2).build())
            .add(Chromosome.builder().id(3).build())
            .add(Chromosome.builder().id(4).build())
            .add(Chromosome.builder().id(5).build())
            .add(Chromosome.builder().id(6).build())
            .add(Chromosome.builder().id(7).build())
            .add(Chromosome.builder().id(8).build())
            .add(Chromosome.builder().id(9).build())
            .build();

      List<Chromosome> secondList = ImmutableList.<Chromosome> builder()
            .add(Chromosome.builder().id(4).build())
            .add(Chromosome.builder().id(5).build())
            .add(Chromosome.builder().id(2).build())
            .add(Chromosome.builder().id(1).build())
            .add(Chromosome.builder().id(8).build())
            .add(Chromosome.builder().id(7).build())
            .add(Chromosome.builder().id(6).build())
            .add(Chromosome.builder().id(9).build())
            .add(Chromosome.builder().id(3).build())
            .build();

      Pair<List<Chromosome>, List<Chromosome>> result = SimpleGiev3.crossOverInternal(firstList, secondList, 3, 7);

      Assertions.assertIterableEquals(result.getLeft().stream().map(c -> c.id).collect(Collectors.toList()),
            of(4, 2, 3, 1, 8, 7, 6, 5, 9));
      Assertions.assertIterableEquals(result.getRight().stream().map(c -> c.id).collect(Collectors.toList()),
            of(1, 8, 2, 4, 5, 6, 7, 9, 3));

   }

   @Test
   void crossOverInternalTest() {

      List<Chromosome> firstList = ImmutableList.<Chromosome> builder()
            .add(Chromosome.builder().id(1).build())
            .add(Chromosome.builder().id(2).build())
            .add(Chromosome.builder().id(3).build())
            .add(Chromosome.builder().id(4).build())
            .add(Chromosome.builder().id(5).build())
            .add(Chromosome.builder().id(6).build())
            .add(Chromosome.builder().id(7).build())
            .add(Chromosome.builder().id(8).build())
            .add(Chromosome.builder().id(9).build())
            .build();

      List<Chromosome> secondList = ImmutableList.<Chromosome> builder()
            .add(Chromosome.builder().id(4).build())
            .add(Chromosome.builder().id(5).build())
            .add(Chromosome.builder().id(2).build())
            .add(Chromosome.builder().id(1).build())
            .add(Chromosome.builder().id(8).build())
            .add(Chromosome.builder().id(6).build())
            .add(Chromosome.builder().id(7).build())
            .add(Chromosome.builder().id(9).build())
            .add(Chromosome.builder().id(3).build())
            .build();

      Pair<List<Chromosome>, List<Chromosome>> result = SimpleGiev3.crossOverInternal(firstList, secondList, 3, 8);

      Assertions.assertIterableEquals(result.getLeft().stream().map(c -> c.id).collect(Collectors.toList()),
            of(4, 2, 3, 1, 8, 6, 7, 9, 5));
      Assertions.assertIterableEquals(result.getRight().stream().map(c -> c.id).collect(Collectors.toList()),
            of(1, 9, 2, 4, 5, 6, 7, 8, 3));

   }
}