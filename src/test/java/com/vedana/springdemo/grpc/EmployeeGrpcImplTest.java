package com.vedana.springdemo.grpc;

import com.google.protobuf.Empty;
import com.vedana.springdemo.EmployeeApi;
import com.vedana.springdemo.EmployeeServiceGrpc;
import com.vedana.springdemo.config.DevhGrpcConfig;
import com.vedana.springdemo.services.EmployeeService;
import io.grpc.internal.testing.StreamRecorder;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@DataJpaTest
@Import({EmployeeServiceGrpcImpl.class, EmployeeService.class})
@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yml" })
@TestMethodOrder(MethodOrderer.MethodName.class)
public class EmployeeGrpcImplTest {
    @Autowired
    private EmployeeServiceGrpcImpl employeeService;

    @Test
    void createEmployeeTest() throws Exception {
        StreamRecorder<EmployeeApi.Employee> responseObserver = StreamRecorder.create();

        EmployeeApi.Employee employee = EmployeeApi.Employee.newBuilder()
                .setId("EMP2")
                .setFirstName("Mathieu")
                .setLastName("MATHIAS")
                .setEmail("mmathias@vedana.com")
                .build();

        employeeService.createEmployee(employee, responseObserver);
        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            fail("The call did not terminate in time");
        }
        employee = responseObserver.firstValue().get();
        assertThat(employee.getId()).isEqualTo("EMP2");
    }

    @Test
    void findAllTest()  throws Exception {
        StreamRecorder<EmployeeApi.Employees> responseObserver = StreamRecorder.create();

        employeeService.findAll(Empty.getDefaultInstance(), responseObserver);
        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            fail("The call did not terminate in time");
        }
        var employees = responseObserver.firstValue().get();
        assertThat(employees.getEmployeesCount()).isEqualTo(1);
    }
}
