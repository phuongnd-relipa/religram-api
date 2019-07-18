package com.relipa.religram.service;

import com.relipa.religram.controller.bean.response.CommentBean;
import com.relipa.religram.controller.bean.response.PhotoBean;
import com.relipa.religram.controller.bean.response.PostBean;
import com.relipa.religram.controller.bean.response.UserInfoBean;
import com.relipa.religram.entity.Post;
import com.relipa.religram.entity.User;
import com.relipa.religram.repository.PostRepository;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl extends AbstractServiceImpl<Post, Long> implements PostService {

    private static final Logger LOG = LoggerFactory.getLogger(PostServiceImpl.class);
    private final static int POST_PER_PAGE = 5;

    @Inject
    private PostRepository postRepository;

    @Inject
    private UserService userService;

    @Inject
    private CommentService commentService;

    @Inject
    private PhotoService photoService;

    @Override
    @Transactional(readOnly = true)
    public List<PostBean> getAllPostByPage(Integer page) {
        List<Post> postList = postRepository.getPagePost(POST_PER_PAGE, (page - 1) * POST_PER_PAGE);

        List<PostBean> posts = new ArrayList<>();
        postList.forEach(post -> {
            PostBean postBean = new PostBean();
            BeanUtils.copyProperties(post, postBean);

            User user = new User();
            try {
                user = userService.findById((long) post.getUserId());
            } catch (NotFoundException e) {
                LOG.error(e.getMessage(), e);
            }
            UserInfoBean userInfoBean = new UserInfoBean();
            BeanUtils.copyProperties(user, userInfoBean);
            postBean.setUser(userInfoBean);

            List<CommentBean> comments =commentService.get3Comments(post.getId());
            postBean.setComments(comments.toArray(new CommentBean[comments.size()]));

            List<PhotoBean> photos = photoService.getPhotosByPostId(post.getId());
            postBean.setPhotos(photos.toArray(new PhotoBean[photos.size()]));

            posts.add(postBean);
        });
        return posts;
    }

    @Override
    public Integer getTotalPage() {
        long totalPost = postRepository.count();
        return (int) totalPost / POST_PER_PAGE + 1;

    }
}
