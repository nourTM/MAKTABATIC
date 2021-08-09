package com.maktabatic.booksmanagement.dao;

import com.maktabatic.booksmanagement.entites.Exemplaire;
import com.maktabatic.booksmanagement.entites.ExemplaireProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(excerptProjection = ExemplaireProjection.class)

public interface ExemplaireRepository extends JpaRepository<Exemplaire, Long> {

     List<Exemplaire> findExemplairesByNotice_IdNotice(Long IdNotice);
     Exemplaire findExemplaireByRfid(String rb);
     Long countExemplairesByNotice_IdNotice(Long id);

}
