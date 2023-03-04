import React, { useEffect, useState } from "react";
import axios from 'axios';
import { authService } from "FirebaseInstance"
import { useNavigate  } from "react-router-dom";
import "css/style.css";

function InPhotoAlbum({prop}) {
    console.log("albumId=",prop);
    const navigate = useNavigate(); //양식이 제출 or 특정 event가 발생 시, url을 조작할 수 있는 interface
    const onClickLogOut = () => {
        authService.signOut();
        navigate(-1);  //뒤로
    }
  const [images, setImages] = useState([]);
    console.log(`/albums/${prop}/photos/getPhotos`);
  useEffect(() => {
    axios.get(`/albums/${prop}/photos/getPhotos`)
      .then(response => setImages(response.data) )
      .catch(error => console.error(error));
  }, []);
  console.log(images);
//<Wrapper key={index}>
return (
    <>
        <button onClick={onClickLogOut}>Log out</button>

       <div>
          {images.map((image, index) => (
            <img className="imgStyle" src={`data:image/png;base64,${image}`} alt="yourImage" key={index}/>
          ))}
       </div>
    </>
);
}
export default InPhotoAlbum;