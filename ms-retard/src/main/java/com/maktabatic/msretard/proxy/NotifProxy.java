package com.maktabatic.msretard.proxy;

import com.maktabatic.msretard.model.EmailTemplate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ms-notif")
public interface NotifProxy {
    @PostMapping("/v1/notification/textemail")
    String notify(@RequestBody EmailTemplate emailTemplate);
}
