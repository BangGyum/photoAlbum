import React, { useEffect, useState } from "react";
import AppRouter from "components/Router";
import axios from 'axios';
import { authService } from "FirebaseInstance";


function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);//처음에 무조건 로그아웃으로 시작, 로드하는 시간때문에
  const [init, setInit] = useState(false); //초기화 안된 set
  const [userObj, setUserObj] = useState(null);
  useEffect(async ()=>{
        const movies = await axios.get("/albums/1/photos/list");
        console.log(movies);
      },[]);
  const [message, setMessage]=useState([]);
    useEffect(()=>{
      //fetch("localhost:8081/albums/albumList")
      //fetch("/api/demo-web")api/photoList
      fetch("/api/demo-web")
          .then((response)=>{
            return response.json();
          })
          .then((data)=>{
              setMessage(data);
          });
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
  return (
    <>
  {init ? <AppRouter isLoggedIns={isLoggedIn} userObj={userObj} /> : " Initailizing" }
  <footer>&copy; Bwitter {new Date().getFullYear()}</footer>
  <div>
      <a>{message}</a>
  </div>
  </>
  );
}
export default App;
//git config --global core.autocrlf true