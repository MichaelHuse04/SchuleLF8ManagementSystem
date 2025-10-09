package de.szut.lf8_starter.project;

import de.szut.lf8_starter.customer.CustomerEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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

}
