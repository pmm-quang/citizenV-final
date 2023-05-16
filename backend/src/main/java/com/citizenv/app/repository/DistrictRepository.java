package com.citizenv.app.repository;

import com.citizenv.app.entity.AdministrativeUnit;
import com.citizenv.app.entity.District;
import com.citizenv.app.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {

    Optional<District> findByCode(String code);
    List<District> findAllByProvince(Province province);
    List<District> findAllByAdministrativeUnit(AdministrativeUnit administrativeUnit);
}
