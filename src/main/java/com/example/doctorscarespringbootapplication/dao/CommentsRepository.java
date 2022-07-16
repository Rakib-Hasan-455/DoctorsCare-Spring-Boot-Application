package com.example.doctorscarespringbootapplication.dao;


import com.example.doctorscarespringbootapplication.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments, Integer> {
    List<Comments> findByPostsId(int parseInt);
}
