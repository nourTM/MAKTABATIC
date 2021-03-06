package com.maktabatic.mscmdloanreturn.dao;

import com.maktabatic.coreapi.model.BookState;
import com.maktabatic.mscmdloanreturn.aggregates.LoanReturn;
import com.maktabatic.mscmdloanreturn.entities.KeyLoanReturn;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface LoanReturnRepository extends JpaRepository<LoanReturn, KeyLoanReturn> {
    List<LoanReturn> findLoanReturnById_RrOrderById_DateLoanDesc(String rr);
    List<LoanReturn> findLoanReturnById_RbOrderById_DateLoanDesc(String rb);
    List<LoanReturn> findLoanReturnById_State(BookState state);
}
