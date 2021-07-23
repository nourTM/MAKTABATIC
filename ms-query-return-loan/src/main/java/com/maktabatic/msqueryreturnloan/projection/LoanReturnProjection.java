package com.maktabatic.msqueryreturnloan.projection;

import com.maktabatic.coreapi.events.LoanEvent;
import com.maktabatic.coreapi.events.ReturnEvent;
import com.maktabatic.msqueryreturnloan.dao.LoanReturnRepository;
import com.maktabatic.msqueryreturnloan.entities.KeyLoanReturn;
import com.maktabatic.msqueryreturnloan.entities.LoanReturn;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoanReturnProjection {

    @Autowired
    LoanReturnRepository loanReturnRepository;


    @EventHandler
    public void loanReturn(LoanEvent event){
        System.out.println("***** event handling*******************");
        LoanReturn loanReturn = new
                LoanReturn(new KeyLoanReturn(event.getId().getRr(),event.getId().getRb(),event.getId().getDateLoan()),event.getBook().getTitle(),
                event.getReader().getName(),event.getReader().getFirstName(),event.getId().getState(),event.getDateReturn(),event.getBook().getIdNotice());
        loanReturnRepository.save(loanReturn);
    }

    @EventHandler
    public void loanReturn(ReturnEvent event){
        System.out.println("***** event handling*******************");
        LoanReturn loanReturn = loanReturnRepository.findById(new KeyLoanReturn(event.getId().getRr(),event.getId().getRb(),event.getId().getDateLoan())).get();
        loanReturn.setState(event.getId().getState());
        loanReturn.setDateReturn(event.getDateReturn());
        loanReturnRepository.save(loanReturn);
    }

}
