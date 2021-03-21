package com.maktabatic.mscmdloanreturn.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ms-reservation")
public interface ReservationProxy {
    @GetMapping("/api/disponible/{idNotice}")
    Long countDisponible(@PathVariable("idNotice") Long id);
    @GetMapping("/api/verify")
    boolean verifyReservationDisponible( @RequestParam("id") Long idnotice,@RequestParam("rr") String rr);
    @DeleteMapping("/api/delete")
    boolean deleteReservation( @RequestParam("id") Long idnotice,@RequestParam("rr") String rr);
    @GetMapping("/api/waiting/{idNotice}")
    Long countWaiting(@PathVariable("idNotice") Long id);
}
