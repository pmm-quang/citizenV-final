package com.citizenv.app.repository;

import com.citizenv.app.entity.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen, String> {
    List<Citizen> findAll();

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
