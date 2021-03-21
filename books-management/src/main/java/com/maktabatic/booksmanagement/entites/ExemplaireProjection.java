package com.maktabatic.booksmanagement.entites;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "tocmd", types = Exemplaire.class)
public interface ExemplaireProjection {

    @Value("#{target.rfid}")
    String getRfid();

    @Value("#{target.notice.idNotice}")
    String getIdNotice();

    @Value("#{target.notice.title}")
    String getTitle();
}
