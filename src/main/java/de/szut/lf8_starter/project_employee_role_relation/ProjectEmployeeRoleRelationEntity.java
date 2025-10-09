package de.szut.lf8_starter.project_employee_role_relation;

import de.szut.lf8_starter.employee.EmployeeEntity;
import de.szut.lf8_starter.project.ProjectEntity;
import de.szut.lf8_starter.role.RoleEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Entity
@Data
@Setter
@Getter
@Table(name = "project_employee_role_relation")
public class ProjectEmployeeRoleRelationEntity {

    @Id
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private ProjectEntity project;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private RoleEntity role;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private EmployeeEntity employee;

}
