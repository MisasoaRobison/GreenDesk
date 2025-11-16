package org.example.entites;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;


@Entity
@Data
public class Plant{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    private Species species;

    private double height;

    private LocalDate plantedDate;
}