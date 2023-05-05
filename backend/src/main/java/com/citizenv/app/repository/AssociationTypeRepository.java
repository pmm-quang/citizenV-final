package com.citizenv.app.repository;

import com.citizenv.app.entity.AssociationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface AssociationTypeRepository extends JpaRepository<AssociationType, Integer> {
}
