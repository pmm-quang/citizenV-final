package com.citizenv.app.repository;

import com.citizenv.app.entity.Address;
import com.citizenv.app.entity.AddressType;
import com.citizenv.app.entity.Citizen;
import com.citizenv.app.entity.Hamlet;
import com.citizenv.app.entity.custom.Population;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    List<Address> findAllByCitizen(Citizen citizen);
    List<Address> findAllByHamlet(Hamlet hamlet);

    List<Address> findAllByHamletAndAddressType(Hamlet hamlet, AddressType addressType);

    /*@Query("SELECT new com.citizenv.app.entity.custom.Population(p.name, COUNT(id)) " +
            "FROM Address add join Province p on p.code = .province.code " +
            "GROUP BY c.province.code")
    List<Population> countByAddressTypeId(Integer addressTypeId);*/
}
