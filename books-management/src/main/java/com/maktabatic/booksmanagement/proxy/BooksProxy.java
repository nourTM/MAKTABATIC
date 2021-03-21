package com.maktabatic.booksmanagement.proxy;

import com.maktabatic.booksmanagement.model.NoticeModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="books-api")
public interface BooksProxy {
    @GetMapping("/api/book/{isbn}")
    NoticeModel getBook(@PathVariable("isbn") String isbn);
}
