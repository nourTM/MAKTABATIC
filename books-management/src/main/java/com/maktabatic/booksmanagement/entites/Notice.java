package com.maktabatic.booksmanagement.entites;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNotice ;

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

    @JsonIgnore
    @OneToMany(mappedBy = "notice")
    private List<Exemplaire>  exemplaires;


}
