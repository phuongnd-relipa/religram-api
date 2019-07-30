/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.service;

import com.relipa.religram.entity.Hashtag;
import com.relipa.religram.repository.HashtagRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class HashtagServiceImpl extends AbstractServiceImpl<Hashtag, Long> implements HashtagService {

    @Inject
    private HashtagRepository hashtagRepository;

    @Override
    public Boolean existHashTagByName(String hashtag) {
        return hashtagRepository.existsHashtagByHashtag(hashtag);
    }
}
