package com.example.doctorscarespringbootapplication.controller.doctor;

import com.example.doctorscarespringbootapplication.dao.*;
import com.example.doctorscarespringbootapplication.dto.*;
import com.example.doctorscarespringbootapplication.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/doctor")
public class DoctorPostHomepage {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private LikesRepository likesRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private SavedPostsRepository savedPostsRepository;

    @GetMapping("/post-homepage")
    public String postHomepage(Model model, Principal principal) {
        List<Posts> postsList = postsRepository.findAll();
        model.addAttribute("postsList", postsList);
        addCommonData(model, principal);
        return "doctor/doctor_post_homepage";
    }

    @PostMapping("/do-post-homepage")
    public String doPostHomepage(@ModelAttribute Posts posts, Model model, Principal principal) {

        DateTimeFormatter postDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter postTimeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

        LocalDateTime localDateTime = LocalDateTime.now();
        Date postDate = Date.valueOf(postDateFormat.format(localDateTime));
        Time postTime =  Time.valueOf(postTimeFormat.format(localDateTime));

        posts.setPostDate(postDate);
        posts.setPostTime(postTime);
        posts.setCoverPhoto("Default.jpg");
        postsRepository.save(posts);

        addCommonData(model, principal);
        return "redirect:/doctor/post-homepage";
    }

    @PostMapping("/process-like")
    @Transactional
    public ResponseEntity<Object> doLikePost(@RequestBody LikesDTO likesDTO, Model model, Principal principal) {
        String likesCountByUser = likesRepository.countAllByLikerIdAndPostsId(likesDTO.getLikerId(), Integer.parseInt(likesDTO.getPostId()));
        if (likesCountByUser.equals("0")) {
            Posts posts = postsRepository.findById(Integer.parseInt(likesDTO.getPostId()));
            List<Likes> likesList = likesRepository.findByPostsId(Integer.parseInt(likesDTO.getPostId()));
            likesList.add(new Likes(posts, likesDTO.getLikerId()));
            posts.setLikesList(likesList);
            postsRepository.save(posts);
            String likesCount = likesRepository.countAllByPostsId(Integer.parseInt(likesDTO.getPostId()));
            if (Integer.parseInt(likesCount) > 1) {
                ServiceResponse<String> response = new ServiceResponse<String>("success", "You and " + (Integer.parseInt(likesCount) - 1) + " people");
                return new ResponseEntity<Object>(response, HttpStatus.OK);
            }
                ServiceResponse<String> response = new ServiceResponse<String>("success", "You");
                return new ResponseEntity<Object>(response, HttpStatus.OK);
        }
        likesRepository.deleteByLikerIdAndPostsId(likesDTO.getLikerId(), Integer.parseInt(likesDTO.getPostId()));
        ServiceResponse<String> response = new ServiceResponse<String>("success", "0 people");
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    @PostMapping("/process-comment")
    @Transactional
    public ResponseEntity<Object> doCommentPost(@RequestBody CommentsDTO commentsDTO, Model model, Principal principal) {
        Posts posts = postsRepository.findById(Integer.parseInt(commentsDTO.getPostId()));
        List<Comments> commentsList = this.commentsRepository.findByPostsId(Integer.parseInt(commentsDTO.getPostId()));
        commentsList.add(new Comments(commentsDTO.getCommenterId(), commentsDTO.getCommenterName(), commentsDTO.getCommenterImage(), commentsDTO.getComment(), posts));
        posts.setCommentsList(commentsList);
        postsRepository.save(posts);
        ServiceResponse<String> response = new ServiceResponse<String>("success", commentsList.size()+"");
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    @PostMapping("/process-savepost")
    public ResponseEntity<Object> doSavePost(@RequestBody SavedPostsDTO savedPostsDTO, Model model, Principal principal) {
         Posts posts = postsRepository.findById(Integer.parseInt(savedPostsDTO.getPostId()));
         SavedPosts savedPosts = null;
        savedPosts = savedPostsRepository.findByPostsIdAndSaverId(Integer.parseInt(savedPostsDTO.getPostId()), savedPostsDTO.getSaverId());
        if (savedPosts == null) {
            SavedPosts savedPosts1 = new SavedPosts();
            savedPosts1.setSaverId(savedPostsDTO.getSaverId());
            savedPosts1.setPosts(posts);
            posts.setSavedPostsList(savedPosts1);
            postsRepository.save(posts);
            ServiceResponse<String> response = new ServiceResponse<String>("success", "Post Saved");
            return new ResponseEntity<Object>(response, HttpStatus.OK);
        }
        ServiceResponse<String> response = new ServiceResponse<String>("Success Already Saved", "Already Saved");
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    @PostMapping("/process-deletepost")
    @Transactional
    public ResponseEntity<Object> doDeletePost(@RequestBody DeletePostDTO deletePostDTO, Model model, Principal principal) {
        postsRepository.deleteById(Integer.parseInt(deletePostDTO.getPostId()));
        ServiceResponse<String> response = new ServiceResponse<String>("success", "Post Deleted Successfully!");
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    @PostMapping("/edit-post")
    public String editPost(@RequestParam("postId") String postId, Model model, Principal principal) {
        Posts post = postsRepository.findById(Integer.parseInt(postId));
        model.addAttribute("post", post);
        addCommonData(model, principal);
        return "doctor/doctor_edit_post";
    }

    @PostMapping("/process-save-editedpost")
    public String saveEditedPost(@RequestParam("postId") String postId, @RequestParam("postContent") String postContent, Model model, Principal principal) {
        Posts post = postsRepository.findById(Integer.parseInt(postId));
        post.setPostContent(postContent);
        postsRepository.save(post);
        model.addAttribute("postsaved", "true");
        List<Posts> postsList = postsRepository.findAll();
        model.addAttribute("postsList", postsList);
        addCommonData(model, principal);
        return "doctor/doctor_post_homepage";
    }

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userEmail = principal.getName();
        User user = this.userRepository.getUserByEmailNative(userEmail);
        model.addAttribute("user", user);
    }
}
