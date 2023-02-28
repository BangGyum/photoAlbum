import React, { useEffect, useState } from "react";
import AppRouter from "components/Router";
import axios from 'axios';
import { authService } from "FirebaseInstance";

import 'bootstrap/dist/css/bootstrap.min.css';
import Form from "react-bootstrap/Form";
//import Button from "react-bootstrap/Button";
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Container from 'react-bootstrap/Container';
import { ChakraProvider } from '@chakra-ui/react';
import Button from "css/Button";
import "css/style.css";



function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);//처음에 무조건 로그아웃으로 시작, 로드하는 시간때문에
  const [init, setInit] = useState(false); //초기화 안된 set
  const [userObj, setUserObj] = useState(null);
  const [originalUrl, setOriginalUrl] = useState([]);
  const [thumbUrl, setThumbUrl] = useState([]);
  useEffect(async ()=>{
    const movies = await axios.get("/albums/1/photos/list");
    console.log(movies);

    for(let i=0; i < movies.data.length; i++){
        console.log(movies.data[i]);
        setThumbUrl((thumbUrl) => {
              return [...thumbUrl,movies.data[i].thumbUrl];
            });

        //setThumbUrl([...thumbUrl,movies.data[i].thumbUrl], () => console.log(this.thumbUrl));

        }
    //console.log(thumbUrl);

  },[]);
  useEffect(()=> { //component가 mount될 때 실행됨, 그리고 변화를 들어야함.
    authService.onAuthStateChanged((user) => {
      if(user){
        setIsLoggedIn(true); 
        setUserObj(user);
      }else{
        setIsLoggedIn(false);
      }
      setInit(true);
      console.log(isLoggedIn);
      //console.log(authService.currentUser);
      
      
     });
  }, []);
  console.log(authService.currentUser); 
  //const auth = fbase.auth();
 
  //console.log(authService.currentUser); //이때 null이고
  setInterval(()=> {
    //console.log(authService.currentUser); //이때 확인하면 로그인돼 있음
  }, 2000);

  const [albumId,setAlbumId] = useState(1);
  //이미지 여러장 보내기
    const onSubmit = async (e) => {
        e.preventDefault();
        e.persist();

        let files = e.target.profile_files.files;
        let formData = new FormData();
        for (let i = 0; i < files.length; i++) {
          formData.append("photos", files[i]);
        }

        let dataSet = {
          name: "1",
        };

        formData.append("data", JSON.stringify(dataSet));

        const postSurvey = await axios({
          method: "POST",
          url: `/albums/${albumId}/photos`,
          mode: "cors",
          headers: {
            "Content-Type": "multipart/form-data",
          },
          data: formData,
        });

        console.log(postSurvey);
      };
    console.log(thumbUrl);




  return (

  <>
    <Button>Default Button</Button>;
  {init ? <AppRouter isLoggedIns={isLoggedIn} userObj={userObj} /> : " Initailizing" }
  <footer>&copy; spring frontend {new Date().getFullYear()}</footer>

  <form onSubmit={(e) => onSubmit(e)}>

    <div className="App">
    <input
      type="file"
      name="profile_files"
      multiple="multiple"
    />
      <Button as="input" type="button" value="제출" type="submit" />{' '}
    </div>

  </form>


  </>
  );
}
export default App;
//git config --global core.autocrlf true