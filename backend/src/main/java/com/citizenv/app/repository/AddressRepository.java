package com.citizenv.app.repository;

import com.citizenv.app.entity.Address;
import com.citizenv.app.entity.AddressType;
import com.citizenv.app.entity.Citizen;
import com.citizenv.app.entity.Hamlet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    @Query("select a from Address a where a.addressType.id = ?1")
    List<Address> findByAddressType_Id(@NonNull Integer id);
    long countByAddressType_Id(@NonNull Integer id);
    List<Address> findAllByCitizen(Citizen citizen);
    List<Address> findAllByHamlet(Hamlet hamlet);

    List<Address> findAllByHamletAndAddressType(Hamlet hamlet, AddressType addressType);

}
