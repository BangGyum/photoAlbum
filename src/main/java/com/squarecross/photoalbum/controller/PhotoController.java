package com.squarecross.photoalbum.controller;

import com.squarecross.photoalbum.FileUtils;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/albums/{albumId}/photos")
public class PhotoController {
    @Autowired
    private PhotoService photoService;

    //사진 정보 api
    @RequestMapping(value="/{photoId}",method = RequestMethod.GET)
    public ResponseEntity<PhotoDto> getPhotoInfo(@PathVariable("photoId") final Long photoId){
        PhotoDto photoDto = photoService.getPhoto(photoId);
        return new ResponseEntity<>(photoDto, HttpStatus.OK);
    }
    //사진 업로드 api
    @RequestMapping(value="", method = RequestMethod.POST)
    public ResponseEntity<List<PhotoDto>> uploadPhotos(@PathVariable("albumId") final Long albumId,
                                                       @RequestParam("photos") MultipartFile[] files) throws IOException {
        List<PhotoDto> photos = new ArrayList<>();
        for (MultipartFile file : files){
            try(InputStream inputStream = file.getInputStream()) { //
                System.out.println("Content Type : " + file.getContentType());
                if(!file.isEmpty()) {
                    boolean isValid = FileUtils.validImgFile(inputStream);
                    if(!isValid) {
                        // exception 처리
                        System.out.println("이미지만 업로드 가능");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            PhotoDto photoDto = photoService.savePhoto(file,albumId);
            photos.add(photoDto);
        }
        return new ResponseEntity<>(photos,HttpStatus.OK);
    }
}
