/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.service;

import com.relipa.religram.entity.Hashtag;
import com.relipa.religram.repository.HashtagRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;

@Service
public class HashtagServiceImpl extends AbstractServiceImpl<Hashtag, Long> implements HashtagService {

    @Inject
    private HashtagRepository hashtagRepository;

    @Override
    public Boolean existHashTagByName(String hashtag) {
        return hashtagRepository.existsHashtagByHashtag(hashtag);
    }

    @Override
    public Hashtag findByHashtag(String hashtag) {

        Hashtag tag = hashtagRepository.findByHashtag(hashtag).orElseThrow(() -> new EntityNotFoundException("Not found hashtag"));
        return tag;
    }
}
