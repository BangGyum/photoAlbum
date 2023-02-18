package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.repository.AlbumRepository;
import com.squarecross.photoalbum.repository.PhotoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PhotoServiceTest {
    @Autowired
    PhotoRepository photoRepository;
    @Autowired
    PhotoService photoService;

    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    AlbumService albumService;

    @Test
    void getPhoto() {
        Album album = new Album();
        album.setAlbumName("테스트");
        //Album savedAlbum = albumRepository.save(album);

        //사진을 생성하고, setAlbum을 통해 앨범을 지정해준 이후, repository에 사진을 저장한다
        Photo photo1 = new Photo();
        photo1.setFileName("사진1");
        //photo1.setAlbum(savedAlbum);
        photoRepository.save(photo1);
        Photo photo2 = new Photo();
        photo1.setFileName("사진2");
        //photo1.setAlbum(savedAlbum);
        photoRepository.save(photo2);

        //int photoCount = photoService.getPhoto(savedAlbum.getAlbumId()); 다시 만들어야함
        //assertEquals(2, photoCount);
    }
    @Test
    void getAlbumPhotos() {
        Album album = new Album();
        album.setAlbumName("테스트");
        Album savedAlbum = albumRepository.save(album);

        //사진을 생성하고, setAlbum을 통해 앨범을 지정해준 이후, repository에 사진을 저장한다
        Photo photo1 = new Photo();
        photo1.setFileName("사진1");
        photo1.setAlbum(savedAlbum);
        photoRepository.save(photo1);
        Photo photo2 = new Photo();
        photo1.setFileName("사진2");
        photo1.setAlbum(savedAlbum);
        photoRepository.save(photo2);

        List<PhotoDto> photoDtos;
        photoDtos = photoService.getPhotosList(savedAlbum.getAlbumId());
        for (PhotoDto photoDto: photoDtos){
            assertEquals("사진1",photoDto.getFileName()); //맞으면 오류 없음
        }

    }


}