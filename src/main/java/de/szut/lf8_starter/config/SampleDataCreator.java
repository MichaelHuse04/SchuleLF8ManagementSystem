package de.szut.lf8_starter.config;

import de.szut.lf8_starter.customer.CustomerEntity;
import de.szut.lf8_starter.customer.CustomerRepository;
import de.szut.lf8_starter.employee.skill.EmployeeSkillEntity;
import de.szut.lf8_starter.hello.HelloEntity;
import de.szut.lf8_starter.hello.HelloRepository;
import de.szut.lf8_starter.project.ProjectEntity;
import de.szut.lf8_starter.project.ProjectRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class SampleDataCreator implements ApplicationRunner {

    private final HelloRepository repository;
    private final CustomerRepository customerRepository;
    private final ProjectRepository projectRepository;

    public SampleDataCreator(HelloRepository repository, CustomerRepository customerRepository, ProjectRepository projectRepository) {
        this.repository = repository;
        this.customerRepository = customerRepository;
        this.projectRepository = projectRepository;
    }

    public void run(ApplicationArguments args) {
        repository.save(new HelloEntity("Hallo Welt!"));
        repository.save(new HelloEntity("Schöner Tag heute"));
        repository.save(new HelloEntity("FooBar"));
        CustomerEntity customer = new CustomerEntity(1L, "Karsten", "Meier", "testmail");
        customerRepository.save(customer);
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = now.plusDays(1);
        EmployeeSkillEntity entity = new EmployeeSkillEntity(1L, 348L, "toll");
        EmployeeSkillEntity entity2 = new EmployeeSkillEntity(2L, 349L, "bester");
        EmployeeSkillEntity entity3 = new EmployeeSkillEntity(3L, 323L, "spieler");
        EmployeeSkillEntity entity4 = new EmployeeSkillEntity(4L, 312312321123L, "jemals");
        EmployeeSkillEntity entity5 = new EmployeeSkillEntity(5L, 34L, "ich");
        List<EmployeeSkillEntity> employees = new ArrayList<>();
        employees.add(entity);
        List<EmployeeSkillEntity> employees2 = new ArrayList<>();
        employees2.add(entity2);
        employees2.add(entity3);
        employees2.add(entity4);
        employees2.add(entity5);
        projectRepository.save(new ProjectEntity(1L, "Project 1",
                Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(tomorrow.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                null, "Kommentar1", customer, 348L, employees));
        projectRepository.save(new ProjectEntity(2L, "Project 2",
                Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(tomorrow.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                null, "Kommentar2", customer, 348L, employees2));
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
