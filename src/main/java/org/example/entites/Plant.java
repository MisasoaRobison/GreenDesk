package org.example.entites;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDate;
import lombok.*;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "plants")
public class Plant{
    @Id
    private String id;

    private String name;

    @DBRef
    private Species species;

    private double height;

    private LocalDate plantedDate;
}