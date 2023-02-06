package com.squarecross.photoalbum.repository;

import com.squarecross.photoalbum.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository //어노테이션을 추가해서 스프링 Bean으로 등록하여 IoC에서 관리
public interface AlbumRepository extends JpaRepository<Album, Long> { //앨범 entity와 pk 데이터 타입
    //- findBy<Field>
    //    <Field> 내에 Entity내에 정의한 필드명을 입력. 대문자로 시작하는게 규칙.
    //@Query(value = "SELECT * FROM album WHERE album_name = ?0", nativeQuery = true)
    //List<Album> fineByAlbumName(String name); //조회할거야, By가 경로 시작점
    //List<Album> findByAlbum_Name(String name)
    List<Album> findByAlbumNameContainingOrderByAlbumNameAsc(String name);
    List<Album> findByAlbumNameContainingOrderByCreatedAtDesc(String createAt);



    //- findBy<Field>And<Field>
    //    - 두가지 이상의 조건이 필요한 경우 And로 이어주시면 됩니다
    //- findBy<Field>Like / findBy<Field>NotLike
    //    - SQL의 Like문처럼 입력값이 포함되어있는 경우 (혹은 아닌 경우) 조회됩니다
    //- findBy<Field>LessThan / findBy<Field>GreaterThan
    //    - 필드가 입력값보다 적거나, 많거나.
    //- findBy<Entity>_<Field>
    //    - Join되는 다른 Entity의 필드값으로 조회하고싶은 경우
}
