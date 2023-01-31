package com.squarecross.photoalbum.domain;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="photo", schema="photo_album", uniqueConstraints = {@UniqueConstraint(columnNames = "photo_id")})
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id", unique = true, nullable = false)
    private Long photoId;

    @Column(name = "file_name", unique = false, nullable = true)
    private String fileName;
    @Column(name = "thumb_url", unique = false, nullable = true)
    private String thumbUrl;
    @Column(name = "original_url", unique = false, nullable = true)
    private String originalUrl;
    @Column(name = "file_size", unique = false, nullable = true)
    private String fileSize;

    @Column(name = "uploaded_at", unique = false, nullable = true)
    @CreationTimestamp
    private Date uploadedAt;

    //@ManyToOne 은 Entity 끼리 어떤 관계를 가지는지 정의
        //@OneToMany 도 존재.
        //FetchType.LAZY, FetchType.EAGER (즉시로딩 등 다양하게 존재하는데 각 fetchtype마다 JOIN된 Entity를 불러오는 방식이 다르다.
        //FetchType.LAZY (지연로딩) 는 Album 정보가 필요할 때만 불러온다. 필요할 떄 별도의 쿼리를 보냄.`
    //@JoinColumn(name="album_id")
        //JoinColumn은 관계의 주인, 즉 관계가 있는 Entity의 Foreign Key를 자신의 Column으로 정의된 곳에서 사용
        //앨범의 어떤 Column을 참조하는지에 대한 정보를 가지고 있다.
        //앨범의 PK를 참조, 따라서 name=”album_id” 값을 넣어준다.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="album_id")
    private Album album;


}
