package com.citizenv.app.repository;

import com.citizenv.app.entity.AdministrativeUnit;
import com.citizenv.app.entity.District;
import com.citizenv.app.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WardRepository extends JpaRepository<Ward, Long> {
    List<Ward> findAllByDistrict(District district);
    List<Ward> findAllByAdministrativeUnit(AdministrativeUnit administrativeUnit);

    Optional<Ward> findByCode(String code);

    @Modifying
    @Query("update Ward w set w.code = :code, w.name = :name, w.district = :district, w.administrativeUnit = :admUnit where w.code = :updateCode")
    void update(String updateCode, String code, String name, District district, AdministrativeUnit admUnit);

    @Query(value = "SELECT admd.*, w.district_code FROM `administrative_divisions` admd join wards w on admd.code = w.code where left(w.code, 2) = '01';", nativeQuery = true)
    List<Ward> findByQuery();
}
