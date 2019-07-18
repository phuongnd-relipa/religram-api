package com.relipa.religram.service;

import com.relipa.religram.controller.bean.response.PostBean;
import com.relipa.religram.entity.Post;

import java.util.List;

public interface PostService extends AbstractService<Post,Long> {

    List<PostBean> getAllPostByPage(Integer page);

    Integer getTotalPage();

}
