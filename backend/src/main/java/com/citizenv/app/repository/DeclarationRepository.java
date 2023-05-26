package com.citizenv.app.repository;

import com.citizenv.app.entity.Declaration;
import com.citizenv.app.payload.DeclarationDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface DeclarationRepository extends JpaRepository<Declaration, Integer> {

    @Query(value = "select date_format(d.start_time, '%Y-%m-%d'), date_format(d.end_time, '%Y-%m-%d'), d.status from declarations d " +
            "join users u on d.user_id = u.id where u.created_by = :userId", nativeQuery = true)
    List<Object[]> findAllDeclarationByCreatedBy(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query(value = "update declartions d set d.status = 'Đã hoàn thành' where d.user_id = :userId", nativeQuery = true)
    void setDeclarationStatusToCompleteByUserId(@Param("userId") Long userId);
}
