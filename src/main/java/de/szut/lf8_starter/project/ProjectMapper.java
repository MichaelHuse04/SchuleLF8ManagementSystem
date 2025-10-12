package de.szut.lf8_starter.project;

import de.szut.lf8_starter.customer.CustomerEntity;
import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import org.springframework.stereotype.Service;

@Service
public class ProjectMapper {


    public ProjectMapper() {

    }

    public ProjectEntity mapCreateDtoToEntity(ProjectCreateDto dto) throws Exception {
        var entity = new ProjectEntity();
        entity.setDescription(dto.getDescription());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setComment(dto.getComment());

        if (dto.getCustomerID() != null){
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setId(dto.getCustomerID());
            entity.setCustomer(customerEntity);
        }

        entity.setResponsibleEmployeeId(dto.getResponsibleEmployeeID());
        entity.setAssingedEmployees(dto.getAssignedEmployees());

        return entity;
    }
}
