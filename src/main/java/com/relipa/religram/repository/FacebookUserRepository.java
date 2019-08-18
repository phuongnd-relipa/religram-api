/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.repository;

import com.relipa.religram.entity.FacebookUser;
import org.springframework.data.repository.CrudRepository;

public interface FacebookUserRepository extends CrudRepository<FacebookUser, Long> {
}
