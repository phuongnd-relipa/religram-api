/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.service;

import com.relipa.religram.controller.bean.request.LikeBean;
import com.relipa.religram.controller.bean.request.PostRequestBean;
import com.relipa.religram.controller.bean.response.CommentBean;
import com.relipa.religram.controller.bean.response.PhotoBean;
import com.relipa.religram.controller.bean.response.PostBean;
import com.relipa.religram.controller.bean.response.UserInfoBean;
import com.relipa.religram.entity.Hashtag;
import com.relipa.religram.entity.Photo;
import com.relipa.religram.entity.Post;
import com.relipa.religram.entity.User;
import com.relipa.religram.repository.PostRepository;
import com.relipa.religram.util.ImageUtils;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PostServiceImpl extends AbstractServiceImpl<Post, Long> implements PostService {

    private static final Logger LOG = LoggerFactory.getLogger(PostServiceImpl.class);
    private final static int POSTS_PER_PAGE = 5;
    private final static int POSTS_OF_USER_PER_PAGE = 9;

    @Inject
    private PostRepository postRepository;

    @Inject
    private UserService userService;

    @Inject
    private CommentService commentService;

    @Inject
    private PhotoService photoService;

    @Inject
    private LikeService likeService;

    @Override
    @Transactional(readOnly = true)
    public List<PostBean> getAllPostByPage(Integer page, UserDetails userDetails) {
        List<Post> postList = postRepository.getPagePost(POSTS_PER_PAGE, (page - 1) * POSTS_PER_PAGE);
        UserInfoBean currentUser = userService.findUserByUserName(userDetails.getUsername());

        List<PostBean> posts = new ArrayList<>();
        postList.forEach(post -> {
            PostBean postBean = getPostBean(post, currentUser);

            List<CommentBean> comments =commentService.get3Comments(post.getId());
            postBean.setComments(comments.toArray(new CommentBean[comments.size()]));

            posts.add(postBean);
        });
        return posts;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostBean> getPostByUserid(Integer userId, Integer page) {

        List<Post> posts = postRepository.getPostsByUserId((long) userId, POSTS_OF_USER_PER_PAGE, (page - 1) * POSTS_OF_USER_PER_PAGE);
        return getPostBeans(posts);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostBean> getAllPostByHashtag(Integer page, String hashtag) {

        List<Post> posts = postRepository.searchByHashtag(hashtag, POSTS_OF_USER_PER_PAGE, (page - 1) * POSTS_OF_USER_PER_PAGE);
        return getPostBeans(posts);
    }

    @Override
    @Transactional(readOnly = true)
    public PostBean getPostDetail(Integer postId, UserDetails userDetails) throws NotFoundException {
        Post post = this.findById((long) postId);
        UserInfoBean currentUser = userService.findUserByUserName(userDetails.getUsername());

        PostBean postBean = getPostBean(post, currentUser);

        return postBean;
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getTotalPage() {
        Long totalPost = postRepository.count();

        return countPage(totalPost, POSTS_PER_PAGE);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getTotalPageByUserId(Integer userId) {
        Long totalPost = (long) postRepository.countByUserId(userId);

        return countPage(totalPost, POSTS_OF_USER_PER_PAGE);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer countPostByUserId(Integer userId) {
        return postRepository.countByUserId(userId);
    }

    @Override
    public Integer countPostByHashtag(String hashtag) {
        Long totalPost = (long)postRepository.countAllByHashtag(hashtag);

        return countPage(totalPost, POSTS_OF_USER_PER_PAGE);
    }

    @Override
    @Transactional
    public boolean createPost(PostRequestBean postRequestBean) {

        Post post = new Post();

        post.setContent(postRequestBean.getCaption());
        post.setCommentCount(0);
        post.setLikeCount(0);
        post.setUserId(postRequestBean.getUserId());

        Set<Hashtag> hashtagSet = commentService.insertHashtags(postRequestBean.getHashtags());
        post.setHashtags(hashtagSet);
        postRepository.save(post);

        // Save image
        String fileOutput = ImageUtils.getImageFileName(postRequestBean.getImage());
        ImageUtils.decodeToImage(postRequestBean.getImage(), fileOutput);

        Photo photo = new Photo();
        photo.setPostId(post.getId());
        photo.setPhotoUri(fileOutput);
        photoService.save(photo);

        return true;
    }

    private List<PostBean> getPostBeans(List<Post> posts) {
        List<PostBean> postBeans = new ArrayList<>();

        posts.forEach(post -> {
            PostBean postBean = new PostBean();
            BeanUtils.copyProperties(post, postBean);
            postBean.setId(post.getId());

            List<PhotoBean> photos = photoService.getPhotosByPostId(post.getId());
            postBean.setPhotos(photos.toArray(new PhotoBean[photos.size()]));

            postBeans.add(postBean);

        });

        return postBeans;
    }

    private PostBean getPostBean(Post post, UserInfoBean currentUser) {
        PostBean postBean = new PostBean();
        BeanUtils.copyProperties(post, postBean);
        postBean.setId(post.getId());

        User user = new User();
        try {
            user = userService.findById((long) post.getUserId());
        } catch (NotFoundException e) {
            LOG.error(e.getMessage(), e);
        }
        UserInfoBean userInfoBean = new UserInfoBean();
        BeanUtils.copyProperties(user, userInfoBean);
        userInfoBean.setId(user.getId());
        postBean.setUser(userInfoBean);

        List<PhotoBean> photos = photoService.getPhotosByPostId(post.getId());
        postBean.setPhotos(photos.toArray(new PhotoBean[photos.size()]));

        LikeBean like = likeService.getLikeByPostIdAndUserId(post.getId(), currentUser.getId());
        postBean.setIsLiked((like == null) ? false : true);

        return postBean;
    }

    private Integer countPage(Long totalPost, Integer postPerPage) {
        Integer totalPage;

        if (totalPost.intValue() % postPerPage > 0) {
            totalPage = totalPost.intValue() / postPerPage + 1;
        } else {
            totalPage = totalPost.intValue() / postPerPage;
        }
        return totalPage;
    }
}
