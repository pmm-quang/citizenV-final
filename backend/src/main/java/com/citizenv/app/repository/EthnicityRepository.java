package com.citizenv.app.repository;

import com.citizenv.app.entity.Ethnicity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EthnicityRepository extends JpaRepository<Ethnicity, Integer> {
    Optional<Ethnicity> findById(String s);
    Optional<Ethnicity> findByName(String name);
}
