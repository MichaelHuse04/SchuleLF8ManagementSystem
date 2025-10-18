package de.szut.lf8_starter.project;

import de.szut.lf8_starter.customer.CustomerEntity;
import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import de.szut.lf8_starter.project.dto.ProjectGetDto;
import org.springframework.stereotype.Service;

@Service
public class ProjectMapper {


    public ProjectMapper() {

    }

    public ProjectGetDto mapToGetProjectDto(ProjectEntity projectEntity) {
        return new ProjectGetDto(projectEntity.getId(), projectEntity.getDescription(), projectEntity.getStartDate(),
                projectEntity.getEndDate(), projectEntity.getRealEndDate(), projectEntity.getComment(),
                projectEntity.getCustomer().getId(), projectEntity.getResponsibleEmployeeId(),
                projectEntity.getAssingedEmployees());
    }

    public ProjectEntity mapCreateDtoToEntity(ProjectCreateDto dto) throws Exception {
        var entity = new ProjectEntity();
        entity.setDescription(dto.getDescription());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setComment(dto.getComment());

        if (dto.getCustomerID() != null) {
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setId(dto.getCustomerID());
            entity.setCustomer(customerEntity);
        }

        entity.setResponsibleEmployeeId(dto.getResponsibleEmployeeID());
        entity.setAssingedEmployees(dto.getAssignedEmployees());

        return entity;
    }
}
