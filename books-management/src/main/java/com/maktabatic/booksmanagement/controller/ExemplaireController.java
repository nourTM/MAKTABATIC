package com.maktabatic.booksmanagement.controller;


import com.maktabatic.booksmanagement.dao.ExemplaireRepository;
import com.maktabatic.booksmanagement.dao.NoticeRepository;
import com.maktabatic.booksmanagement.entites.Exemplaire;
import com.maktabatic.booksmanagement.entites.ExemplaireProjection;
import com.maktabatic.booksmanagement.entites.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.config.Projection;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("exemplaireApi")

public class ExemplaireController  {

    @Autowired
    ExemplaireRepository exemplaireRepository;
    @Autowired
    NoticeRepository noticeRepository;

    @GetMapping("/{idNotice}/exemplaires")
    public List<Exemplaire> getExemplaires(@PathVariable("idNotice")  Long idNotice){
       return exemplaireRepository.findExemplairesByNotice_IdNotice(idNotice);
    }
    @PostMapping("/{idNotice}/addExemplaire")
    public Exemplaire addExemplaire(@PathVariable("idNotice") Long idNotice, @Valid @RequestBody Exemplaire exemplaire){
            exemplaire.setNotice(noticeRepository.findById(idNotice).get());
            exemplaireRepository.save(exemplaire);
            return exemplaire;
    }

    @PutMapping("/{idNotice}/updateExemplaire/{idExemplaire}")
    public Exemplaire updateNotice(@PathVariable("idNotice") Long idNotice,@PathVariable(value = "idExemplaire") Long idExemplaire,
                                   @Valid @RequestBody Exemplaire exemplaire)
    {
        if( exemplaireRepository.findById(idExemplaire).isPresent())
        {
            exemplaire.setIdExemplaire(idExemplaire);
            exemplaire.setNotice(noticeRepository.findById(idNotice).get());

            return exemplaireRepository.save(exemplaire);
        }
        return null;
    }

    @DeleteMapping("/notice/{idNotice}/deleteExemplaire/{idExemplaire}")
    public void deleteNotice(@PathVariable("idNotice") Long idNotice,@PathVariable("idExemplaire") Long idExemplaire)
    {
        if( exemplaireRepository.findById(idExemplaire).isPresent())
            exemplaireRepository.deleteById(idExemplaire);
    }

    @GetMapping("/bookexist/{rb}")
    public Exemplaire getBook(@PathVariable("rb") String rb){
        return exemplaireRepository.findExemplaireByRfid(rb);
    }


    @GetMapping("/idnotice/{rb}")
    Long getIdNotice(@PathVariable("rb") String rb){
        if (exemplaireRepository.findExemplaireByRfid(rb) != null){
            return exemplaireRepository.findExemplaireByRfid(rb).getNotice().getIdNotice();
        }
        return -1L;
    }
    @GetMapping("/nbbooks/{idNotice}")
    Long countExampTotal(@PathVariable("idNotice") Long id){
        return exemplaireRepository.countExemplairesByNotice_IdNotice(id);
    }
    @GetMapping("/verifyExemplaireRFID/{rfid}")
    public boolean verifyExemplaireRFID(@PathVariable("rfid") String rfid){
        if(exemplaireRepository.findExemplaireByRfid(rfid) != null){
            return true;
        }else{
            return false;
        }
    }
}
