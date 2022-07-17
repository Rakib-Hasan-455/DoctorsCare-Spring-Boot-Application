package com.example.doctorscarespringbootapplication.controller.patient;

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

import java.security.Principal;
import java.util.List;

import static com.example.doctorscarespringbootapplication.controller.doctor.DoctorPostHomepage.getObjectResponseEntityComment;

@Controller
@RequestMapping("/patient")
public class PatientDoctorsTIps {

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
        model.addAttribute("title", "Doctor Tips Homepage");
        List<Posts> postsList = postsRepository.findAll();
        model.addAttribute("postsList", postsList);
        addCommonData(model, principal);
        return "patient/patient_doctor_tips_homepage";
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

    @PostMapping("/process-savepost")
    public ResponseEntity<Object> doSavePost(@RequestBody SavedPostsDTO savedPostsDTO, Model model, Principal principal) {
        return getObjectResponseEntitySavePost(savedPostsDTO, postsRepository, savedPostsRepository);
    }


    @PostMapping("/process-unsavepost")
    @Transactional
    public ResponseEntity<Object> doUnsavePost(@RequestBody UnsavePostDTO unsavePostDTO, Model model, Principal principal) {
        return getObjectResponseEntityUnsavePost(unsavePostDTO, savedPostsRepository);
    }

    @GetMapping("/saved-tips-posts")
    public String savedTipsPosts(Model model, Principal principal) {
        patientSavedTips(model, principal, this.userRepository, savedPostsRepository);
        return "patient/patient_doctor_saved_tips";
    }

    public static void patientSavedTips(Model model, Principal principal, UserRepository userRepository, SavedPostsRepository savedPostsRepository) {
        model.addAttribute("title", "Doctor Saved Tips");
        model.addAttribute("postsaved", "true");
        String userEmail = principal.getName();
        User user = userRepository.getUserByEmailNative(userEmail);
        model.addAttribute("user", user);
        List<SavedPosts> savedPostsList = savedPostsRepository.findBySaverId(user.getId()+"");
        model.addAttribute("postsList", savedPostsList);
    }

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userEmail = principal.getName();
        User user = this.userRepository.getUserByEmailNative(userEmail);
        model.addAttribute("user", user);
    }

    public static ResponseEntity<Object> getObjectResponseEntityLike(@RequestBody LikesDTO likesDTO, LikesRepository likesRepository, PostsRepository postsRepository) {
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
        String likesCount = likesRepository.countAllByPostsId(Integer.parseInt(likesDTO.getPostId()));
        ServiceResponse<String> response = new ServiceResponse<String>("success", Integer.parseInt(likesCount) + " people");
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }


    public static ResponseEntity<Object> getObjectResponseEntitySavePost(@RequestBody SavedPostsDTO savedPostsDTO, PostsRepository postsRepository, SavedPostsRepository savedPostsRepository) {
        Posts posts = postsRepository.findById(Integer.parseInt(savedPostsDTO.getPostId()));
        SavedPosts savedPosts = null;
        savedPosts = savedPostsRepository.findByPostsIdAndSaverId(Integer.parseInt(savedPostsDTO.getPostId()), savedPostsDTO.getSaverId());
        if (savedPosts == null) {
            List<SavedPosts> savedPostsList = savedPostsRepository.findByPostsId(Integer.parseInt(savedPostsDTO.getPostId()));
            SavedPosts savedPosts1 = new SavedPosts();
            savedPosts1.setSaverId(savedPostsDTO.getSaverId());
            savedPosts1.setPosts(posts);
            savedPostsList.add(savedPosts1);
            posts.setSavedPostsList(savedPostsList);
            postsRepository.save(posts);
            ServiceResponse<String> response = new ServiceResponse<String>("success", "Post Saved");
            return new ResponseEntity<Object>(response, HttpStatus.OK);
        }
        ServiceResponse<String> response = new ServiceResponse<String>("Success Already Saved", "Already Saved");
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }


    public static ResponseEntity<Object> getObjectResponseEntityUnsavePost(@RequestBody UnsavePostDTO unsavePostDTO, SavedPostsRepository savedPostsRepository) {
        System.out.println("Post ID -> " + unsavePostDTO.getPostId() );
        System.out.println("Unsaver ID -> " + unsavePostDTO.getUnsaverId() );
        savedPostsRepository.deleteByPostsIdAndSaverId(Integer.parseInt(unsavePostDTO.getPostId()), unsavePostDTO.getUnsaverId());
        ServiceResponse<String> response = new ServiceResponse<String>("success", "Post Unsaved Successfully!");
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

}
