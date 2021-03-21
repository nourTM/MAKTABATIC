package com.maktabatic.booksmanagement.model;

import lombok.Data;
import org.springframework.hateoas.CollectionModel;

import java.util.ArrayList;

@Data

public class NoticeModel {

    private String title;
    private String subtitle;
    private String description;
    private String publishedDate;
    private String publisher;
    private String pages;
    private String link;
    private String imgLink;
    private String isbn13;
    private String isbn10;
    private String authors;
    private String categories;
}
