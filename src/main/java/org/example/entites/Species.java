package org.example.entites;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
public class Species{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; //le nom de l'espèce
    private double waterNeeds; //les besoins en eau

    //Les temperatures min et max
    private double TempMin;
    private double TempMax;

    //Les luminosités min et max
    private double LuminosityMin;
    private double LuminosityMax;
}