package com.citizenv.app.repository;

import com.citizenv.app.entity.AdministrativeDivision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministrativeDivisionRepository extends JpaRepository<AdministrativeDivision, Long> {
    Optional<AdministrativeDivision> findByCode(String code);
}