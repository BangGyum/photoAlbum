package com.squarecross.photoalbum.controller;

import com.squarecross.photoalbum.Constants;
import com.squarecross.photoalbum.FileUtils;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.service.PhotoService;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.xmlbeans.impl.common.IOUtil;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/albums/{albumId}/photos")
public class PhotoController {
    @Autowired
    private PhotoService photoService;
//    @RequestMapping(value="/getPhotos",method=RequestMethod.GET)
//    public ResponseEntity<List<byte[]>> getImages() throws IOException {
//
//        return photoService.getImages();
//    }
    @RequestMapping(value="/getPhotos",method=RequestMethod.GET)
    public ResponseEntity<List<byte[]>> getImages() throws IOException {
        List<byte[]> imageBytes = photoService.getImages();

        return new ResponseEntity<>(imageBytes,HttpStatus.OK);
    }
    @RequestMapping(value="/getEachAlbumPhotos",method=RequestMethod.GET)
    public ResponseEntity<List<byte[]>> getEachAlbumImages(@PathVariable("albumId") final Long albumId) throws IOException {
        List<byte[]> imageBytes = photoService.getEachAlbumImagesImages(albumId);

        return new ResponseEntity<>(imageBytes,HttpStatus.OK);
    }


    // 한개 이상사진 삭제 api
    @RequestMapping(value = "/delete", method = RequestMethod.PUT)
    ResponseEntity<Void> deletePhoto(@RequestParam("photoIds") final Long[] photoIds){
        for(Long photoId : photoIds){
            photoService.deletePhoto(photoId);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    //해당 앨범의 사진 목록 불러오기 api

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<List<PhotoDto>> albumPhotos(@PathVariable("albumId")final Long albumId){
        List<PhotoDto> photos;
        photos = photoService.getPhotosList(albumId);
        return new ResponseEntity<>(photos,HttpStatus.OK);
    }

    //사진 옮기기 api
    @RequestMapping(value = "", method = RequestMethod.GET)
    public void movePhoto(@RequestParam("albumId") final Long albumId,
                          @RequestParam("photoIds") Long[] photoIds){
        for(Long photoId : photoIds){
            photoService.movePhoto(albumId, photoId);
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
                FileOutputStream fileOStream = null;
                ZipOutputStream zipOut = null;
                FileInputStream fileIStream = null;
                try {

                    fileOStream = new FileOutputStream("C:/Users/uj052/Downloads/images.zip");
                    zipOut = new ZipOutputStream(fileOStream);

                    for (Long photoId : photoIds ) {
                        File file = photoService.getImageFile(photoId);
                        fileIStream = new FileInputStream(file);
                        ZipEntry zipEntry = new ZipEntry(file.getName());
                        zipOut.putNextEntry(zipEntry);

                        byte[] bytes = new byte[1024];
                        int length;

                        while((length = fileIStream.read(bytes)) >= 0) {
                            zipOut.write(bytes, 0, length);
                        }

                        fileIStream.close();
                        zipOut.closeEntry();
                    }

                    zipOut.close();
                    fileOStream.close();

                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } finally {
                    try { if(fileIStream != null)fileIStream.close(); } catch (IOException e1) {System.out.println(e1.getMessage());/*ignore*/}
                    try { if(zipOut != null)zipOut.closeEntry();} catch (IOException e2) {System.out.println(e2.getMessage());/*ignore*/}
                    try { if(zipOut != null)zipOut.close();} catch (IOException e3) {System.out.println(e3.getMessage());/*ignore*/}
                    try { if(fileOStream != null)fileOStream.close(); } catch (IOException e4) {System.out.println(e4.getMessage());/*ignore*/}
                }
            }
        }catch (FileNotFoundException e){
            throw new RuntimeException(e);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
