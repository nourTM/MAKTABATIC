package com.maktabatic.msretard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyLoanReturn {

    private String rr;
    private String rb;
    private Date dateLoan;

}
