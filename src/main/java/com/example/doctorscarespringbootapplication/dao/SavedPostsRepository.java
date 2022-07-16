package com.example.doctorscarespringbootapplication.dao;

import com.example.doctorscarespringbootapplication.entity.SavedPosts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedPostsRepository extends JpaRepository<SavedPosts, Integer> {
    SavedPosts findByPostsIdAndSaverId(int id, String saverId);
}
