package com.maktabatic.msretard.api;

import com.maktabatic.msretard.dao.PunishRepository;
import com.maktabatic.msretard.entities.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@EnableScheduling
@RequestMapping("api")
public class LateController {
    @Autowired
    PunishRepository punishRepository;

    @GetMapping("punished")
    boolean isPunished(@RequestParam("rr") String rr){
        return (!punishRepository.findPunishesByKeyPunish_Rr(rr).isEmpty()
        && punishRepository.findPunishesByKeyPunish_Rr(rr).get(0).getState()== State.INACTIVATE);
    }

}
