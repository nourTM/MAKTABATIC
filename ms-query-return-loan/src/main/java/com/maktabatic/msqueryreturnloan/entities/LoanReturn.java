package com.maktabatic.msqueryreturnloan.entities;

import com.maktabatic.coreapi.model.BookState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class LoanReturn {
    @EmbeddedId
    private KeyLoanReturn id;
    private String title;
    private String name;
    private String firstName;
    @Enumerated(EnumType.STRING)
    private BookState state;
    private Date dateReturn;
    private Long idNotice;
}
