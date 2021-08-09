package com.maktabatic.booksmanagement.entites;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Exemplaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idExemplaire;

    private String rfid;

    private boolean lost;

    @ToString.Exclude
    @ManyToOne
    @JsonBackReference
    private Notice notice;
}
