package org.example.entites;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "species")
public class Species{
    @Id
    private String id;

    private String name; //le nom de l'espèce
    private double waterNeeds; //les besoins en eau

    //Les temperatures min et max
    private double TempMin;
    private double TempMax;

    //Les luminosités min et max
    private double LuminosityMin;
    private double LuminosityMax;

    public Species(String id, String name) {
        this.id = id;
        this.name = name;
    }

}