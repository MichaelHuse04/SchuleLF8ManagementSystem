package de.szut.lf8_starter.employee.skill;

import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Setter
@Getter
@Table(name = "employeeSkills")
public class EmployeeSkillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId;

    private String skill;

}
