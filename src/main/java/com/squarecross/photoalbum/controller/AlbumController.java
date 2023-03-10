package com.squarecross.photoalbum.controller;
import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.repository.AlbumRepository;
import com.squarecross.photoalbum.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumController {
    @Autowired
    AlbumService albumService;
    @Autowired
    AlbumRepository albumRepository;

    //앨범 삭제
    @RequestMapping(value="/delete/{albumId}", method=RequestMethod.PUT)
    ResponseEntity<Void> deleteAlbum(@PathVariable("albumId")final long albumId){
        albumService.deleteAlbum(albumId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //앨범명 변경
    @RequestMapping(value="/{albumId}", method=RequestMethod.PUT)
    ResponseEntity<AlbumDto> updateAlbum(@PathVariable("albumId")final long albumId,
                                         @RequestBody AlbumDto albumDto){
        AlbumDto res = albumService.changeName(albumId, albumDto);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
    @RequestMapping("/all")
    List<Album> all(@RequestParam(required=false) String name){ //required=false 는 파라미터가 필수가 아니란 것
        return albumRepository.findAll();
    }
//    @RequestMapping("/name/{albumName}")
//    List<Album> findAlbumName(@PathVariable("albumName") final String name){
//        return albumRepository.fineByAlbumName(name);
//    }
    @RequestMapping("/id/{albumId}")
    Album findAlbumId(@PathVariable("albumId") final Long albumId){
        return albumService.getAlbum(albumId);
    }

    //앨범 list 가져오기
    @RequestMapping(value = "/albumList" , method = RequestMethod.GET)
    public ResponseEntity<List<AlbumDto>> getAlbumList( //Json 안에 Array들
        @RequestParam(value="keyword", required = false, defaultValue = "") final String keyword, //required=false 는 필수 값이 아니라는 의미.
        @RequestParam(value="sort", required = false, defaultValue = "") final String sort){
        System.out.println(keyword);
        List<AlbumDto> albumDtos1 = albumService.getAlbumList(keyword,sort);
        return new ResponseEntity<>(albumDtos1, HttpStatus.OK);
    }


    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<AlbumDto> createAlbum(@RequestBody final AlbumDto albumDto) throws IOException {
        AlbumDto savedAlbumDto = albumService.createAlbum(albumDto);
        return new ResponseEntity<>(savedAlbumDto, HttpStatus.OK);
    }
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity<AlbumDto> deleteAlbum(@RequestBody final AlbumDto albumDto) throws IOException {
        AlbumDto savedAlbumDto = albumService.createAlbum(albumDto);
        return new ResponseEntity<>(savedAlbumDto, HttpStatus.OK);
    }
//    @RequestMapping(value="", method = RequestMethod.POST)
//    public ResponseEntity<AlbumDto> createAlbum(@RequestBody final AlbumDto albumDto) {
//
//    }
}
