package com.hamzajg.accounting.employee.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.xoom.common.Completes;

import com.hamzajg.accounting.employee.infrastructure.EmployeeData;

public interface EmployeeQueries {
  Completes<EmployeeData> employeeOf(String id);
  Completes<Collection<EmployeeData>> employees();
}