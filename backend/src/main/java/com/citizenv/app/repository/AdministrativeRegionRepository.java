package com.citizenv.app.repository;

import com.citizenv.app.entity.AdministrativeRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdministrativeRegionRepository extends JpaRepository<AdministrativeRegion, Integer> {
    List<AdministrativeRegion> findAll();

    @Override
    Optional<AdministrativeRegion> findById(Integer s);
}
