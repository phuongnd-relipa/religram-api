/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.repository;

import com.relipa.religram.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Long countByUsernameAndIdNot(String username, Long Id);

    @Query(value = "select * from users where username like %:name% order by created_at desc limit :limit offset :offset", nativeQuery = true)
    List<User> searchUsersByName(@Param("name") String name, @Param("limit") Integer limit, @Param("offset") Integer offset);
}