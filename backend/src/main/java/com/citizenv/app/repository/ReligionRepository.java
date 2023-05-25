package com.citizenv.app.repository;

import com.citizenv.app.entity.Religion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReligionRepository extends JpaRepository<Religion, Integer> {
    Optional<Religion> findByName(String name);
}
