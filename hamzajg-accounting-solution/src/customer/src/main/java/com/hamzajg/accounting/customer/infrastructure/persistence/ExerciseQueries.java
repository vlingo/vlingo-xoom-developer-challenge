package com.hamzajg.accounting.customer.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.xoom.common.Completes;

import com.hamzajg.accounting.customer.infrastructure.ExerciseData;

public interface ExerciseQueries {
  Completes<ExerciseData> exerciseOf(String id);
  Completes<Collection<ExerciseData>> exercises();
}