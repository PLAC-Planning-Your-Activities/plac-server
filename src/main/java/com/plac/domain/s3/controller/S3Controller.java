package com.plac.domain.s3.controller;

import com.plac.domain.s3.dto.request.DeleteImageRequest;
import com.plac.domain.s3.dto.response.UploadImageResponse;
import com.plac.domain.s3.service.S3Service;
import com.plac.domain.s3.validator.ValidateImagePath;
import com.plac.domain.s3.validator.type.ImagePath;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/images")
    public ResponseEntity<UploadImageResponse> uploadImage(
            @RequestPart(value = "image") MultipartFile image,
            @RequestPart(value = "path") @ValidateImagePath(enumClass = ImagePath.class) String path
    ) throws IOException {
        UploadImageResponse result = s3Service.uploadImage(image, path);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/images")
    public ResponseEntity<Void> deleteImage(@RequestBody @Valid DeleteImageRequest imageRequest) {
        s3Service.deleteImage(imageRequest.getName());
        return ResponseEntity.ok().build();
    }
}
