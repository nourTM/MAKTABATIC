package com.maktabatic.msretard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.Date;

@Data @AllArgsConstructor  @NoArgsConstructor
public class Reservation {
    private KeyReservation id;
    private boolean disponible;
    private Date dateResv;
}
