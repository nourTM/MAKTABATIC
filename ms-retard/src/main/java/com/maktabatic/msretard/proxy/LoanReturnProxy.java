package com.maktabatic.msretard.proxy;

import com.maktabatic.msretard.model.LoanReturn;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ms-cmd")
public interface LoanReturnProxy {

    @GetMapping("/command/cmd/lates")
    List<LoanReturn> getLates();
    @PutMapping("/command/cmd/nolate")
    boolean nolate(@RequestBody LoanReturn loanReturn);
}
