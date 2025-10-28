package de.szut.lf8_starter.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.szut.lf8_starter.customer.CustomerEntity;
import de.szut.lf8_starter.customer.CustomerRepository;
import de.szut.lf8_starter.employee.EmployeeClient;
import de.szut.lf8_starter.employee.skill.EmployeeSkillEntity;
import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProjectControllerIT extends AbstractIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EntityManager entityManager;

    @MockBean
    private EmployeeClient employeeClient;

    private CustomerEntity testCustomer;
    private Long responsibleEmployeeId;

    @BeforeEach
    void setUp() {
        // Erstelle einen Testkunden
        testCustomer = new CustomerEntity();
        testCustomer.setFirstName("Test");
        testCustomer.setLastName("Customer");
        testCustomer.setEmail("test@customer.com");
        testCustomer = customerRepository.save(testCustomer);

        // Mock den EmployeeClient
        responsibleEmployeeId = 1L;
        when(employeeClient.existsById(responsibleEmployeeId)).thenReturn(true);
    }

    @AfterEach
    void tearDown() {
        projectRepository.deleteAll();
        customerRepository.deleteAll();
        entityManager.createNativeQuery("ALTER SEQUENCE projects_id_seq RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER SEQUENCE customers_id_seq RESTART WITH 1").executeUpdate();
    }

    @Test
    void whenCreateProject_happyPath() throws Exception {
        // given
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 86400000); // Ein Tag später
        ProjectCreateDto createDto = new ProjectCreateDto(
            "Test Description",
            startDate,
            endDate,
            "Test Comment",
            responsibleEmployeeId,
            testCustomer.getId(),
            new ArrayList<>()
        );

        // when
        MvcResult result = mockMvc.perform(post("/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.comment").value("Test Comment"))
                .andExpect(jsonPath("$.startDate").exists())
                .andExpect(jsonPath("$.endDate").exists())
                .andReturn();

        // then
        Long projectId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
        assertThat(projectRepository.findById(projectId)).isPresent();
    }

    @Test
    void whenGetAllProjects_happyPath() throws Exception {
        // given
        ProjectEntity project = new ProjectEntity();
        project.setDescription("Project 1");
        project.setComment("Kommentar1");
        project.setCustomer(testCustomer);
        project.setResponsibleEmployeeId(responsibleEmployeeId);
        project.setStartDate(new Date());
        project.setEndDate(new Date(System.currentTimeMillis() + 86400000));
        project.setEmployeeSkills(new ArrayList<>());
        projectRepository.save(project);

        // when/then
        mockMvc.perform(get("/project"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Project 1"))
                .andExpect(jsonPath("$[0].comment").value("Kommentar1"));
    }

    @Test
    void whenGetProjectById_happyPath() throws Exception {
        // given
        ProjectEntity project = new ProjectEntity();
        project.setDescription("Test Description");
        project.setComment("Test Comment");
        project.setCustomer(testCustomer);
        project.setResponsibleEmployeeId(responsibleEmployeeId);
        project.setStartDate(new Date());
        project.setEndDate(new Date(System.currentTimeMillis() + 86400000));
        project.setEmployeeSkills(new ArrayList<>());
        ProjectEntity savedProject = projectRepository.save(project);

        // when/then
        mockMvc.perform(get("/project/" + savedProject.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.comment").value("Test Comment"));
    }

    @Test
    void whenUpdateProject_happyPath() throws Exception {
        // given
        ProjectEntity project = new ProjectEntity();
        project.setDescription("Original Description");
        project.setComment("Original Comment");
        project.setCustomer(testCustomer);
        project.setResponsibleEmployeeId(responsibleEmployeeId);
        project.setStartDate(new Date());
        project.setEndDate(new Date(System.currentTimeMillis() + 86400000));
        project.setEmployeeSkills(new ArrayList<>());
        ProjectEntity savedProject = projectRepository.save(project);

        Date newStartDate = new Date();
        Date newEndDate = new Date(newStartDate.getTime() + 86400000);
        ProjectCreateDto updateDto = new ProjectCreateDto(
            "Updated Description",
            newStartDate,
            newEndDate,
            "Updated Comment",
            responsibleEmployeeId,
            testCustomer.getId(),
            new ArrayList<>()
        );

        // when/then
        mockMvc.perform(put("/project/" + savedProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.comment").value("Updated Comment"));
    }

    @Test
    void whenDeleteProject_happyPath() throws Exception {
        // given
        ProjectEntity project = new ProjectEntity();
        project.setDescription("To Delete");
        project.setComment("Will be deleted");
        project.setCustomer(testCustomer);
        project.setResponsibleEmployeeId(responsibleEmployeeId);
        project.setStartDate(new Date());
        project.setEndDate(new Date(System.currentTimeMillis() + 86400000));
        project.setEmployeeSkills(new ArrayList<>());
        ProjectEntity savedProject = projectRepository.save(project);

        // when
        mockMvc.perform(delete("/project/" + savedProject.getId()))
                .andExpect(status().isOk());

        // then
        assertThat(projectRepository.findById(savedProject.getId())).isEmpty();
    }

    @Test
    void whenGetProjectEmployees_happyPath() throws Exception {
        // given
        ProjectEntity project = new ProjectEntity();
        project.setDescription("Project with Employees");
        project.setComment("Test Comment");
        project.setCustomer(testCustomer);
        project.setResponsibleEmployeeId(responsibleEmployeeId);
        project.setStartDate(new Date());
        project.setEndDate(new Date(System.currentTimeMillis() + 86400000));
        project.setEmployeeSkills(new ArrayList<>());
        ProjectEntity savedProject = projectRepository.save(project);

        // when/then
        mockMvc.perform(get("/project/" + savedProject.getId() + "/employees"))
                .andExpect(status().isOk());
    }

    @Test
    void whenDeleteAssignedEmployeeFromProject_happyPath() throws Exception {
        // given
        ProjectEntity project = new ProjectEntity();
        project.setDescription("Project with Employee");
        project.setComment("Test Comment");
        project.setCustomer(testCustomer);
        project.setResponsibleEmployeeId(responsibleEmployeeId);
        project.setStartDate(new Date());
        project.setEndDate(new Date(System.currentTimeMillis() + 86400000));

        // Erstelle einen EmployeeSkill für das Projekt
        EmployeeSkillEntity employeeSkill = new EmployeeSkillEntity();
        employeeSkill.setEmployeeId(2L); // Anderer Mitarbeiter als der Verantwortliche
        employeeSkill.setSkill("Java");
        ArrayList<EmployeeSkillEntity> skills = new ArrayList<>();
        skills.add(employeeSkill);
        project.setEmployeeSkills(skills);

        ProjectEntity savedProject = projectRepository.save(project);

        // Mock für den zusätzlichen Mitarbeiter
        when(employeeClient.existsById(2L)).thenReturn(true);

        // when/then
        mockMvc.perform(delete("/project/" + savedProject.getId() + "/assignedEmployee/2"))
                .andExpect(status().isOk());

        // Überprüfe, dass der Mitarbeiter entfernt wurde
        ProjectEntity updatedProject = projectRepository.findById(savedProject.getId()).orElseThrow();
        assertThat(updatedProject.getEmployeeSkills()).isEmpty();
    }
}
