package com.example.doctorscarespringbootapplication.dao;

import com.example.doctorscarespringbootapplication.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes, Integer> {

    List<Likes> findByPostsId(int id);

    String countAllByPostsId(int id);

    String countAllByLikerIdAndPostsId(String likerId, int posts_id);

    void deleteByLikerIdAndPostsId(String likerId, int posts_id);
}
