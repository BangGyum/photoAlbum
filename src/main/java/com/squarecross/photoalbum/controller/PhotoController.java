package com.squarecross.photoalbum.controller;

import com.squarecross.photoalbum.FileUtils;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.service.PhotoService;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.xmlbeans.impl.common.IOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/albums/{albumId}/photos")
public class PhotoController {
    @Autowired
    private PhotoService photoService;

    //사진 다운로드 api
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void downloadPhotos(@RequestParam("photoIds") Long[] photoIds, HttpServletResponse httpServletResponse){
        try{
            if (photoIds.length==1){
                File file = photoService.getImageFile(photoIds[0]);
                OutputStream outputStream = httpServletResponse.getOutputStream();
                IOUtils.copy(new FileInputStream(file), outputStream);
                //	copy(InputStream, OutputStream) =Copies bytes from an InputStream to an OutputStream.
                outputStream.close();
            }else { //여러장일때 zip파일로
            }
        }catch (FileNotFoundException e){
            throw new RuntimeException(e);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    //사진 정보 api
    @RequestMapping(value="/{photoId}",method = RequestMethod.GET)
    public ResponseEntity<PhotoDto> getPhotoInfo(@PathVariable("photoId") final Long photoId){
        PhotoDto photoDto = photoService.getPhoto(photoId);
        return new ResponseEntity<>(photoDto, HttpStatus.OK);
    }
    //사진 업로드 api
    @RequestMapping(value="", method = RequestMethod.POST)
    public ResponseEntity<List<PhotoDto>> uploadPhotos(@PathVariable("albumId") final Long albumId,
                                                       @RequestParam("photos") MultipartFile[] files) throws IOException {
        List<PhotoDto> photos = new ArrayList<>();
        for (MultipartFile file : files){
            try(InputStream inputStream = file.getInputStream()) { //
                System.out.println("Content Type : " + file.getContentType());
                if(!file.isEmpty()) {
                    boolean isValid = FileUtils.validImgFile(inputStream);
                    if(!isValid) {
                        // exception 처리
                        System.out.println("이미지만 업로드 가능");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            PhotoDto photoDto = photoService.savePhoto(file,albumId);
            photos.add(photoDto);
        }
        return new ResponseEntity<>(photos,HttpStatus.OK);
    }
}
