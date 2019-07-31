/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.repository;

import com.relipa.religram.entity.Hashtag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HashtagRepository extends CrudRepository<Hashtag, Long> {

    Boolean existsHashtagByHashtag(String hashtag);

    Optional<Hashtag> findByHashtag(String hashtag);
}
