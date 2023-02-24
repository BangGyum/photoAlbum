package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.Constants;
import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.mapper.AlbumMapper;
import com.squarecross.photoalbum.mapper.PhotoMapper;
import com.squarecross.photoalbum.repository.AlbumRepository;
import com.squarecross.photoalbum.repository.PhotoRepository;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.EntityNotFoundException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoService {
    @Autowired  //어노테이션으로 등록된 빈을 컨테이너에서 가져와서 사용
    private AlbumRepository albumRepository;
    @Autowired
    private PhotoRepository photoRepository;
    private PhotoMapper photoMapper;

    private final String original_path = Constants.PATH_PREFIX+"/photos/original";
    private final String thumb_path = Constants.PATH_PREFIX+"/photos/thumb";

    public List<byte[]> getImages() throws IOException {
        List<byte[]> images = new ArrayList<>();

        List<Photo> photoList = photoRepository.findAll();
        for (Photo photo : photoList) {
            Path imagePath = Paths.get("D:/photoalbumSpring/photoalbum"+photo.getThumbUrl());
            byte[] imageBytes = Files.readAllBytes(imagePath);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            images.add(imageBytes);
        }

        return images;
    }

    //이미지 리액트로 전송 service
//    public List<ResponseEntity<byte[]>> getImages() throws IOException {
//        List<ResponseEntity<byte[]>> images = new ArrayList<>();
//
//        List<Photo> photoList = photoRepository.findAll();
//        for (Photo photo : photoList) {
//            Path imagePath = Paths.get("D:/photoalbumSpring/photoalbum"+photo.getThumbUrl());
//            byte[] imageBytes = Files.readAllBytes(imagePath);
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.IMAGE_PNG);
//            images.add(new ResponseEntity<>(imageBytes, headers, HttpStatus.OK));
//        }
//
//        return images;
//    }

    //이미지 삭제 서비스
    public void deletePhoto(Long photoId){
        Optional<Photo> res = photoRepository.findById(photoId);
        if(res.isEmpty()){
            throw new EntityNotFoundException(String.format("해당 사진Id : %d로 조회되지 않았습니다", photoId));
        }
        Photo photo = res.get();
        try{
            photoRepository.deleteById(photoId);

        }catch(Exception e){
            e.printStackTrace();
        }
        try {
            Path filePath = Paths.get("D:/photoalbumSpring/photoalbum"+photo.getOriginalUrl());
            Files.delete(filePath);
            Path filePathThumb = Paths.get("D:/photoalbumSpring/photoalbum"+photo.getThumbUrl());
            Files.delete(filePathThumb);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public List<PhotoDto> getPhotosList(Long albumId){
        List<Photo> photos = photoRepository.findByAlbum_AlbumId(albumId);
        List<PhotoDto> photoDtos = new ArrayList();
        for(Photo photo : photos){
            PhotoDto photoDto = PhotoMapper.convertToDto(photo);
            photoDtos.add(photoDto);
        }
        return photoDtos;
    }

    public void movePhoto(Long albumId,Long photoId) {
        Optional<Photo> res = photoRepository.findById(photoId); //반환되지 않는 경우 Optional 리턴값을 가짐
        if (res.isPresent()) {
            Photo photo = res.get();
            PhotoDto photoDto = photoMapper.convertToDto(photo);
            try {
                String a = "D:/photoalbumSpring/photoalbum"+photoDto.getOriginalUrl();
                Path filePath = Paths.get(a);
                //a는 bbbbD:/photoalbumSpring/photoalbum/photos/original/1/갤럭시s23 라임 (2).PNG
                //filePath.toAbsolutePath() 는 bbbbD:\photoalbumSpring\photoalbum\photos\original\1\갤럭시s23 라임 (2).PNG
                Path filePathToMove = Paths.get(original_path+"/"+albumId+"/"+photoDto.getFileName());
                Files.move(filePath, filePathToMove);
                //밑은 썸네일
                Path filePathThumb = Paths.get("D:/photoalbumSpring/photoalbum"+photoDto.getThumbUrl());
                Path filePathToMoveThumb = Paths.get(thumb_path+"/"+albumId+"/"+photoId);
                Files.move(filePathThumb, filePathToMoveThumb);

                photo.setOriginalUrl("/photos/original"+"/"+albumId+"/"+photoDto.getFileName());
                photo.setThumbUrl("/photos/thumb"+"/"+albumId+"/"+photoId);
                this.photoRepository.save(photo);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //return photoDto;
        } else {
            throw new EntityNotFoundException(String.format("해당 사진Id : %d가 조회되지 않았습니다", photoId));
        }
    }

    public PhotoDto getPhoto(Long photoId) {
        Optional<Photo> res = photoRepository.findById(photoId); //반환되지 않는 경우 Optional 리턴값을 가짐
        if (res.isPresent()) {
            PhotoDto photoDto = photoMapper.convertToDto(res.get());
            //albumDto.setCount(photoRepository.countByAlbum_AlbumId(albumId));
            return photoDto;
        } else {
            throw new EntityNotFoundException(String.format("해당 사진Id : %d로 조회되지 않았습니다", photoId));
        }
    }
    //사진 다운로드
    public File getImageFile(Long photoId){
        Optional<Photo> res = photoRepository.findById(photoId);
        if(res.isEmpty()){
            throw new EntityNotFoundException(String.format("사진을 ID %d를 찾을 수 없습니다", photoId));
        }
        return new File(Constants.PATH_PREFIX + res.get().getOriginalUrl());
    }

    public PhotoDto savePhoto(MultipartFile file, Long albumId) throws IOException {
        Optional<Album> res = albumRepository.findById(albumId);
        if(res.isEmpty()){
            throw new EntityNotFoundException("앨범이 존재하지 않습니다.");
        }
        String fileName = file.getOriginalFilename();
        int fileSize = (int)file.getSize(); //Long 64byte, int는 32byte (대략 2GB까지 표현)
        fileName = getNextFileName(fileName, albumId);
        saveFile(file,albumId, fileName);

        Photo photo = new Photo();
        photo.setOriginalUrl("/photos/original/" + albumId + "/" + fileName);
        photo.setThumbUrl("/photos/thumb/" + albumId + "/" + fileName);
        photo.setFileName(fileName);
        photo.setFileSize(fileSize);
        photo.setAlbum(res.get());
        Photo createdPhoto = photoRepository.save(photo);
        return PhotoMapper.convertToDto(createdPhoto);

    }
    //이미지 저장 메소드
    private void saveFile(MultipartFile file, Long AlbumId, String fileName) throws IOException {
        try {
            String filePath = AlbumId + "/" + fileName;
            Files.copy(file.getInputStream(), Paths.get(original_path + "/" + filePath));

            BufferedImage thumbImg = Scalr.resize(ImageIO.read(file.getInputStream()), Constants.THUMB_SIZE, Constants.THUMB_SIZE);
            File thumbFile = new File(thumb_path + "/" + filePath);
            String ext = StringUtils.getFilenameExtension(fileName);////주어진 path로 부터 파일 확장자를 추출한다.
            if (ext == null) {
                throw new IllegalArgumentException("No Extention");
            }
            ImageIO.write(thumbImg, ext, thumbFile); //이미지 만들기
            //write(RenderedImage im, String formatName, File output)

        }catch (Exception e){
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }
    private String getNextFileName(String fileName, Long albumId){
        String fileNameNoExt = StringUtils.stripFilenameExtension(fileName); //파일명에서 확장자를 제거한다.
        String ext = StringUtils.getFilenameExtension(fileName); //주어진 path로 부터 파일 확장자를 추출한다.

        Optional<Photo> res = photoRepository.findByFileNameAndAlbum_AlbumId(fileName, albumId);

        int count = 2; //숫자붙이기
        while(res.isPresent()){//Optional 객체가 값을 가지고 있다면 true, 값이 없다면 false 리턴
            fileName = String.format("%s (%d).%s", fileNameNoExt, count,ext);
            res = photoRepository.findByFileNameAndAlbum_AlbumId(fileName, albumId);
            count++;
        }
        return fileName;
    }

}
