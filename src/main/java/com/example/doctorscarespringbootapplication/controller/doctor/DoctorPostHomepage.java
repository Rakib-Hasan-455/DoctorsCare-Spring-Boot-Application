package com.example.doctorscarespringbootapplication.controller.doctor;

import com.example.doctorscarespringbootapplication.dao.LikesRepository;
import com.example.doctorscarespringbootapplication.dao.PostsRepository;
import com.example.doctorscarespringbootapplication.dao.UserRepository;
import com.example.doctorscarespringbootapplication.dto.LikesDTO;
import com.example.doctorscarespringbootapplication.dto.ServiceResponse;
import com.example.doctorscarespringbootapplication.entity.Likes;
import com.example.doctorscarespringbootapplication.entity.Posts;
import com.example.doctorscarespringbootapplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.time.DateTimeException;
import java.time.LocalDate;
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
        System.out.println(posts);
        postsRepository.save(posts);

        addCommonData(model, principal);
        return "redirect:/doctor/post-homepage";
    }

    @PostMapping("/process-like")
    @Transactional
    public ResponseEntity<Object> doPostHomepage(@RequestBody LikesDTO likesDTO, Model model, Principal principal) {
        System.out.println(likesDTO.getPostId() + " : likerId " + likesDTO.getLikerId());
        String likesCountByUser = likesRepository.countAllByLikerIdAndPostsId(likesDTO.getLikerId(), Integer.parseInt(likesDTO.getPostId()));
        System.out.println(likesCountByUser);
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



        @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userEmail = principal.getName();
        User user = this.userRepository.getUserByEmailNative(userEmail);
        model.addAttribute("user", user);
    }
}
