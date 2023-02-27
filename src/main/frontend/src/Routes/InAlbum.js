import React, { useEffect, useState } from "react";
import axios from 'axios';
import { authService } from "FirebaseInstance"
import { useNavigate  } from "react-router-dom";

const InAlbum = () => {
    //앨범 목록 가져오기
  const [albumList, setAlbumList] = useState([]);
  useEffect(async ()=>{
    const album = await axios.get("/albums/albumList?sort=byName");
    console.log(album);

    for(let i=0; i < album.data.length; i++){
        console.log(album.data[i]);
        setAlbumList((albumList) => {
              return [...albumList,album.data[i].AlbumName];
            });
        //setThumbUrl([...thumbUrl,movies.data[i].thumbUrl], () => console.log(this.thumbUrl));

        }
    console.log(albumList);

  },[]);

    const navigate = useNavigate(); //양식이 제출 or 특정 event가 발생 시, url을 조작할 수 있는 interface
    const onClickLogOut = () => {
        authService.signOut();

        navigate(-1);  //뒤로
    }


return (
    <>
    <button onClick={onClickLogOut}>Log out</button>
   <div>
      {albumList.map((image, index) => (
        <p>
        d
        </p>
      ))}
   </div>
    </>
);
}
export default InAlbum;