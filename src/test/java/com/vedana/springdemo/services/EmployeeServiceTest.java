package com.vedana.springdemo.services;

import com.vedana.springdemo.domain.Employee;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yml" })
@TestMethodOrder(MethodOrderer.MethodName.class)
@Import(EmployeeService.class)
public class EmployeeServiceTest {
    @Autowired
    EmployeeService employeeService;

    @Test
    void injectedComponentsAreNotNull(){
        assertThat(employeeService).isNotNull();
    }

    @Test
    void createEmployeeTest() {
        Employee employee = new Employee();
        employee.setId("EMP2");

        employeeService.createOrUpdateEmployee(employee);

        List<Employee> employees = employeeService.findAllEmployees();
        assertThat(employees.size()).isEqualTo(2);
    }

    @Test
    void findAllTest() {
        List<Employee> employees = employeeService.findAllEmployees();
        assertThat(employees.size()).isEqualTo(1);
    }
}
