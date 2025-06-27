package com.stevenk.wholefoods.service.image;

import com.stevenk.wholefoods.dto.ImageDTO;
import com.stevenk.wholefoods.exceptions.ResourceNotFoundException;
import com.stevenk.wholefoods.model.Image;
import com.stevenk.wholefoods.repository.ImageRepository;
import com.stevenk.wholefoods.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;

import com.stevenk.wholefoods.model.Product;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements iImageService{
    private final ImageRepository imgRepo;
    private final IProductService productServ;


    @Override
    public Image getImageById(Long id) {
        return imgRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image not found where id == " + id));
    }

    @Override
    public void deleteImageById(Long id) {
        imgRepo.findById(id).ifPresentOrElse(imgRepo::delete, () -> {
            throw new ResourceNotFoundException("Image not found where id == " + id);
        } );
    }

    @Override
    public List<ImageDTO> saveImages(List<MultipartFile> files, Long productID) {
        Product product = productServ.getProductById(productID);
        List<ImageDTO> savedImageDTO = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFilename(file.getOriginalFilename());
                image.setFiletype(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String path = "/api/v1/images/image/download/";
                String downloadURL = path + image.getId();
                image.setUrl(downloadURL);

                Image saved = imgRepo.save(image);

                saved.setUrl(path + saved.getId());
                imgRepo.save(saved);

                ImageDTO imageDTO = new ImageDTO();
                imageDTO.setId(saved.getId());
                imageDTO.setImageName(saved.getFilename());
                imageDTO.setDownloadURL(saved.getUrl());
                savedImageDTO.add(imageDTO);

            } catch (SQLException | IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDTO;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try {
            image.setFilename(file.getOriginalFilename());
            image.setFiletype(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imgRepo.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
