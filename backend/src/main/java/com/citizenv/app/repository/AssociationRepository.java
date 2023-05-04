package com.citizenv.app.repository;

import com.citizenv.app.entity.Association;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociationRepository extends JpaRepository<Association, String> {
}
