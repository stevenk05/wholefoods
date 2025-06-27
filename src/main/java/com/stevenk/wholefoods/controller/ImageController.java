package com.stevenk.wholefoods.controller;

import com.stevenk.wholefoods.dto.ImageDTO;
import com.stevenk.wholefoods.exceptions.ResourceNotFoundException;
import com.stevenk.wholefoods.model.Image;
import com.stevenk.wholefoods.response.ApiResponse;
import com.stevenk.wholefoods.service.image.iImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;



@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {
    private final iImageService imgService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long productID){
        try {
            List<ImageDTO> imageDTOs = imgService.saveImages(files, productID);
            return ResponseEntity.ok(new ApiResponse("Successful upload operation.", imageDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload failed/unsuccessful.", e.getMessage()));
        }
    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
        Image image = imgService.getImageById(imageId);
        ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
        return  ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFiletype()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +image.getFilename() + "\"")
                .body(resource);
    }

    @PutMapping("/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile file) throws SQLException {
        try {
            Image image = imgService.getImageById(imageId);
            if (image != null){
                imgService.updateImage(file, imageId);
                return ResponseEntity.ok(new ApiResponse("Successful update operation.", null));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Update failed.", INTERNAL_SERVER_ERROR));
    }

    @DeleteMapping("/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) throws SQLException {
        try {
            Image image = imgService.getImageById(imageId);
            if (image != null){
                imgService.deleteImageById(imageId);
                return ResponseEntity.ok(new ApiResponse("Successful delete operation.", null));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Delete failed.", INTERNAL_SERVER_ERROR));
    }
}
