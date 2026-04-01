package com.projectgym.repository;

import com.projectgym.model.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Integer> {
    Optional<Gallery> findByImageUrl(String imageUrl);
}
