package com.maktabatic.mscmdloanreturn.aggregates;

import com.maktabatic.coreapi.commands.LoanCommand;
import com.maktabatic.coreapi.commands.ReturnCommand;
import com.maktabatic.coreapi.events.LoanEvent;
import com.maktabatic.coreapi.events.ReturnEvent;
import com.maktabatic.coreapi.model.BookState;
import com.maktabatic.mscmdloanreturn.entities.KeyLoanReturn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.common.Assert;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


@Aggregate
@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class LoanReturn {

    @AggregateIdentifier
    @EmbeddedId
    private KeyLoanReturn id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateReturn;




    @CommandHandler
    public LoanReturn(LoanCommand cmd){

        Calendar c = Calendar.getInstance();
        c.setTime(cmd.getId().getDateLoan());
        c.add(Calendar.DATE, 15);
        Assert.notNull(cmd.getId(), () -> "The CMD ID should be not null");
        Assert.notNull(cmd.getReader(), () -> "The RFID Reader should be not null");
        AggregateLifecycle.apply( new LoanEvent(cmd.getId(),cmd.getReader(),cmd.getBook()
                ,c.getTime()));
    }

    @CommandHandler
    public LoanReturn(ReturnCommand cmd){
        AggregateLifecycle.apply( new ReturnEvent(cmd.getId(), cmd.getDateReturnEff()));
    }

    @EventSourcingHandler
    public void on (LoanEvent event)
    {
        this.id = new KeyLoanReturn(event.getId().getRr(),event.getId().getRb(),event.getId().getDateLoan(),event.getId().getState());
        this.dateReturn = event.getDateReturn();

    }

    @EventSourcingHandler
    public void on (ReturnEvent event)
    {
        this.id = new KeyLoanReturn(event.getId().getRr(),event.getId().getRb(),event.getId().getDateLoan(),event.getId().getState());
        this.dateReturn = event.getDateReturn();

    }


}