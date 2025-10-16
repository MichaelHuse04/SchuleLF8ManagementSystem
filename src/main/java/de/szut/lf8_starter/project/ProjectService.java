package de.szut.lf8_starter.project;

import de.szut.lf8_starter.EmployeeClient.EmployeeClient;
import de.szut.lf8_starter.customer.CustomerEntity;
import de.szut.lf8_starter.customer.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<ProjectEntity> getAll(){
        return this.projectRepository.findAll();
    }

    public ProjectEntity getById(Long id){
        return this.projectRepository.findById(id).orElse(null);
    }

    public void  delete(Long id){
        ProjectEntity projectEntity = this.projectRepository.findById(id).orElse(null);
        if (projectEntity == null) {
            throw new IllegalArgumentException("Project not found");
        }else{
            this.projectRepository.delete(projectEntity);
        }
    }
}
