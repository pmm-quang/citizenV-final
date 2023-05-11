package com.citizenv.app.repository;

import com.citizenv.app.entity.Citizen;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen, String> {

    @Modifying
    @Transactional
    @Query(value = "update Citizen c set c = :citizen where c.id = :id")
    void updateById(String id, Citizen citizen);

    List<Citizen> findAll();

    Optional<Citizen> findById(String id);

    List<Citizen> findByAddresses_Hamlet_code(String hamletCode);
    @Query(value = "select c from Citizen c " +
            "join Address a on c.id = a.citizen.id " +
            "join Hamlet h on h.code = a.hamlet.code " +
            "where h.code = :hamletCode and a.addressType.id = :addressTypeId")
    List<Citizen> findAllByHamletCode(@Param("hamletCode") String hamletCode,
                                      @Param("addressTypeId") Integer addressTypeId);
    @Query(value = "select c from Citizen c " +
            "join Address a on c.id = a.citizen.id " +
            "join Hamlet h on h.code = a.hamlet.code " +
            "join Ward w on w.code = h.ward.code " +
            "where w.code = :wardCode and a.addressType.id = :addressTypeId")
    List<Citizen> findAllByWardCode(@Param("wardCode") String wardCode,
                                    @Param("addressTypeId") Integer addressTypeId);
    @Query(value = "select c from Citizen c " +
            "join Address a on c.id = a.citizen.id " +
            "join Hamlet h on h.code = a.hamlet.code " +
            "join Ward w on w.code = h.ward.code " +
            "join District d on d.code = w.district.code " +
            "where d.code = :districtCode and a.addressType.id = :addressTypeId")
    List<Citizen> findAllByDistrictCode(@Param("districtCode") String districtCode,
                                        @Param("addressTypeId") Integer addressTypeId);
    @Query(value = "select c from Citizen c " +
            "join Address a on c.id = a.citizen.id " +
            "join Hamlet h on h.code = a.hamlet.code " +
            "join Ward w on w.code = h.ward.code " +
            "join District d on d.code = w.district.code " +
            "join Province p on p.code = d.province.code " +
            "where p.code = :provinceCode and a.addressType.id = :addressTypeId")
    List<Citizen> findAllByProvinceCode(@Param("provinceCode") String provinceCode,
                                        @Param("addressTypeId") Integer addressTypeId);


    /*@Query("SELECT new com.citizenv.app.entity.custom.Population(p.name, COUNT(id)) " +
            "FROM Citizen c join Province p on p.code = c.province.code " +
            "GROUP BY c.province.code")
    List<Population> getPopulationGroupByProvince();

    @Query("SELECT new com.citizenv.app.entity.custom.Population(d.name, COUNT(id)) " +
            "FROM Citizen c join District d on d.code = c.district.code " +
            "GROUP BY c.district.code")
    List<Population> getPopulationGroupByDistrict();

    @Query("SELECT new com.citizenv.app.entity.custom.Population(w.name, COUNT(id)) " +
            "FROM Citizen c join Ward w on w.code = c.ward.code " +
            "GROUP BY c.ward.code")
    List<Population> getPopulationGroupByWard();*/
}
