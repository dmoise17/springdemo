package com.vedana.springdemo.controllers;


import com.vedana.springdemo.domain.Employee;
import com.vedana.springdemo.repositories.EmployeeRepository;
import com.vedana.springdemo.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping(value="/", produces = "application/json")
    public List<Employee> listEmployees() {
        return employeeService.findAllEmployees();
    }

    @GetMapping(value="/{id}", produces = "application/json")
    public Employee getEmployee(@PathVariable String id) {
        return employeeService.findEmployeeById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource"));
    }
}
