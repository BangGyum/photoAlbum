package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.repository.AlbumRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.TransactionScoped;
import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
//스프링 컨테이너 내에 있는 모든 빈을 DI로 가져와서 사용할 수 있도록 만듦.
//@Autowired로 IoC내에 있는 빈들을 모두 사용 할 수 있다.
//@Transactional
//DB에 입출력시, 쿼리 실행 후 commit을 해야 실제로 변경됨.
//하지만 이건 테스트니 commit하지 말자

@SpringBootTest
class AlbumServiceTest {
    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    AlbumService albumService;

    @Test
    void getAlbum() {
        Album album = new Album();
        album.setAlbumName("테스트");
        Album savedAlbum = albumRepository.save(album);

        Album getAlbum = albumService.getAlbum(savedAlbum.getAlbumId());
        assertEquals("테스트", getAlbum.getAlbumName());
    }

        //Album resAlbum = albumService.getAlbum(savedAlbum.getAlbumId());
        //assertEquals("test", resAlbum.getAlbumName());

//        List<Album> resAlbums = albumService.getAlbum(savedAlbum.getAlbumName());
//        for(int i=0;i< resAlbums.size();i++){
//            assertEquals("test", resAlbums.get(i).getAlbumName());
//        }

    //}

}