package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.mapper.AlbumMapper;
import com.squarecross.photoalbum.repository.AlbumRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.TransactionScoped;
import javax.transaction.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    AlbumMapper albumMapper;

    @Test
    void getAlbum() {
        Album album = new Album();
        album.setAlbumName("테스트");
        Album savedAlbum = albumRepository.save(album);

        Album getAlbum = albumService.getAlbum(savedAlbum.getAlbumId());
        assertEquals("테스트", getAlbum.getAlbumName());
    }

    @Test
    void createAlbum() throws IOException {
        Album album = new Album();
        album.setAlbumName("생성후삭제");
        Album savedAlbum = albumRepository.save(album);
        AlbumDto saveAlbumDto = albumMapper.convertToDto(savedAlbum);
        AlbumDto getAlbum = albumService.createAlbum(saveAlbumDto);
        albumService.deleteAlbumDirectories(savedAlbum);
    }

    @Test
    void testChangeAlbumName() throws IOException{
        AlbumDto albumDto = new AlbumDto();
        albumDto.setAlbumName("변경 전");
        AlbumDto res = albumService.createAlbum(albumDto);

        Long albumId = res.getAlbumId(); // 앨범 아이디 추출
        AlbumDto updateDto = new AlbumDto();
        updateDto.setAlbumName("변경 후");
        albumService.changeName(albumId,updateDto);

        Album updated = albumService.getAlbum(albumId);

        assertEquals("변경 후",updated.getAlbumName());
    }

    @Test
    void testDeleteAlbumId() throws IOException{
        AlbumDto albumDto = new AlbumDto();
        albumDto.setAlbumName("삭제 예정");
        AlbumDto res = albumService.createAlbum(albumDto);
        System.out.print(res.getAlbumId());
        Album select = albumService.getAlbum(res.getAlbumId());
        AlbumDto deleteDto = albumService.deleteAlbum(res.getAlbumId());

        assertEquals("삭제 예정",deleteDto.getAlbumName()); //맞으면 오류 없음
    }

    @Test
    void testAlbumRepository() throws InterruptedException { //service 만들기 전 테스트
        Album album1 = new Album();
        Album album2 = new Album();
        album1.setAlbumName("test1");
        album2.setAlbumName("test2");

        albumRepository.save(album1);
        TimeUnit.SECONDS.sleep(1); //시간차를 벌리기위해 두번째 앨범 생성 1초 딜레이
        albumRepository.save(album2);

        //최신순 정렬, 두번째로 생성한 앨범이 먼저 나와야합니다
        List<Album> resDate = albumRepository.findByAlbumNameContainingOrderByCreatedAtDesc("test");
        assertEquals("test2", resDate.get(0).getAlbumName()); // 0번째 Index가 두번째 앨범명 test1 인지 체크
        assertEquals("test1", resDate.get(1).getAlbumName()); // 1번째 Index가 첫번째 앨범명 test2 인지 체크
        assertEquals(2, resDate.size()); // test 이름을 가진 다른 앨범이 없다는 가정하에, 검색 키워드에 해당하는 앨범 필터링 체크

        //앨범명 정렬, aaaa -> aaab 기준으로 나와야합니다
        List<Album> resName = albumRepository.findByAlbumNameContainingOrderByAlbumNameAsc("test");
        assertEquals("test1", resName.get(0).getAlbumName()); // 0번째 Index가 두번째 앨범명 test1 인지 체크
        assertEquals("test2", resName.get(1).getAlbumName()); // 1번째 Index가 두번째 앨범명 test2 인지 체크
        assertEquals(2, resName.size()); // aaa 이름을 가진 다른 앨범이 없다는 가정하에, 검색 키워드에 해당하는 앨범 필터링 체크
    }

    //Album resAlbum = albumService.getAlbum(savedAlbum.getAlbumId());
        //assertEquals("test", resAlbum.getAlbumName());

//        List<Album> resAlbums = albumService.getAlbum(savedAlbum.getAlbumName());
//        for(int i=0;i< resAlbums.size();i++){
//            assertEquals("test", resAlbums.get(i).getAlbumName());
//        }

    //}

}