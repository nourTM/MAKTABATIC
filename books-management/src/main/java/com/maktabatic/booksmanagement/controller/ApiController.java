package com.maktabatic.booksmanagement.controller;


import com.maktabatic.booksmanagement.entites.Notice;
import com.maktabatic.booksmanagement.model.NoticeModel;
import com.maktabatic.booksmanagement.proxy.BooksProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class ApiController {

    @Autowired
    BooksProxy booksProxy;


    /* --------------------------------------------------------------------------------------------------- */
    /* ------------------------  Get notice info from the Books Api  ------------------------------------- */
    /* --------------------------------------------------------------------------------------------------- */
    @GetMapping("/book/{isbn}")
    public Notice getNoticeByISBN(@PathVariable("isbn") String isbn){
        Notice notice = new Notice();
        NoticeModel noticeModelApi = booksProxy.getBook(isbn);

        notice.setTitle(noticeModelApi.getTitle());
        notice.setSubtitle(noticeModelApi.getSubtitle());
        notice.setDescription(noticeModelApi.getDescription());
        notice.setPublishedDate(noticeModelApi.getPublishedDate());
        notice.setPublisher(noticeModelApi.getPublisher());
        notice.setPages(noticeModelApi.getPages());
        notice.setLink(noticeModelApi.getLink());
        notice.setImgLink(noticeModelApi.getImgLink());
        notice.setIsbn13(noticeModelApi.getIsbn13());
        notice.setIsbn10(noticeModelApi.getIsbn10());
        notice.setAuthors(noticeModelApi.getAuthors());
        notice.setCategories(noticeModelApi.getCategories());
        notice.setExemplaires(null);

        return notice;
    }
}
