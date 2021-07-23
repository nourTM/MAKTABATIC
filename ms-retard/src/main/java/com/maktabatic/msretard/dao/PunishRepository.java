package com.maktabatic.msretard.dao;

import com.maktabatic.msretard.entities.KeyPunish;
import com.maktabatic.msretard.entities.Punish;
import com.maktabatic.msretard.entities.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface PunishRepository extends JpaRepository<Punish, KeyPunish> {
    List<Punish> findPunishesByState(State state);
    List<Punish> findPunishesByKeyPunish_Rr(String rr);
}
