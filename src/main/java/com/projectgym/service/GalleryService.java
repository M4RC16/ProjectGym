package com.projectgym.service;

import com.projectgym.exception.BadRequestException;
import com.projectgym.model.Gallery;
import com.projectgym.repository.GalleryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class GalleryService {

    @Value("${file.upload-dir-gallery}")
    private String uploadDir;

    private final GalleryRepository repository;
    public GalleryService(GalleryRepository repository) {
        this.repository = repository;
    }

    public void saveToGallery(MultipartFile file, String title)  {
        try{
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String originalName = file.getOriginalFilename();
            if (originalName == null || !originalName.contains(".")) {
                throw new BadRequestException("Érvénytelen fájlformátum!");
            }
            String newFileName = UUID.randomUUID() + originalName.substring(originalName.lastIndexOf("."));
            Path filePath = uploadPath.resolve(newFileName);
            file.transferTo(filePath.toFile());

            Gallery gallery = new Gallery();
            gallery.setAltText(title);
            gallery.setImageUrl(filePath.toString());
            repository.save(gallery);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Hiba a fájl létrehozásakor: " + e.getMessage());
        }
    }

    public List<String> getAllImages() {
        return repository.findAll().stream().map(Gallery::getImageUrl).toList();
    }

    public void deleteImage(String imageUrl) {
        Gallery gallery = repository.findByImageUrl(imageUrl)
                .orElseThrow(() -> new BadRequestException("Nincs ilyen kép a galériában!"));
        try {
            Files.deleteIfExists(Paths.get(gallery.getImageUrl()));
            repository.delete(gallery);
        } catch (Exception e) {
            throw new RuntimeException("Hiba a fájl törlésekor: " + e.getMessage());
        }
    }

}
