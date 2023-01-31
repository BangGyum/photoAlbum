package com.squarecross.photoalbum.domain;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

//@Entity 객체 하나가 DB에서 하나의 레코드, 즉 하나의 Row를 의미한다.Entity 객체 하나가 DB에서 하나의 레코드, 즉 하나의 Row를 의미한다.
//@Table 매핑되는 테이블 관련 정보를 정의합니다.
    //- name: 테이블명
    //- schema: 스키마명
    //- uniqueConstraints: 반복되면 안되는 제약조건
@Entity
@Table(name="album", schema="photo_album", uniqueConstraints = {@UniqueConstraint(columnNames = "album_id")})
public class Album {
    //@id
    //해당 Entity의 Primary Key로 사용한다는 의미이다.
    //@Id는 같은 테이블내에서 중복될 값이 있을 수 없다.

    //@GeneratedValue
    //@id 값을 새롭게 부여할 때 사용하는 방법에 대한 정보를 입력
        //strategy = GenerationType.IDENTITY
        //가장 최근 id에 +1 을 해서 다음 아이디를 생성한다.

    //@Column
    //album 테이블의 매핑되는 column 정보를 입력
        //name: Column 명
        //unique: 중복 불가
        //nullable: null값 허용
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id", unique = true, nullable = false)
    private Long albumId;

    @Column(name = "album_name", unique = false, nullable = false)
    private String albumName;

    //@CreationTimestamp 새로운 앨범을 생성해 DB INSERT할 때 자동으로 현재 시간을 입력해준다.
    @Column(name="created_at", unique = false, nullable = true)
    @CreationTimestamp
    private Date createdAt;

    //하나의 앨범이 여러개의 사진을 들고있을 수 있기에
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "album")
    private List<Photo> photos;

    public Album(){};

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}
