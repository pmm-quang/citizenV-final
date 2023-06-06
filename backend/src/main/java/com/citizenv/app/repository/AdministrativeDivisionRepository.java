package com.citizenv.app.repository;

import com.citizenv.app.entity.AdministrativeDivision;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AdministrativeDivisionRepository extends JpaRepository<AdministrativeDivision, Long> {
    Optional<AdministrativeDivision> findByCode(String code);

    @Query(value = "select ad from AdministrativeDivision ad where ad.name = :divisionName and ad.code like concat(:codeOfSupDivision, '%')")
    Optional<AdministrativeDivision> findByName(@Param("divisionName") String divisionName,
                                                @Param("codeOfSupDivision") String codeOfSupDivision);

    @Cacheable("administrativeDivision")
    @Query(value = "select  ad.code, ad.name from administrative_divisions ad where ad.code like concat(:codeOfSupDivision, '__')", nativeQuery = true)
    List<Object[]> getAllCodeOfSubDivision(@Param("codeOfSupDivision") String codeOfSupDivision);
    @Transactional
    @Modifying
    @CacheEvict(value = "administrativeDivision", allEntries = true)
    @Query(value = "update administrative_divisions ad set ad.code = concat(:divisionCode, substr(ad.code, :startIndex)) where ad.code like concat(:oldDivisionCode, '%') ", nativeQuery = true)
    void updateCodeOfSubDivision(@Param("divisionCode") String divisionCode,
                                 @Param("startIndex") Integer startIndex,
                                 @Param("oldDivisionCode") String oldDivisionCode);
}