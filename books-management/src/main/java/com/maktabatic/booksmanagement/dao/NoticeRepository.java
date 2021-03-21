package com.maktabatic.booksmanagement.dao;

import com.maktabatic.booksmanagement.entites.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource
public interface NoticeRepository extends JpaRepository<Notice, Long> {

     Notice findNoticeByIsbn13OrIsbn10(String isbn13, String insb10);
}
