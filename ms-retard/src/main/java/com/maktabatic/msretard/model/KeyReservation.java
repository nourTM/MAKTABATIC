package com.maktabatic.msretard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;


@Data @AllArgsConstructor @NoArgsConstructor
public class KeyReservation implements Serializable {
    private Long idNotice;
    private String rr;
}
