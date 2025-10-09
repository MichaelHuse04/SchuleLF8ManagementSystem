package de.szut.lf8_starter.project.dto;

import de.szut.lf8_starter.customer.CustomerEntity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public class CreateProjectDto {


    private String description;

    @NotBlank(message = "a Project needs a start date")
    private Date startDate;

    @NotBlank(message = "a Project needs a end date")
    private Date endDate;

    private Date realEndDate;

    private String comment;

}
