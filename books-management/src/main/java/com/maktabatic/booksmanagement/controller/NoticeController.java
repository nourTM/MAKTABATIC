package com.maktabatic.booksmanagement.controller;


import com.maktabatic.booksmanagement.dao.NoticeRepository;
import com.maktabatic.booksmanagement.entites.Notice;
import com.maktabatic.booksmanagement.model.NoticeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("noticeApi")

public class NoticeController {
    @Autowired
    NoticeRepository noticeRepository;


    @PostMapping("/add")
    public Notice createNotice(@Valid @RequestBody NoticeModel noticeModel)
    {
        Notice notice = new Notice();

        notice.setTitle(noticeModel.getTitle());
        notice.setSubtitle(noticeModel.getSubtitle());
        notice.setDescription(noticeModel.getDescription());
        notice.setPublishedDate(noticeModel.getPublishedDate());
        notice.setPublisher(noticeModel.getPublisher());
        notice.setPages(noticeModel.getPages());
        notice.setLink(noticeModel.getLink());
        notice.setImgLink(noticeModel.getImgLink());
        notice.setIsbn13(noticeModel.getIsbn13());
        notice.setIsbn10(noticeModel.getIsbn10());
        notice.setAuthors(noticeModel.getAuthors());
        notice.setCategories(noticeModel.getCategories());

        return noticeRepository.save(notice);

    }

    @PutMapping("/update/{id}")
    public Notice updateNotice(@PathVariable(value="id") Long idNotice,
                                @Valid @RequestBody Notice notice)
    {
        if( noticeRepository.findById(idNotice).isPresent())
        {
            notice.setIdNotice(idNotice);
            return noticeRepository.save(notice);
        }
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public void deleteNotice(@PathVariable(value="id") Long idNotice)
    {
        if( noticeRepository.findById(idNotice).isPresent())
            noticeRepository.deleteById(idNotice);
    }

    @GetMapping("/{isbn}")
    public Notice getByIsbn(@PathVariable("isbn") String isbn)
    {
        return  noticeRepository.findNoticeByIsbn13OrIsbn10(isbn,isbn);
    }

    @GetMapping("/notices")
    public ArrayList<Notice> getNotices(){
        return new ArrayList(noticeRepository.findAll());
    }


}
