package com.maktabatic.msreservation.api;

import com.maktabatic.msreservation.dao.ReservartionRepository;
import com.maktabatic.msreservation.entities.KeyReservation;
import com.maktabatic.msreservation.entities.Reservation;
import com.maktabatic.msreservation.model.BookState;
import com.maktabatic.msreservation.model.LoanReturn;
import com.maktabatic.msreservation.proxy.BooksProxy;
import com.maktabatic.msreservation.proxy.LoanReturnProxy;
import com.maktabatic.msreservation.proxy.ReaderProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@EnableScheduling
@RequestMapping("api")
public class ReservationController {
    @Autowired
    BooksProxy booksProxy;

    @Autowired
    LoanReturnProxy loanReturnProxy;

    @Autowired
    ReaderProxy readerProxy;

    @Autowired
    ReservartionRepository reservartionRepository;

    @GetMapping("/disponible/{idNotice}")
    public Long countDisponible(@PathVariable("idNotice") Long id){
        return booksProxy.countExampTotal(id)-loanReturnProxy.countLoaned(id)-reservartionRepository.countReservationsById_IdNoticeAndDisponibleTrue(id);
    }

    @GetMapping("/waiting/{idNotice}")
    Long countWaiting(@PathVariable("idNotice") Long id){
        return reservartionRepository.countReservationsById_IdNoticeAndDisponibleFalse(id);
    }

    @GetMapping("/verify")
    boolean verifyReservationDisponible(@RequestParam("id") Long idnotice,@RequestParam("rr") String rr){
        return reservartionRepository.existsById(new KeyReservation(idnotice,rr));
    }

    @DeleteMapping("/delete")
    boolean deleteReservation( @RequestParam("id") Long idnotice,@RequestParam("rr") String rr){
        Reservation reservation = (reservartionRepository.findById(new KeyReservation(idnotice,rr)).isPresent())?reservartionRepository.findById(new KeyReservation(idnotice,rr)).get():null;
        if (reservation!= null ) {
            reservartionRepository.delete(reservation);
            return true;
        }
        return false;
    }

    @PostMapping("/reservation")
    public String reserve(@Validated @RequestBody Reservation reservation){
        if (readerProxy.verifyRFIDReader(reservation.getId().getRr(),"toloan")!=null) {
            LoanReturn lastLoan = loanReturnProxy.getLastLoan(reservation.getId().getRr());
            long nbresv = reservartionRepository.countReservationsById_RrAndDisponibleTrue(reservation.getId().getRr());
            if (countDisponible(reservation.getId().getIdNotice()) > 0 && nbresv == 0 && (lastLoan == null || lastLoan.getState() == BookState.RENDERING)) {
                reservation.setDisponible(true);
                reservation.setDateResv(new Date());
                reservartionRepository.save(reservation);
                return "You have two Days to go to the library and loan your reserved book";
            } else if (countDisponible(reservation.getId().getIdNotice()) <= 0) {
                reservation.setDisponible(false);
                reservation.setDateResv(new Date());
                reservartionRepository.save(reservation);
                return "Sorry, You can not reserve this book now but you will be notified when it is available";
            } else if (nbresv != 0) return "you had already a reservation";
            else return "You already a borrowed book our you are penalized";
        }else return "this RFID is Unknown";
    }
/*
 * "0 0 * * * *" = the top of every hour of every day.
 * "*//*10 * * * * *" = every ten seconds.
*            * "0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
*            * "0 0 8,10 * * *" = 8 and 10 o'clock of every day.
*            * "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.
*            * "0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays
* "0 0 0 25 12 ?" = every Christmas Day at midnight

second, minute, hour, day of month, month, day(s) of week
* */
    @Scheduled(cron = "*/10 * * * * *")
    public void scan(){
        for (Reservation reservation : reservartionRepository.findReservationsByDisponibleTrue()){
            if (new Date(reservation.getDateResv().getTime() + 2 * 86400000).before(new Date()) || new Date (reservation.getDateResv().getTime() + 2 * 86400000).equals(new Date()) ){
                reservartionRepository.delete(reservation);
                // TODO noity the reader that the reserve time is finished
            }
        }
    }

}
