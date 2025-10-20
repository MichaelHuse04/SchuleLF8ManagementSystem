package de.szut.lf8_starter.project;

import de.szut.lf8_starter.customer.CustomerEntity;
import de.szut.lf8_starter.employee.skill.EmployeeSkillEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Setter
@Getter
@Table(name = "projects")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private Date startDate;

    private Date endDate;

    private Date realEndDate;

    private String comment;

    @ManyToOne
    private CustomerEntity customer;

    private Long responsibleEmployeeId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "project_id")
    private List<EmployeeSkillEntity> employeeSkills;
}
