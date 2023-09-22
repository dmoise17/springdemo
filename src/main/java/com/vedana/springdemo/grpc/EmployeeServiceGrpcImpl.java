package com.vedana.springdemo.grpc;

import com.google.protobuf.Empty;
import com.google.protobuf.StringValue;
import com.vedana.springdemo.EmployeeApi;
import com.vedana.springdemo.EmployeeServiceGrpc;
import com.vedana.springdemo.domain.Employee;
import com.vedana.springdemo.services.EmployeeService;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@GrpcService
public class EmployeeServiceGrpcImpl extends EmployeeServiceGrpc.EmployeeServiceImplBase {
    @Autowired
    EmployeeService employeeService;

    @Override
    public void findById(StringValue request, StreamObserver<EmployeeApi.Employee> responseObserver) {
        Optional<Employee> dbEmployee = employeeService.findEmployeeById(request.getValue());
        if (dbEmployee.isPresent()) {
            EmployeeApi.Employee employee = EmployeeApi.Employee.newBuilder()
                    .setId(dbEmployee.get().getId())
                    .setFirstName(dbEmployee.get().getFirstName())
                    .setLastName(dbEmployee.get().getLastName())
                    .setEmail(dbEmployee.get().getEmail())
                    .build();
            responseObserver.onNext(employee);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new StatusRuntimeException(Status.NOT_FOUND));
        }
    }

    @Override
    public void findAll(Empty request, StreamObserver<EmployeeApi.Employees> responseObserver) {
        List<Employee> dbEmployees = employeeService.findAllEmployees();

        var employees = dbEmployees.stream().map(e -> EmployeeApi.Employee.newBuilder()
                .setId(e.getId())
                .setFirstName(e.getFirstName())
                .setLastName(e.getLastName())
                .setEmail(e.getEmail())
                .build()).toList();

        responseObserver.onNext(EmployeeApi.Employees.newBuilder().addAllEmployees(employees).build());
        responseObserver.onCompleted();
    }

    @Override
    public void createEmployee(EmployeeApi.Employee request, StreamObserver<EmployeeApi.Employee> responseObserver) {
        Employee dbEmployee = new Employee();
        dbEmployee.setId(request.getId());
        dbEmployee.setFirstName(request.getFirstName());
        dbEmployee.setLastName(request.getLastName());
        dbEmployee.setEmail(request.getEmail());

        dbEmployee = employeeService.createOrUpdateEmployee(dbEmployee);

        EmployeeApi.Employee employee = EmployeeApi.Employee.newBuilder()
                .setId(dbEmployee.getId())
                .setFirstName(dbEmployee.getFirstName())
                .setLastName(dbEmployee.getLastName())
                .setEmail(dbEmployee.getEmail())
                .build();
        responseObserver.onNext(employee);
        responseObserver.onCompleted();
    }
}
