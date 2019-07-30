package com.relipa.religram.service;

import com.relipa.religram.controller.bean.request.PostRequestBean;
import com.relipa.religram.controller.bean.response.PostBean;
import com.relipa.religram.entity.Post;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface PostService extends AbstractService<Post,Long> {

    List<PostBean> getAllPostByPage(Integer page, UserDetails userDetails);

    Integer getTotalPage();

    boolean createPost(PostRequestBean postRequestBean);

}
