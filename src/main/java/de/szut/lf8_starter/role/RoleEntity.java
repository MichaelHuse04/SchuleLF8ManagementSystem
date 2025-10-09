package de.szut.lf8_starter.role;

import jakarta.persistence.*;
import lombok.*;


@Setter
@Getter
@Entity
@Data
@Table(name = "roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

}
