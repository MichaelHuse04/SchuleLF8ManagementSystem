package de.szut.lf8_starter.project;

import de.szut.lf8_starter.EmployeeClient.EmployeeClient;
import de.szut.lf8_starter.customer.CustomerEntity;
import de.szut.lf8_starter.customer.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeClient employeeClient;

    public ProjectService(ProjectRepository projectRepository, CustomerRepository customerRepository, EmployeeClient employeeClient) {
        this.projectRepository = projectRepository;
        this.customerRepository = customerRepository;
        this.employeeClient = employeeClient;
    }

    public ProjectEntity create(ProjectEntity projectEntity) {
        //Customer
        if(projectEntity.getCustomer() == null) {
            throw new IllegalArgumentException("CustomerID cant be null");
        }
        if (!customerRepository.existsById(projectEntity.getCustomer().getId())) {
            throw new IllegalArgumentException("CustomerID " + projectEntity.getCustomer().getId() + " doesnt exists");
        }

        CustomerEntity customerEntity = customerRepository.getReferenceById(projectEntity.getCustomer().getId());
        projectEntity.setCustomer(customerEntity);

        //Responsible Employee
        if (!employeeClient.existsById(projectEntity.getResponsibleEmployeeId())){
            throw new IllegalArgumentException("Responsible Employee not found");
        }

        for (Long id : projectEntity.getAssingedEmployees()){
            if (!employeeClient.existsById(id)){
                throw new IllegalArgumentException("Employee not found");
            }
        }
        return projectRepository.save(projectEntity);
    }
}
