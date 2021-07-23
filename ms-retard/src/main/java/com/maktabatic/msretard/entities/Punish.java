package com.maktabatic.msretard.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class Punish {
    @Id
    private KeyPunish keyPunish;
    private State state;
}
