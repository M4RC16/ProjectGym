package com.projectgym.controller;

import com.projectgym.ApiResponse;
import com.projectgym.service.GalleryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/api/gallery")
public class GalleryController {
    private final GalleryService service;
    public GalleryController(GalleryService service) {
        this.service = service;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> addToGallery(@RequestParam("file") MultipartFile file, @RequestParam String text)  {
        service.saveToGallery(file, text);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Sikeres feltöltés!"));
    }

    @GetMapping("/getAll/image")
    public ResponseEntity<List<String>> getAllImages() {
        return ResponseEntity.ok(service.getAllImages());
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteImage(@RequestParam String imageUrl) {
        service.deleteImage(imageUrl);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Sikeres törlés!"));
    }

}
