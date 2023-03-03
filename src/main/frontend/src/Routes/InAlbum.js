import React, { useEffect, useState } from "react";
import axios from 'axios';
import { authService } from "FirebaseInstance";
import { BrowserRouter, Route, Routes, useNavigate, Link } from "react-router-dom";
import { ChakraBaseProvider, extendBaseTheme } from '@chakra-ui/react';
import chakraTheme from '@chakra-ui/theme';
import InPhotoAlbum from "Routes/InPhotoAlbum";


function InAlbum() {
    //앨범 목록 가져오기
  const [albumListName, setAlbumListName] = useState([]);
  const [albumListId, setAlbumListId] = useState([]);
  useEffect(()=>{
      async function fetchAlbums() {
            try {
                const album = await axios.get("/albums/albumList?sort=byName");
                console.log(album);

                for(let i=0; i < album.data.length; i++){
                    console.log(album.data[i]);
                    setAlbumListName((albumListName) => {
                          return [...albumListName, album.data[i].AlbumName];
                        });
                    setAlbumListId((albumListId) => {
                                  return [...albumListId, album.data[i].albumId];
                                });
                    //setThumbUrl([...thumbUrl,movies.data[i].thumbUrl], () => console.log(this.thumbUrl));

                    }
            console.log(albumListName);
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


return (
    <>
        <button onClick={onClickLogOut}>Log out</button>
        <ul>
        {albumListId.map((albumId, index) => (
            <li><Link to="/photoAlbum" key={index}>InPhotoAlbum</Link></li>
        ))}
        </ul>
    </>

);
}
export default InAlbum;