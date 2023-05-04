package com.citizenv.app.repository;

import com.citizenv.app.entity.AdministrativeRegion;
import com.citizenv.app.entity.AdministrativeUnit;
import com.citizenv.app.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, String> {
    @Override
    List<Province> findAll();

    @Override
    Optional<Province> findById(String s);

    List<Province> findAllByAdministrativeUnit(AdministrativeUnit administrativeUnit);

    List<Province> findAllByAdministrativeRegion(AdministrativeRegion administrativeRegion);


}
