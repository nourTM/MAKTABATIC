package com.maktabatic.msqueryloanreturn.api;

import com.maktabatic.coreapi.model.BookState;
import com.maktabatic.msqueryloanreturn.dao.LoanReturnRepository;
import com.maktabatic.msqueryloanreturn.entities.LoanReturn;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("query")
public class QueryController {

    @Autowired
    private QueryGateway queryGateway;

    @Autowired
    private LoanReturnRepository loanReturnRepository;

    @GetMapping("/loanreturn")
    public List<LoanReturn> getLoans() {
        return loanReturnRepository.findAll();
    }

    @GetMapping("/loaned/{idNotice}")
    public Long countLoaned(@PathVariable("idNotice") Long id){
        return loanReturnRepository.countLoanReturnsByIdNoticeAndState(id, BookState.BORROWED);
    }
    @GetMapping("/lastLoan")
    LoanReturn getLastLoan(@RequestParam("rr") String rr){
        if (loanReturnRepository.findLoanReturnById_RrOrderById_DateLoanDesc(rr)!=null && !loanReturnRepository.findLoanReturnById_RrOrderById_DateLoanDesc(rr).isEmpty()) return loanReturnRepository.findLoanReturnById_RrOrderById_DateLoanDesc(rr).get(0);
        else return null;
    }


}
