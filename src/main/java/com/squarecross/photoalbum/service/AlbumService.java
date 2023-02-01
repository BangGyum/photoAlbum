package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Album;
import org.springframework.stereotype.Service;
import com.squarecross.photoalbum.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class AlbumService {
    @Autowired  //어노테이션으로 등록된 빈을 컨테이너에서 가져와서 사용
    private AlbumRepository albumRepository;

    public Album getAlbum(Long albumId){
        Optional<Album> res = albumRepository.findById(albumId); //반환되지 않는 경우 Optional 리턴값을 가짐
        if (res.isPresent()){ //isPresent() 로 값이 존재하는지 확인(있으면 true), 있는경우 Album entity 반환
            return res.get();
        } else {
            throw new EntityNotFoundException(String.format("앨범 아이디 %d로 조회되지 않았습니다", albumId));
        }

    }
}
