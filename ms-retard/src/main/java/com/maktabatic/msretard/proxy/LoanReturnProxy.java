package com.maktabatic.msretard.proxy;

import com.maktabatic.msretard.model.LoanReturn;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ms-query")
public interface LoanReturnProxy {

    @GetMapping("/query/lastLoan")
    LoanReturn getLastLoan(@RequestParam("rr") String rr);

    @GetMapping("/query/lates")
    List<LoanReturn> getLates();
}
