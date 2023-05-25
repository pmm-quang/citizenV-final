package com.citizenv.app.repository;

import com.citizenv.app.entity.Address;
import com.citizenv.app.entity.AddressType;
import com.citizenv.app.entity.Citizen;
import com.citizenv.app.entity.Hamlet;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer>, CustomAddressRepository {
    @Query("select a from Address a where a.addressType.id = ?1")
    List<Address> findByAddressType_Id(@NonNull Integer id);

    @Query("select a from Address a where a.addressType.id = ?1 and a.hamlet.code like CONCAT(?2, '%') ")
    List<Address> findByAddressType_IdAndDivisionCode(@NonNull Integer id, @NonNull String code);
    List<Address> findAllByCitizen(Citizen citizen);
    List<Address> findAllByHamlet(Hamlet hamlet);



    List<Address> findAllByHamletAndAddressType(Hamlet hamlet, AddressType addressType);

}
