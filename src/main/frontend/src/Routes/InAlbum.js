import React, { useEffect, useState } from "react";
import axios from 'axios';
import { authService } from "FirebaseInstance"
import { useNavigate  } from "react-router-dom";

const InAlbum = () => {
    //앨범 목록 가져오기
  const [albumList, setAlbumList] = useState([]);
  useEffect(async ()=>{
    const album = await axios.get("/albums/albumList");
    console.log(album);

    for(let i=0; i < album.data.length; i++){
        console.log(album.data[i]);
        setAlbumList((albumList) => {
              return [...albumList,album.data[i].AlbumName];
            });

        //setThumbUrl([...thumbUrl,movies.data[i].thumbUrl], () => console.log(this.thumbUrl));

        }
    //console.log(thumbUrl);

  },[]);

    const navigate = useNavigate(); //양식이 제출 or 특정 event가 발생 시, url을 조작할 수 있는 interface
    const onClickLogOut = () => {
        authService.signOut();

        navigate(-1);  //뒤로
    }
  const [images, setImages] = useState([]);

  useEffect(() => {
    axios.get('/albums/1/photos/getPhotos')
      .then(response => setImages(response.data) )
      .catch(error => console.error(error));
  }, []);
  console.log(images);

return (
    <>
        <button onClick={onClickLogOut}>Log out</button>

       <div>
          {images.map((image, index) => (
            <img src={`data:image/png;base64,${image}`} alt="Your image" />
          ))}
       </div>
    </>
);
}
export default InAlbum;