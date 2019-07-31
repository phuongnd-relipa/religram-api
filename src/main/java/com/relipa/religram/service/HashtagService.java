/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.service;

import com.relipa.religram.entity.Hashtag;

public interface HashtagService extends AbstractService<Hashtag, Long> {

    Boolean existHashTagByName(String hashtag);

    Hashtag findByHashtag(String hashtag);
}
