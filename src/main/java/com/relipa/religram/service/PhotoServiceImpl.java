package com.relipa.religram.service;

import com.relipa.religram.controller.bean.response.PhotoBean;
import com.relipa.religram.entity.Photo;
import com.relipa.religram.repository.PhotoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service
public class PhotoServiceImpl extends AbstractServiceImpl<Photo, Long> implements PhotoService {

    @Inject
    private PhotoRepository photoRepository;

    @Override
    public List<PhotoBean> getPhotosByPostId(Long postId) {
        List<Photo> photoList = photoRepository.getPhotosByPostId(postId);
        List<PhotoBean> photos = new ArrayList<>();

        photoList.forEach(photo -> {
            PhotoBean photoBean = new PhotoBean();
            BeanUtils.copyProperties(photo, photoBean);
            photoBean.setId(photo.getId());

            photos.add(photoBean);
        });
        return photos;
    }
}
