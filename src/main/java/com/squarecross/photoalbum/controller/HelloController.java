package com.squarecross.photoalbum.controller;

import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class HelloController {
    @Autowired
    AlbumService albumService;
    @GetMapping("/api/demo-web")
    public List<String> Hello(){
        return Arrays.asList("리액트 스프링 ", "연결 성공");
    }
    @GetMapping("/api/demo-web1")
    public ResponseEntity<List<AlbumDto>> getAlbumList( //Json 안에 Array들
                                                        @RequestParam(value="keyword", required = false, defaultValue = "") final String keyword, //required=false 는 필수 값이 아니라는 의미.
                                                        @RequestParam(value="sort", required = false, defaultValue = "") final String sort){
        System.out.println(keyword);
        List<AlbumDto> albumDtos1 = albumService.getAlbumList(keyword,sort);
        return new ResponseEntity<>(albumDtos1, HttpStatus.OK);
    }
}