package com.squarecross.photoalbum.repository;

import com.squarecross.photoalbum.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    int countByAlbum_AlbumId(Long AlbumId);

    //List<Photo> findTop4ByAlbumIdOrderByUploadedAt(Long AlbumId);
    //List<Photo> findTop4ByAlbum_AlbumIdOrderByUploadedAt(Long AlbumId);

    @Query(value = "SELECT * FROM photo WHERE album_id = ?1 ORDER BY uploaded_at DESC LIMIT 4", nativeQuery = true)
    List<Photo> findTop4ByAlbumIdOrderByUploadedAt(Long albumId);

    @Query(value = "SELECT * FROM photo WHERE album_id = ?1", nativeQuery = true)
    List<Photo> findTop4ByAlbumId(Long albumId);

    List<Photo> findByAlbum_AlbumId(Long AlbumId);

    //같은 파일명 유무 체크 메소드
    Optional<Photo> findByFileNameAndAlbum_AlbumId(String photoName, Long albumId);

}
