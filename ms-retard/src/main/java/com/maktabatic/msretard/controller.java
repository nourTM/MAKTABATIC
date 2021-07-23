package com.maktabatic.msretard;


import com.maktabatic.msretard.dao.PunishRepository;
import com.maktabatic.msretard.entities.KeyPunish;
import com.maktabatic.msretard.entities.Punish;
import com.maktabatic.msretard.entities.State;
import com.maktabatic.msretard.model.BookState;
import com.maktabatic.msretard.model.EmailTemplate;
import com.maktabatic.msretard.model.LoanReturn;
import com.maktabatic.msretard.model.Reservation;
import com.maktabatic.msretard.proxy.LoanReturnProxy;
import com.maktabatic.msretard.proxy.NotifProxy;
import com.maktabatic.msretard.proxy.ReaderProxy;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@EnableScheduling
@RequestMapping("api")
@RefreshScope
public class controller {
    @Value("${day : 86400000}")
    long DAY_MILLIS;

    @Value("${punish.days : 7}")
    long punish_days;

    @Value("${end.penalty.subject : End of penalty}")
    String end_penalty_subject;

    @Value("${end.penalty.body : This is the end of your penalty. You should be  in time!}")
    String end_penalty_body;

    @Value("${penalty.subject : Penalty}")
    String penalty_subject;

    @Value("${penalty.body : You are Punished. You should return the book the earliest you can. Put in mind starting from the return date you will be punished for {punish.days} days!}")
    String penalty_body;

    @Autowired
    ReaderProxy readerProxy;
    @Autowired
    NotifProxy notifProxy;
    @Autowired
    PunishRepository punishRepository;
    @Autowired
    LoanReturnProxy loanReturnProxy;
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
    /*@Scheduled(cron = "*//*10 * * * * *")
    public void scan(){
        List<Punish> punishes = punishRepository.findPunishesByState(State.INACTIVATE);
        for (Punish punish: punishes) {
            LoanReturn loanReturn = loanReturnProxy.getLastLoan(punish.getKeyPunish().getRr());
            if ((new Date(loanReturn.getDateReturn().getTime() + punish_days * DAY_MILLIS).before(new Date())
                    || new Date (loanReturn.getDateReturn().getTime() + punish_days * DAY_MILLIS).equals(new Date()) )
            && loanReturn.getState() == BookState.RENDERING)
                punish.setState(State.ACTIVATE);
            EmailTemplate email = new EmailTemplate();
            email.setSendTo(readerProxy.verifyRFIDReader(punish.getKeyPunish().getRr(),"").getEmail());
            email.setSubject(end_penalty_subject);
            email.setBody(end_penalty_body);
            notifProxy.notify(email);
        }
        for (LoanReturn loanReturn : loanReturnProxy.getLates()){
            Punish punish = new Punish();

            KeyPunish keyPunish = new KeyPunish();
            keyPunish.setDate(new Date());
            keyPunish.setRb(loanReturn.getId().getRb());
            keyPunish.setRr(loanReturn.getId().getRr());

            punish.setKeyPunish(keyPunish);
            punish.setState(State.INACTIVATE);
            punishRepository.save(punish);
            EmailTemplate email = new EmailTemplate();
            email.setSendTo(readerProxy.verifyRFIDReader(punish.getKeyPunish().getRr(),"").getEmail());
            email.setSubject(penalty_subject);
            email.setBody(penalty_body);
            notifProxy.notify(email);
        }
    }*/

    @PostMapping("punish")
    boolean punish(@RequestParam("rr") String rr,@RequestParam("rb") String rb){
        Punish punish = new Punish();

        KeyPunish keyPunish = new KeyPunish();
        keyPunish.setDate(new Date());
        keyPunish.setRb(rb);
        keyPunish.setRr(rr);

        punish.setKeyPunish(keyPunish);
        punish.setState(State.INACTIVATE);
        punishRepository.save(punish);
        return true;
    }

    @GetMapping("date")
    public String getDate() throws ParseException {
        SimpleDateFormat dt1 = new SimpleDateFormat("MM-dd");
        String d = dt1.format(new Date());
        return d;
    }
}