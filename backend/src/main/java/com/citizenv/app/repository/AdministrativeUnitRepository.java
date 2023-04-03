package com.citizenv.app.repository;

import com.citizenv.app.entity.AdministrativeUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdministrativeUnitRepository extends JpaRepository<AdministrativeUnit, Integer> {
    List<AdministrativeUnit> findAll();

    @Override
    Optional<AdministrativeUnit> findById(Integer s);
}
