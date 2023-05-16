package com.citizenv.app.repository;

import com.citizenv.app.entity.AdministrativeRegion;
import com.citizenv.app.entity.AdministrativeUnit;
import com.citizenv.app.entity.Province;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {
    @Override
    List<Province> findAll();

    Optional<Province> findByCode(String code);

    @Override
    Optional<Province> findById(Long s);

    Optional<Province> findByAdministrativeCode(String admCode);

    List<Province> findAllByAdministrativeUnit(AdministrativeUnit administrativeUnit);

    List<Province> findAllByAdministrativeRegion(AdministrativeRegion administrativeRegion);


}
