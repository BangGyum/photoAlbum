package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.Constants;
import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.mapper.AlbumMapper;
import com.squarecross.photoalbum.repository.PhotoRepository;
import org.springframework.stereotype.Service;
import com.squarecross.photoalbum.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlbumService {
    @Autowired  //어노테이션으로 등록된 빈을 컨테이너에서 가져와서 사용
    private AlbumRepository albumRepository;
    private PhotoRepository photoRepository;

    public Album getAlbum(Long albumId){
        Optional<Album> res = albumRepository.findById(albumId); //반환되지 않는 경우 Optional 리턴값을 가짐
        if (res.isPresent()){ //isPresent() 로 값이 존재하는지 확인(있으면 true), 있는경우 Album entity 반환
            Album album = res.get();
            return album;
        } else {
            throw new EntityNotFoundException(String.format("앨범 아이디 %d로 조회되지 않았습니다", albumId));
        }
    }
    public AlbumDto createAlbum(AlbumDto albumDto) throws IOException {
        Album album = AlbumMapper.convertToModel(albumDto);// 받은 dto를 변경
        this.albumRepository.save(album); //데이터 저장
        this.createAlbumDirectories(album); //메소드 실행
        return AlbumMapper.convertToDto(album); //dto로 전송
    }

    private void createAlbumDirectories(Album album) throws IOException {
        //디렉토리 생성
        Files.createDirectories(Paths.get(Constants.PATH_PREFIX + "/photos/original/" + album.getAlbumId()));
        Files.createDirectories(Paths.get(Constants.PATH_PREFIX + "/photos/thumb/" + album.getAlbumId()));
    }
    public void deleteAlbumDirectories(Album album) throws IOException {
        //디렉토리 생성
        Files.delete(Paths.get(Constants.PATH_PREFIX + "/photos/original/" + album.getAlbumId()));
        Files.delete(Paths.get(Constants.PATH_PREFIX + "/photos/thumb/" + album.getAlbumId()));
    }
    public List<AlbumDto> getAlbumList(String keyword, String sort) {
        List<Album> album;
        if(Objects.equals(sort,"byName")){
            album = albumRepository.findByAlbumNameContainingOrderByAlbumNameAsc(keyword);
        } else if (Objects.equals(sort,"byDate")) {
            album = albumRepository.findByAlbumNameContainingOrderByCreatedAtDesc(keyword);
            
        } else {
            throw new IllegalArgumentException("알 수 없는 정렬 기준");
        }
       // return AlbumMapper.convertToDtoList(album);
        List<AlbumDto> albumDtos = AlbumMapper.convertToDtoList(album);

        for(AlbumDto albumDto : albumDtos){
            List<Photo> top4 = photoRepository.findTop4ByAlbumIdOrderByUploadedAt(albumDto.getAlbumId());
            albumDto.setThumbUrls(
                    top4.stream()
                            .map(Photo::getThumbUrl)
                            .map(c -> Constants.PATH_PREFIX + c)
                            .collect(Collectors.toList()));
        }
        return albumDtos;
    }

//    public AlbumDto getAlbum(Long albumId) {
//        Optional<Album> res = albumRepository.findById(albumId); //반환되지 않는 경우 Optional 리턴값을 가짐
//        if (res.isPresent()) {
//            AlbumDto albumDto = AlbumMapper.convertToDto(res.get());
//            albumDto.setCount(photoRepository.countByAlbum_AlbumId(albumId));
//            return albumDto;
//        } else {
//            throw new EntityNotFoundException(String.format("앨범 아이디 %d로 조회되지 않았습니다", albumId));
//        }
//    }
//    public List<Album> getAlbum(String AlbumName) {
//        Optional<Album> res = albumRepository.findByName(AlbumName); //반환되지 않는 경우 Optional 리턴값을 가짐
//        if (res.isPresent()){
//        List<Album> posts = albumRepository.fineByAlbumName("test");
//        if (posts == null || posts.isEmpty())
//            // 찾지 못할 경우 "포스트 없음" 예외 처리
//            throw new EntityNotFoundException(String.format("앨범 이름으로 조회 안됐음"));
////        Album a=new Album();
////        try {
////        return albumRepository.fineByAlbumName(AlbumName); //반환되지 않는 경우 Optional 리턴값을 가짐
////        } catch (Exception e) {
////            System.out.print(e+"select Name이 작동 안함");
////        }
////        return a;
//        return posts;
//    }
}
