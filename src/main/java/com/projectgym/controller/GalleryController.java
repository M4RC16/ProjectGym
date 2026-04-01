package com.projectgym.controller;

import com.projectgym.service.GalleryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/api/auth")
public class GalleryController {
    private final GalleryService service;
    public GalleryController(GalleryService service) {
        this.service = service;
    }

    @PostMapping("add/galery")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addToGallery(@RequestParam("file") MultipartFile file, @RequestParam String text)  {
        service.saveToGallery(file, text);
        return ResponseEntity.ok("Sikeres feltöltés");
    }

    @GetMapping("getAll/image")
    public ResponseEntity<List<String>> getAllImages() {
        return ResponseEntity.ok(service.getAllImages());
    }

    @DeleteMapping("delete/image")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteImage(@RequestParam String imageUrl) {
        service.deleteImage(imageUrl);
        return ResponseEntity.ok("Sikeres törlés");
    }

}
