package com.citizenv.app.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CustomAddressRepository {
    List<Object[]> countByProperties(Set<String> properties, String codeSqlSequence);
}
