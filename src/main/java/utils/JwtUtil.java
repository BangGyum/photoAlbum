package utils;
//import io.jsonwebtoken.*;


public class JwtUtil {
    //토큰은 header.payload.signature (헤더.내용.서명) 으로 구성돼 있고, cliams는 내용 안에 있다.
    /**
     * 사용자 정보를 기반으로 토큰을 생성하여 반환 해주는 메서드
     *
     * @param userName  : 사용자 이름
     * @return String : 토큰
     */
//    public static createJwt(String userName, String secretKey){ //secretKey는 토큰에 서명하는데 사용, userName은
//                                                            // controller에서 check 할때 토큰의 유저네임을 꺼내서 표시
//                                                            // jwt는 클레임이라고, 내가 원하는 정보를 저장하는 공간을 제공
//        Claims claims = Jwts.claims();
//
//    }
}
