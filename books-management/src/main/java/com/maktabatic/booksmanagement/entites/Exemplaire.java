package com.maktabatic.booksmanagement.entites;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @JsonIgnore
    @ManyToOne
    private Notice notice;
}
