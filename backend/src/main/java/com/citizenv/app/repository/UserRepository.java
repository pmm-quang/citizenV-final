package com.citizenv.app.repository;

import com.citizenv.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findAllByCreatedBy(User user);

    @Transactional
    @Modifying
    @Query(value = "delete from users_roles where user_id = :userId and role_id = :roleId", nativeQuery = true)
    void deleteUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    @Transactional
    @Modifying
    @Query(value = "insert into users_roles (user_id, role_id) values (:userId, :roleId)", nativeQuery = true)
    void insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    @Query(value = "select u from User u where u.username like concat(:username, '%')")
    List<User> findAllSubordinateAccounts(@Param("username") String username);

    @Query(value = "select count(u.id) from users u join declarations d on u.id = d.user_id " +
            "where u.created_by = :createdById and d.status = 'Đang khai báo' group by u.created_by", nativeQuery = true)
    Long countSubUserAreDeclaring(@Param("createdById") Long createdById);

    @Query(value = "select u.created_by from users u where u.id = :userId", nativeQuery = true)
    Optional<Long> getCreateByOfUserByUserId(@Param("userId") Long userId);
}
