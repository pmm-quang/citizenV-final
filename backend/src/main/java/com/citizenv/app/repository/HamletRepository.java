package com.citizenv.app.repository;

import com.citizenv.app.entity.AdministrativeUnit;
import com.citizenv.app.entity.Hamlet;
import com.citizenv.app.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HamletRepository extends JpaRepository<Hamlet, Long> {

    Optional<Hamlet> findByCode(String code);
    List<Hamlet> findAllByWard(Ward ward);
    List<Hamlet> findAllByAdministrativeUnit(AdministrativeUnit administrativeUnit);

}
