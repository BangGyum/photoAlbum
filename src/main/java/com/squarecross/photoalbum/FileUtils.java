package com.squarecross.photoalbum;

import org.apache.tika.Tika;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class FileUtils {
    private static final Tika tika = new Tika();
    public static boolean validImgFile(InputStream inputStream) { //이미지 확장자 체크
        try {
            //Arrays.asList()는 Arrays의 private 정적 클래스인 ArrayList를 리턴한다.
            List<String> ValidTypeList = Arrays.asList("image/jpeg", "image/pjpeg", "image/png", "image/gif", "image/bmp", "image/x-windows-bmp");

            String mimeType = tika.detect(inputStream); //File의 MIME Type을 String 문자열로 리턴
            System.out.println("MimeType : " + mimeType);

            boolean isValid = ValidTypeList.stream().anyMatch(ValidType -> ValidType.equalsIgnoreCase(mimeType));

            return isValid;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
