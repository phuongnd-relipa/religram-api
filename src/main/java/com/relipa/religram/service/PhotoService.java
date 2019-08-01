/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.service;

import com.relipa.religram.controller.bean.response.PhotoBean;
import com.relipa.religram.entity.Photo;

import java.util.List;

public interface PhotoService extends AbstractService<Photo,Long> {

    List<PhotoBean> getPhotosByPostId(Long postId);
}
