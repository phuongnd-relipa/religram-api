/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.service;

import com.relipa.religram.controller.bean.request.PostRequestBean;
import com.relipa.religram.controller.bean.response.PostBean;
import com.relipa.religram.entity.Post;
import javassist.NotFoundException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface PostService extends AbstractService<Post,Long> {

    List<PostBean> getAllPostByPage(Integer page, UserDetails userDetails);

    List<PostBean> getPostByUserid(Integer page, Integer userId);

    PostBean getPostDetail(Integer postId, UserDetails userDetails) throws NotFoundException;

    Integer getTotalPage();

    Integer getTotalPageByUserId(Integer userId);

    Integer countPostByUserId(Integer userId);

    boolean createPost(PostRequestBean postRequestBean);

}
