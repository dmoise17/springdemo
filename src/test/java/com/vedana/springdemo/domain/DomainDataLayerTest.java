package com.vedana.springdemo.domain;

import com.vedana.springdemo.repositories.EmployeeRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import static org.assertj.core.api.Assertions.*;

import javax.sql.DataSource;
import java.util.List;

@DataJpaTest
@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yml" })
@TestMethodOrder(MethodOrderer.MethodName.class)
public class DomainDataLayerTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void injectedComponentsAreNotNull(){
        assertThat(dataSource).isNotNull();
        assertThat(employeeRepository).isNotNull();
    }

    @Test
    void createEmployeeTest() {
        Employee employee = new Employee();
        employee.setId("EMP2");

        employeeRepository.save(employee);

        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees.size()).isEqualTo(2);
    }

    @Test
    void findAllTest() {
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees.size()).isEqualTo(1);
    }
}
