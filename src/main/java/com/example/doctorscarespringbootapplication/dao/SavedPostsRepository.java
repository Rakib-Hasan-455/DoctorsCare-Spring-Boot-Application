package com.example.doctorscarespringbootapplication.dao;

import com.example.doctorscarespringbootapplication.entity.SavedPosts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavedPostsRepository extends JpaRepository<SavedPosts, Integer> {
    SavedPosts findByPostsIdAndSaverId(int id, String saverId);

    void deleteByPostsIdAndSaverId(int id, String saverId);

    List<SavedPosts> findBySaverId(String id);

    Page<SavedPosts> findBySaverId(String id, Pageable pageable);


    List<SavedPosts> findByPostsId(int id);

    long countBySaverId(String saverId);
}
