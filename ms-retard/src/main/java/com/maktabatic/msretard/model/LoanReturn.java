package com.maktabatic.msretard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanReturn {
    private KeyLoanReturn id;
    private BookState state;
    private Date dateReturn;
}
