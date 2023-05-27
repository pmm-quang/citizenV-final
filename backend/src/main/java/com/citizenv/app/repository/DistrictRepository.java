package com.citizenv.app.repository;

import com.citizenv.app.entity.AdministrativeUnit;
import com.citizenv.app.entity.District;
import com.citizenv.app.entity.Province;
import com.citizenv.app.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {

    Optional<District> findByCode(String code);
    List<District> findAllByProvince(Province province);
    List<District> findAllByAdministrativeUnit(AdministrativeUnit administrativeUnit);

    @Query(value = "select d from District  d where d.code like concat(:supDivisionCode, '%')")
    List<District> findAllBySupDivisionCode(@Param("supDivisionCode") @NotNull String supDivisionCode);
}
