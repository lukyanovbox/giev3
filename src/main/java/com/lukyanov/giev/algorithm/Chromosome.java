package com.lukyanov.giev.algorithm;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class Chromosome {

   int id;

   int xValue;
   int yValue;

   @Override
   public boolean equals(Object o) {
      if (o == null) {
         return false;
      }
      if (!(o instanceof Chromosome)) {
         return false;
      }
      Chromosome c = (Chromosome) o;

      return this.id == c.id;
   }
}
