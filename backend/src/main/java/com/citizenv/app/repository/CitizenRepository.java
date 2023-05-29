package com.citizenv.app.repository;

import com.citizenv.app.entity.Citizen;
import com.citizenv.app.payload.custom.CustomCitizenResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen, Long> {

    Optional<Citizen> findByNationalId(String nationalId);

    @Modifying
    @Transactional
    @Query(value = "update Citizen c set c = :citizen where c.id = :id")
    void updateById(String id, Citizen citizen);

    List<Citizen> findAll();

    Optional<Citizen> findById(String id);

    List<Citizen> findByAddresses_Hamlet_code(String hamletCode);
    @Query(value = "select c from Citizen c " +
            "join Address a on c.id = a.citizen.id " +
            "join Hamlet h on h.id = a.hamlet.id " +
            "where h.code = :hamletCode and a.addressType.id = :addressTypeId")
    List<Citizen> findAllByHamletCode(@Param("hamletCode") String hamletCode,
                                      @Param("addressTypeId") Integer addressTypeId);
    @Query(value = "select new com.citizenv.app.payload.custom.CustomCitizenResponse(c.nationalId, c.name, c.sex) from Citizen c " +
            "join Address a on c.id = a.citizen.id " +
            "join Hamlet h on h.id = a.hamlet.id " +
            "where h.code = :hamletCode and a.addressType.id = :addressTypeId")
    Page<CustomCitizenResponse> findAllByHamletCode(@Param("hamletCode") String hamletCode,
                                                    @Param("addressTypeId") Integer addressTypeId, Pageable pageable);
    @Query(value = "select c from Citizen c " +
            "join Address a on c.id = a.citizen.id " +
            "join Hamlet h on h.id = a.hamlet.id " +
            "join Ward w on w.id = h.ward.id " +
            "where w.code = :wardCode and a.addressType.id = :addressTypeId")
    List<Citizen> findAllByWardCode(@Param("wardCode") String wardCode,
                                    @Param("addressTypeId") Integer addressTypeId);

    @Query(value = "select new com.citizenv.app.payload.custom.CustomCitizenResponse(c.nationalId, c.name, c.sex) from Citizen c " +
            "join Address a on c.id = a.citizen.id " +
            "join Hamlet h on h.id = a.hamlet.id " +
            "join Ward w on w.id = h.ward.id " +
            "where w.code = :wardCode and a.addressType.id = :addressTypeId")
    Page<CustomCitizenResponse> findAllByWardCode(@Param("wardCode") String wardCode,
                                    @Param("addressTypeId") Integer addressTypeId, Pageable pageable);
    @Query(value = "select c from Citizen c " +
            "join Address a on c.id = a.citizen.id " +
            "join Hamlet h on h.id = a.hamlet.id " +
            "join Ward w on w.id = h.ward.id " +
            "join District d on d.id = w.district.id " +
            "where d.code = :districtCode and a.addressType.id = :addressTypeId")
    List<Citizen> findAllByDistrictCode(@Param("districtCode") String districtCode,
                                        @Param("addressTypeId") Integer addressTypeId);

    @Query(value = "select new com.citizenv.app.payload.custom.CustomCitizenResponse(c.nationalId, c.name, c.sex) from Citizen c " +
            "join Address a on c.id = a.citizen.id " +
            "join Hamlet h on h.id = a.hamlet.id " +
            "join Ward w on w.id = h.ward.id " +
            "join District d on d.id = w.district.id " +
            "where d.code = :districtCode and a.addressType.id = :addressTypeId")
    Page<CustomCitizenResponse> findAllByDistrictCode(@Param("districtCode") String districtCode,
                                        @Param("addressTypeId") Integer addressTypeId, Pageable pageable);
    @Query(value = "select c from Citizen c " +
            "join Address a on c.id = a.citizen.id " +
            "join Hamlet h on h.id = a.hamlet.id " +
            "join Ward w on w.id = h.ward.id " +
            "join District d on d.id = w.district.id " +
            "join Province p on p.id = d.province.id " +
            "where p.code = :provinceCode and a.addressType.id = :addressTypeId")
    List<Citizen> findAllByProvinceCode(@Param("provinceCode") String provinceCode,
                                        @Param("addressTypeId") Integer addressTypeId);

    @Query(value = "select c from Citizen c " +
            "join Address a on c.id = a.citizen.id " +
            "join Hamlet h on h.id = a.hamlet.id " +
            "join Ward w on w.id = h.ward.id " +
            "join District d on d.id = w.district.id " +
            "join Province p on p.id = d.province.id " +
            "where p.code = :provinceCode and a.addressType.id = :addressTypeId")
    Page<Citizen> findAllByProvinceCode(@Param("provinceCode") String provinceCode,
                                        @Param("addressTypeId") Integer addressTypeId, Pageable pageable);


    /*@Query("SELECT new com.citizenv.app.entity.custom.Population(p.name, COUNT(id)) " +
            "FROM Citizen c join Province p on p.id = c.province.id " +
            "GROUP BY c.province.code")
    List<Population> getPopulationGroupByProvince();

    @Query("SELECT new com.citizenv.app.entity.custom.Population(d.name, COUNT(id)) " +
            "FROM Citizen c join District d on d.id = c.district.id " +
            "GROUP BY c.district.code")
    List<Population> getPopulationGroupByDistrict();

    @Query("SELECT new com.citizenv.app.entity.custom.Population(w.name, COUNT(id)) " +
            "FROM Citizen c join Ward w on w.id = c.ward.id " +
            "GROUP BY c.ward.code")
    List<Population> getPopulationGroupByWard();*/

    @Query(value = "select distinct * from citizens c " +
            "where (:c_name is null or c.name = :c_name)\n" +
            "and (:c_date_of_birth is null or c. = :c_date_of_birth)\n" +
            "and (:c_blood_type is null or c.blood_type = :c_blood_type)\n" +
            "and (:c_sex is null or c.sex = :c_sex)\n" +
            "and (:c_marital_status is null or c.marital_status = :c_marital_status)\n" +
            "and (:c_ethnicity_id is null or c.ethnicity_id = :c_ethnicity_id)\n" +
            "and (:c_other_nationality is null or c.other_nationality = :c_other_nationality)\n" +
            "and (:c_religion_id is null or c.religion_id = :c_religion_id)\n" +
            "and (:c_job is null or c.job = :c_job) \n" +
            "and (:c_education_level is null or c.educational_level = :c_education_level) \n" +
            "and (:c_join_condition is null or :c_join_condition)", nativeQuery = true)
    List<Object[]> search(
                          @Param("c_name") String c_name,
                          @Param("c_date_of_birth") LocalDate c_date_of_birth,
                          @Param("c_blood_type") String c_blood_type,
                          @Param("c_sex") String c_sex,
                          @Param("c_marital_status") String c_marital_status,
                          @Param("c_ethnicity_id") Integer c_ethnicity_id,
                          @Param("c_other_nationality") String c_other_nationality,
                          @Param("c_religion_id") Integer c_religion_id,
                          @Param("c_job") String c_job,
                          @Param("c_education_level") String c_education_level,
                          @Param("c_join_condition") String c_join_condition);

    @Query(value = "SELECT DISTINCT c " +
            "FROM Citizen c " +
            "JOIN c.addresses a1 " +
            "JOIN c.addresses a2 " +
            "JOIN c.associations ass1 " +
            "where a1.hamlet.id = 171 " +
            "AND a1.addressType.id = 1 " +
            "AND a2.hamlet.id = 171 " +
            "AND a2.addressType.id = 2 ")
    List<Citizen> getAllCustom();
}


