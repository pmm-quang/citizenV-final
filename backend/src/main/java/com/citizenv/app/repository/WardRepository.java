package com.citizenv.app.repository;

import com.citizenv.app.entity.AdministrativeUnit;
import com.citizenv.app.entity.District;
import com.citizenv.app.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface WardRepository extends JpaRepository<Ward, Long> {
    List<Ward> findAllByDistrict(District district);
    List<Ward> findAllByAdministrativeUnit(AdministrativeUnit administrativeUnit);

    Optional<Ward> findByCode(String code);


    @Transactional
    @Modifying
    @Query(value = "SELECT admd.*, w.district_code FROM `administrative_divisions` admd join wards w on admd.code = w.code where left(w.code, 2) = '01';", nativeQuery = true)
    List<Ward> findByQuery();

    @Transactional
    @Modifying
    @Query(value = "select w from Ward w where w.district.code like concat(:province_code, '%') ")
    List<Ward> findAllByProvince_Code(@Param ("province_code") @NotNull String provinceCode);

    List<Ward> findAllByDistrict_Code(String districtCode);

    @Query(value = "select w from Ward  w where w.code like concat(:supDivisionCode, '%')")
    List<Ward> findAllBySupDivisionCode(@Param("supDivisionCode") @NotNull String supDivisionCode);
}
