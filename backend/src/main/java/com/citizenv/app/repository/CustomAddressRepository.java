package com.citizenv.app.repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface CustomAddressRepository {
    List<Map<String, Object>> countByProperties(Set<String> properties, String codeSqlSequence);
}
