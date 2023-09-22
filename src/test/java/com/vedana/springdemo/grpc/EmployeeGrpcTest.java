package com.vedana.springdemo.grpc;

import com.google.protobuf.Empty;
import com.vedana.springdemo.EmployeeApi;
import com.vedana.springdemo.EmployeeServiceGrpc;
import com.vedana.springdemo.config.DevhGrpcConfig;
import com.vedana.springdemo.services.EmployeeService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({DevhGrpcConfig.class, EmployeeServiceGrpcImpl.class, EmployeeService.class})
@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yml" })
@TestMethodOrder(MethodOrderer.MethodName.class)
public class EmployeeGrpcTest {
    @GrpcClient("inProcess")
    EmployeeServiceGrpc.EmployeeServiceBlockingStub employeeServiceStub;

    @Test
    void createEmployeeTest() {
        EmployeeApi.Employee employee = EmployeeApi.Employee.newBuilder()
                .setId("EMP2")
                .setFirstName("Mathieu")
                .setLastName("MATHIAS")
                .setEmail("mmathias@vedana.com")
                .build();

        employee = employeeServiceStub.createEmployee(employee);
        assertThat(employee.getId()).isEqualTo("EMP2");
    }

    @Test
    void findAllTest() {
        EmployeeApi.Employees employees = employeeServiceStub.findAll(Empty.getDefaultInstance());
        assertThat(employees.getEmployeesCount()).isEqualTo(1);
    }
}
