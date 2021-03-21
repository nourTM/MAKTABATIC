package com.maktabatic.msqueryloanreturn.dao;

import com.maktabatic.coreapi.model.BookState;
import com.maktabatic.msqueryloanreturn.entities.KeyLoanReturn;
import com.maktabatic.msqueryloanreturn.entities.LoanReturn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface LoanReturnRepository extends JpaRepository <LoanReturn, KeyLoanReturn> {
    Long countLoanReturnsByIdNoticeAndState(Long id, BookState state);
    List<LoanReturn> findLoanReturnById_RrOrderById_DateLoanDesc(String rr);
}
