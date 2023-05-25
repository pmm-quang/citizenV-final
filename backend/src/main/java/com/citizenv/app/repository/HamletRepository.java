package com.citizenv.app.repository;

import com.citizenv.app.entity.AdministrativeUnit;
import com.citizenv.app.entity.Hamlet;
import com.citizenv.app.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HamletRepository extends JpaRepository<Hamlet, Long> {

    Optional<Hamlet> findByCode(String code);
    List<Hamlet> findAllByWard(Ward ward);
    List<Hamlet> findAllByAdministrativeUnit(AdministrativeUnit administrativeUnit);

    @Query(value = "select h from Hamlet h join Ward w on  h.ward.id = w.id join District d on w.district.id = d.id join Province p on d.province.id = p.id " +
            "where p.name = :province and d.name = :district and w.name = :ward and h.name = :hamlet")
    List<Hamlet> findHamletFromExcel(@Param("province") String province,
                                     @Param("district") String district,
                                     @Param("ward") String ward,
                                     @Param("hamlet") String hamlet);

}
