package com.maktabatic.mscmdloanreturn.api;



import com.maktabatic.coreapi.commands.LoanCommand;
import com.maktabatic.coreapi.commands.ReturnCommand;
import com.maktabatic.coreapi.dto.OperationDTO;
import com.maktabatic.coreapi.model.Book;
import com.maktabatic.coreapi.model.BookState;
import com.maktabatic.coreapi.model.KeyLoanReturn;
import com.maktabatic.coreapi.model.Reader;
import com.maktabatic.mscmdloanreturn.aggregates.LoanReturn;
import com.maktabatic.mscmdloanreturn.dao.LoanReturnRepository;
import com.maktabatic.mscmdloanreturn.proxy.BooksProxy;
import com.maktabatic.mscmdloanreturn.proxy.ReaderProxy;
import com.maktabatic.mscmdloanreturn.proxy.ReservationProxy;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("command")
public class CommandController {

    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    ReaderProxy readerProxy;
    @Autowired
    BooksProxy booksProxy;
    @Autowired
    ReservationProxy reservationProxy;

    @Autowired
    LoanReturnRepository loanReturnRepository;

    @PostMapping("/operation")
    public  String operation(@RequestBody OperationDTO operationDTO, @RequestParam("op") String op)
    {
        List<LoanReturn> loanReturns ;
        LoanReturn lastLoanReturn = null ;
        Date now = new Date();
        Reader reader = readerProxy.verifyRFIDReader(operationDTO.getRfidReader(),"toloan");
        if(verifyOp(op,operationDTO.getRfidReader())) {
            Book book = booksProxy.verifyBook(operationDTO.getRfidBook());

            if (book != null) {
                book = booksProxy.getBook(operationDTO.getRfidBook(),"tocmd");
                Long idnotice = booksProxy.getIdNotice(operationDTO.getRfidBook());
                book.setIdNotice(idnotice);
                if (verifyOp(op, operationDTO.getRfidReader())) {
                    switch (op) {
                        case "loan":
                            // TODO demander le nombre des examp dispo --> rfidbook
                            long nbDispo = reservationProxy.countDisponible(idnotice);
                            if (nbDispo > 0) {
                                if(!loanReturnRepository
                                        .findLoanReturnById_RbOrderById_DateLoanDesc
                                                (operationDTO.getRfidBook()).isEmpty() && loanReturnRepository
                                        .findLoanReturnById_RbOrderById_DateLoanDesc
                                                (operationDTO.getRfidBook())
                                        .get(0).getId().getState()!= BookState.BORROWED){
                                    commandGateway.send(
                                    new LoanCommand(
                                    new KeyLoanReturn(operationDTO.getRfidReader(), operationDTO.getRfidBook(), new Date(),BookState.BORROWED),
                                            reader, book));
                                    reservationProxy.deleteReservation(idnotice, operationDTO.getRfidReader());
                                    return "You could take your book :) ";
                                }else if (loanReturnRepository
                                        .findLoanReturnById_RbOrderById_DateLoanDesc
                                                (operationDTO.getRfidBook()).isEmpty()){
                                    commandGateway.send(
                                            new LoanCommand(
                                                    new KeyLoanReturn(operationDTO.getRfidReader(), operationDTO.getRfidBook(), new Date(),BookState.BORROWED),
                                                    reader, book));
                                    reservationProxy.deleteReservation(idnotice, operationDTO.getRfidReader());
                                    return "You could take your book :) ";
                                }
                                    return "this book is already borrowed or you have not done any operation yet";
                            } else {
                                // TODO verifier que c lui qui a reserver si oui lancer une commande ---> rr & rb
                                //  et demander au ms-resv de supprimer la resv sinon
                                if (reservationProxy.verifyReservationDisponible(idnotice, operationDTO.getRfidReader())){
                                    commandGateway.send(
                                                new LoanCommand(new KeyLoanReturn(operationDTO.getRfidReader()
                                                        , operationDTO.getRfidBook(), new Date(),BookState.BORROWED), reader, book));
                                    reservationProxy.deleteReservation(idnotice,operationDTO.getRfidReader());
                                    return "You could take your reserved book :) ";
                                }
                                return "Sorry, this book is not available";
                            }
                            // TODO envoyer une demande de suppresion
                        case "return":
                            loanReturns = loanReturnRepository.findLoanReturnById_RrOrderById_DateLoanDesc(operationDTO.getRfidReader());
                            lastLoanReturn = loanReturns.get(0);
                            if (lastLoanReturn.getId().getRb().equals(operationDTO.getRfidBook())){
                                if (now.before(lastLoanReturn.getDateReturn()) || now.equals(lastLoanReturn.getDateReturn())) {
                                    commandGateway.send(
                                            new ReturnCommand(new KeyLoanReturn(operationDTO.getRfidReader(), operationDTO.getRfidBook(), lastLoanReturn.getId().getDateLoan(), BookState.RENDERING), new Date()));
                                    return "Thank you best regards :) ";
                                } else {
                                    commandGateway.send(
                                            new ReturnCommand(new KeyLoanReturn(operationDTO.getRfidReader(), operationDTO.getRfidBook(), lastLoanReturn.getId().getDateLoan(), BookState.LATE), new Date()));
                                    return "Please Try to Returns Books in time for not being penalized, Now you can not loan books for 7 days";
                                }
                            } else return "this is not the loaned book";
                        default:
                            return "there no such operation";
                    }
                }else return "This book does not exists";
            }else{
                return "This RFID does not correspond to any book in our Database";
            }
        } else if (op.equals("prolongation")){

            loanReturns = loanReturnRepository.findLoanReturnById_RrOrderById_DateLoanDesc(operationDTO.getRfidReader());
            if (loanReturns != null && !loanReturns.isEmpty()) {
                lastLoanReturn = loanReturns.get(0);
                Book book = booksProxy.getBook(lastLoanReturn.getId().getRb(),"tocmd");

                Long idnotice = booksProxy.getIdNotice(operationDTO.getRfidBook());
                book.setIdNotice(idnotice);

                final long DAY_MILLIS = 86400000;

                Date temp = new Date(now.getTime() + 2 * DAY_MILLIS);
                // TODO : demander au ms- resv
                // TODO demander si il ya des reader en attente de ce livre
                long nbdispo = reservationProxy.countDisponible(idnotice) + 1;
                long waiting = reservationProxy.countWaiting(idnotice);
                if (
                        lastLoanReturn.getId().getState() == BookState.BORROWED
                                && lastLoanReturn.getDateReturn().after(now)
                                && (lastLoanReturn.getDateReturn().before(temp)) && (nbdispo > 0 && waiting == 0)) {
                    commandGateway.send(
                            new ReturnCommand(new KeyLoanReturn(operationDTO.getRfidReader(),lastLoanReturn.getId().getRb() , lastLoanReturn.getId().getDateLoan(), BookState.RENDERING), new Date()));

                    commandGateway.send(
                            new LoanCommand(new KeyLoanReturn(operationDTO.getRfidReader(), lastLoanReturn.getId().getRb(), new Date(),BookState.BORROWED), reader, book));
                    return "You could take your book :) ";
                } else {
                    if (lastLoanReturn.getId().getState() != BookState.BORROWED) return  "you have not a borrowed book";
                    else if ( (lastLoanReturn.getDateReturn().before(temp))) return "you have not extend it now we attend some reservation you could reserve this book before 2 days of expiration";
                    else
                        return "Sorry you can not extend the time there's is other student which are waiting for it";
                }
            } else return "you have no operation before";

        }
        else {
            if(reader!=null) return "You can not do this operation";
            return "you have passed unknown tag";
        }
    }

    @GetMapping("/verify")
    public boolean verifyOp(@RequestParam("op") String operation,@RequestParam("rr") String rr ){
        if(readerProxy.verifyRFIDReader(rr,"toloan") != null) {
            List<LoanReturn> loanReturns = loanReturnRepository.findLoanReturnById_RrOrderById_DateLoanDesc(rr);
            LoanReturn lastLoanReturn = null ;
            if(loanReturns != null && !loanReturns.isEmpty())  lastLoanReturn = loanReturns.get(0);
            switch (operation){
                case "loan":
                    return lastLoanReturn == null || lastLoanReturn.getId().getState() == BookState.RENDERING;
                case "return":
                    return lastLoanReturn != null && (lastLoanReturn.getId().getState() == BookState.BORROWED);
                /*case "reservation":
                    return lastLoanReturn == null || lastLoanReturn.getState() == BookState.RENDERING;*/
                default:
                    return  false;
            }
        }
        else return false;
    }

    @GetMapping("/loans")
    public List<LoanReturn> getloans(){
        return loanReturnRepository.findAll();
    }


    @GetMapping("/cmd/lates")
    List<LoanReturn> getLates(){
        return loanReturnRepository.findLoanReturnById_State(BookState.LATE);
    }
    @PutMapping("/cmd/nolate")
    void nolate(@RequestBody LoanReturn loanReturn){
        loanReturn.getId().setState(BookState.RENDERING);
        commandGateway.send(
                new ReturnCommand(new KeyLoanReturn(loanReturn.getId().getRr(),loanReturn.getId().getRb()
                        ,loanReturn.getId().getDateLoan(),loanReturn.getId().getState()), loanReturn.getDateReturn()));
    }

    @GetMapping("/book/{rb}")
    public Book getbook(@PathVariable("rb") String rb){
        return booksProxy.getBook(rb,"tocmd");
    }
}