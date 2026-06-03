package com.huuhv.mini_project.service;

import com.huuhv.mini_project.entity.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class EmployeeService {
    // List temporary data in RAM (it disappears when the application is deployed).
    private final List<Employee> employeeList = new ArrayList<>();
    private final AtomicLong currentEmployeeId = new AtomicLong(0);

    // Get all employees
    public List<Employee> getAllEmployees() {
        return employeeList;
    }

    // Add new employee
    public Employee addEmployee(Employee employee) {
        employee.setId(currentEmployeeId.incrementAndGet());
        employeeList.add(employee);
        return employee;
    }
}
