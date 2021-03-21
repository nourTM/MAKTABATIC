package com.maktabatic.msretard;


import com.maktabatic.msretard.model.LoanReturn;
import com.maktabatic.msretard.model.Reservation;
import com.maktabatic.msretard.proxy.LoanReturnProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@EnableScheduling
@RequestMapping("api")
public class controller {
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
    @Scheduled(cron = "*/10 * * * * *")
    public void scan(){
        for (LoanReturn loanReturn : loanReturnProxy.getLates()){
            if (new Date(loanReturn.getDateReturn().getTime() + 7 * 86400000).before(new Date())
                    || new Date (loanReturn.getDateReturn().getTime() + 7 * 86400000).equals(new Date()) ){
                loanReturnProxy.nolate(loanReturn);
                // TODO noity the reader that the reserve time is finished
            }
        }
    }

}