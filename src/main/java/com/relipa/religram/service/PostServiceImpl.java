package com.relipa.religram.service;

import com.relipa.religram.controller.bean.request.LikeBean;
import com.relipa.religram.controller.bean.request.PostRequestBean;
import com.relipa.religram.controller.bean.response.CommentBean;
import com.relipa.religram.controller.bean.response.PhotoBean;
import com.relipa.religram.controller.bean.response.PostBean;
import com.relipa.religram.controller.bean.response.UserInfoBean;
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

    @Inject
    private LikeService likeService;

    @Inject HashtagService hashtagService;

    @Override
    @Transactional(readOnly = true)
    public List<PostBean> getAllPostByPage(Integer page, UserDetails userDetails) {
        List<Post> postList = postRepository.getPagePost(POST_PER_PAGE, (page - 1) * POST_PER_PAGE);
        UserInfoBean currentUser = userService.findUserByUserName(userDetails.getUsername());

        List<PostBean> posts = new ArrayList<>();
        postList.forEach(post -> {
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

            List<CommentBean> comments =commentService.get3Comments(post.getId());
            postBean.setComments(comments.toArray(new CommentBean[comments.size()]));

            List<PhotoBean> photos = photoService.getPhotosByPostId(post.getId());
            postBean.setPhotos(photos.toArray(new PhotoBean[photos.size()]));

            LikeBean like = likeService.getLikeByPostIdAndUserId(post.getId(), currentUser.getId());
            postBean.setIsLiked((like == null) ? false : true);

            posts.add(postBean);
        });
        return posts;
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getTotalPage() {
        long totalPost = postRepository.count();
        return (int) totalPost / POST_PER_PAGE + 1;

    }

    @Override
    @Transactional
    public boolean createPost(PostRequestBean postRequestBean) {

        // Save image
        String fileOutput = ImageUtils.getImageFileName(postRequestBean.getImage());
        ImageUtils.decodeToImage(postRequestBean.getImage(), fileOutput);
        // TODO: coding insert new post + image hashtags

        List<String> hashtags = postRequestBean.getHashtags();
        hashtags.forEach(hashtag -> {
            if (hashtagService.existHashTagByName(hashtag)) {
                // TODO get ID and insert into hashtag_post
            } else {
                // TODO save hashtag and get ID to insert into hashtag_post
            }
        });


        return true;
    }
}
