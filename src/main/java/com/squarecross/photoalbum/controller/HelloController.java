package com.squarecross.photoalbum.controller;

import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.service.AlbumService;
import com.squarecross.photoalbum.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class HelloController {
    @Autowired
    PhotoService photoService;
    @GetMapping("/api/demo-web")
    public List<String> Hello(){
        return Arrays.asList("리액트 스프링 ", "연결 성공");
    }

    @GetMapping("/api/demo-web2")
    public ResponseEntity<String> Hello2(){
        return ResponseEntity.ok("hi");
    }

    @GetMapping("/api/photoList")
    public ResponseEntity<List<PhotoDto>> albumPhotos(){
        Long albumId = new Long(1);
        List<PhotoDto> photos;
        photos = photoService.getPhotosList(albumId);
        //return new ResponseEntity<>(photos,HttpStatus.OK);
        return ResponseEntity.ok(photos);
    }
}