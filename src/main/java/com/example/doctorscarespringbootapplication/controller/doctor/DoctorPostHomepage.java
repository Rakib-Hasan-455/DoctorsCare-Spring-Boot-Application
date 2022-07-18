package com.example.doctorscarespringbootapplication.controller.doctor;

import com.example.doctorscarespringbootapplication.controller.patient.PatientDoctorsTIps;
import com.example.doctorscarespringbootapplication.dao.*;
import com.example.doctorscarespringbootapplication.dto.*;
import com.example.doctorscarespringbootapplication.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.doctorscarespringbootapplication.controller.patient.PatientDoctorsTIps.*;

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

    @GetMapping("/post-homepage/{page}")
    public String postHomepage(@PathVariable("page") Integer page, Model model, Principal principal) {
        model.addAttribute("title", "Doctor Tips Homepage");
        Pageable pageable = PageRequest.of(page-1, 5);
        Page<Posts> postsList = postsRepository.findAllByOrderByIdDesc(pageable);
        model.addAttribute("postsList", postsList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postsList.getTotalPages());
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
        model.addAttribute("posted", true);
        addCommonData(model, principal);
        return "redirect:/doctor/post-homepage/1";
    }

    @PostMapping("/process-like")
    @Transactional
    public ResponseEntity<Object> doLikePost(@RequestBody LikesDTO likesDTO, Model model, Principal principal) {
        return getObjectResponseEntityLike(likesDTO, likesRepository, postsRepository);
    }

    @PostMapping("/process-comment")
    @Transactional
    public ResponseEntity<Object> doCommentPost(@RequestBody CommentsDTO commentsDTO, Model model, Principal principal) {
        return getObjectResponseEntityComment(commentsDTO, postsRepository, this.commentsRepository);
    }

    public static ResponseEntity<Object> getObjectResponseEntityComment(@RequestBody CommentsDTO commentsDTO, PostsRepository postsRepository, CommentsRepository commentsRepository) {
        Posts posts = postsRepository.findById(Integer.parseInt(commentsDTO.getPostId()));
        List<Comments> commentsList = commentsRepository.findByPostsId(Integer.parseInt(commentsDTO.getPostId()));
        commentsList.add(new Comments(commentsDTO.getCommenterId(), commentsDTO.getCommenterName(), commentsDTO.getCommenterImage(), commentsDTO.getComment(), posts));
        posts.setCommentsList(commentsList);
        postsRepository.save(posts);
        ServiceResponse<String> response = new ServiceResponse<String>("success", commentsList.size()+"");
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    @PostMapping("/process-savepost")
    public ResponseEntity<Object> doSavePost(@RequestBody SavedPostsDTO savedPostsDTO, Model model, Principal principal) {
        return getObjectResponseEntitySavePost(savedPostsDTO, postsRepository, savedPostsRepository);
    }

    @PostMapping("/process-deletepost")
    @Transactional
    public ResponseEntity<Object> doDeletePost(@RequestBody DeletePostDTO deletePostDTO, Model model, Principal principal) {
        postsRepository.deleteById(Integer.parseInt(deletePostDTO.getPostId()));
        ServiceResponse<String> response = new ServiceResponse<String>("success", "Post Deleted Successfully!");
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    @PostMapping("/process-unsavepost")
    @Transactional
    public ResponseEntity<Object> doUnsavePost(@RequestBody UnsavePostDTO unsavePostDTO, Model model, Principal principal) {
        return getObjectResponseEntityUnsavePost(unsavePostDTO, savedPostsRepository);
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
        model.addAttribute("title", "Doctor Tips Homepage");
        Posts post = postsRepository.findById(Integer.parseInt(postId));
        post.setPostContent(postContent);
        postsRepository.save(post);
        model.addAttribute("postsaved", "true");
        List<Posts> postsList = postsRepository.findAll();
        model.addAttribute("postsList", postsList);
        addCommonData(model, principal);
        return "redirect:/doctor/post-homepage/1";
    }

    @GetMapping("/saved-tips-posts/{page}")
    public String savedTipsPosts(@PathVariable("page") Integer page, Model model, Principal principal) {
        model.addAttribute("title", "Doctor Saved Tips");
        model.addAttribute("postsaved", "true");
        String userEmail = principal.getName();
        User user = userRepository.getUserByEmailNative(userEmail);
        model.addAttribute("user", user);
        Pageable pageable = PageRequest.of(page-1, 5);
        Page<SavedPosts> savedPostsList = savedPostsRepository.findBySaverId(user.getId()+"", pageable);
        model.addAttribute("postsList", savedPostsList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", savedPostsList.getTotalPages());
        return "doctor/doctor_saved_tips";
    }

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userEmail = principal.getName();
        User user = this.userRepository.getUserByEmailNative(userEmail);
        model.addAttribute("user", user);
    }
}
