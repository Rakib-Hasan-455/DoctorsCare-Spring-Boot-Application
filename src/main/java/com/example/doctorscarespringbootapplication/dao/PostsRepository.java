package com.example.doctorscarespringbootapplication.dao;

import com.example.doctorscarespringbootapplication.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts, Integer> {

    Posts findById(int postId);

}
