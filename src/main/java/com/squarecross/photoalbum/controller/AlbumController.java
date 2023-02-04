package com.squarecross.photoalbum.controller;
import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.repository.AlbumRepository;
import com.squarecross.photoalbum.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumController {
    @Autowired
    AlbumService albumService;
    @Autowired
    AlbumRepository albumRepository;
//    @RequestMapping(value = "/{albumId}", method = RequestMethod.GET)
//    public ResponseEntity<AlbumDto> getAlbum(@PathVariable("albumId") final long albumId) {
//        AlbumDto albumDto = albumService.getAlbum(albumId);
//        return new ResponseEntity<>(albumDto, HttpStatus.OK);
//    }
    @RequestMapping("/all")
    List<Album> all(@RequestParam(required=false) String name){
        return albumRepository.findAll();
    }
    @RequestMapping("/name/{albumName}")
    List<Album> findAlbumName(@PathVariable("albumName") final String name){
        return albumRepository.fineByAlbumName(name);
    }
}
