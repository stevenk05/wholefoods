package com.stevenk.wholefoods.service.image;
import com.stevenk.wholefoods.dto.ImageDTO;
import com.stevenk.wholefoods.model.Image;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface iImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDTO> saveImages(List<MultipartFile> files, Long prodID);
    void updateImage(MultipartFile file, Long imageID);
}
