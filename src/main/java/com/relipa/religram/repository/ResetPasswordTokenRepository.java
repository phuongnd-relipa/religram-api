/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.repository;

import com.relipa.religram.entity.ResetPasswordToken;
import com.relipa.religram.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ResetPasswordTokenRepository extends CrudRepository<ResetPasswordToken, Long> {

    Optional<ResetPasswordToken> findFirstByUser(User user);

}
