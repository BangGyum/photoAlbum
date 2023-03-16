import React, { useEffect, useState } from "react";
import axios from 'axios';
import AppRouter from "components/Router";
import { authService } from "FirebaseInstance";
import { useNavigate, Link } from "react-router-dom";
//import { ChakraBaseProvider, extendBaseTheme } from '@chakra-ui/react';
//import chakraTheme from '@chakra-ui/theme';
import InPhotoAlbum from "Routes/InPhotoAlbum";
import "css/albumStyle.css";

//앨범 목록 가져오기
function InAlbum() {
  const [albumList, setAlbumList] = useState([]);
  //const [albumListId, setAlbumListId] = useState([]);
  useEffect(()=>{
      async function fetchAlbums() {
        try {
            const album = await axios.get("/albums/albumList?sort=byName");
            for(let i=0; i < album.data.length; i++){
                setAlbumList((albumList) => {
                  return [...albumList, {
                    id : album.data[i].albumId,
                    name : album.data[i].albumName,
                    },
                  ]});
            }
        } catch (error) {
            console.error(error);
      }
    }
    fetchAlbums();
  },[]);

    const navigate = useNavigate(); //양식이 제출 or 특정 event가 발생 시, url을 조작할 수 있는 interface
    const onClickLogOut = () => {
        authService.signOut();

        navigate(-1);  //뒤로
    }

    console.log("aaa",albumList);
return (

        <>
        <button onClick={onClickLogOut}>Log out</button>
        <div class="albumFolder1">
        {albumList.map((albumListEach, index) => (
            <Link to={`/photoAlbum/${albumListEach.id}`} key={index}>
                {console.log(albumListEach.id)};
                <div class="albumFolder2" >
                    name : {albumListEach.name} , id: {albumListEach.id}
                </div>
            </Link>
        ))}
        </div>
    </>

);
}
export default InAlbum;