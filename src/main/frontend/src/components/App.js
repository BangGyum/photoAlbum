import React, { useEffect, useState } from "react";
import AppRouter from "components/Router";
import axios from 'axios';
import { authService } from "FirebaseInstance";


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
    useEffect(async ()=>{
      try {
        //응답 성공
        const response = await axios.post("/albums",{
          	//보내고자 하는 데이터
            albumName: "리액트에서 앨범 추가"
        });
        console.log(response);
      } catch (error) {
        //응답 실패
        console.error(error);
      }
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


  //이미지 전부 받기
//  const [images, setImages] = useState([]);
//  let userForm = new FormData();
//
//
//    useEffect(() => {
//      fetchImages();
//    }, []);
//
//    const fetchImages = async () => {
//      const response = await axios.request({
//        url: "/albums/1/photos/getPhotos",
//        method: "GET",
//        responseType: "arraybuffer",
//      });
//      console.log("aa");
//      console.log(response);
//      console.log("aa");
//
//      const imageList = Object.values(response.data).map((imageData, index) => {
//        const imageBlob = new Blob([imageData], { type: "image/png" });
//        const imageUrl = URL.createObjectURL(imageBlob);
//        console.log("aaa");
//        console.log(imageBlob);
//        return {
//          id: index,
//          url: imageUrl,
//        };
//      });
//
//      setImages(imageList);
//    };
//    console.log("bb");
//    console.log(images);

//이미지 전부받기 2
  const [image, setImage] = useState([]);

//  useEffect(() => {
//    axios.get('/albums/1/photos/getPhotos', { responseType: 'arraybuffer' })
//      .then(response => {
//        const buffer = new ArrayBuffer(response.data.length);
//        const view = new Uint8Array(buffer);
//        console.log(response.data);
//        for (let i = 0; i < response.data.length; i++) {
//          view[i] = response.data.charCodeAt(i);
//        }
//        const blob = new Blob([buffer], { type: 'image/png' });
//        const imgUrl = URL.createObjectURL(blob);
//        setImages([imgUrl]);
//      })
//      .catch(error => {
//        console.log(error);
//      });
//  }, []);
//   useEffect(() => {
//       axios.get('/albums/1/photos/getPhotos', { responseType: 'arraybuffer' })
//         .then(response => {
//           const imageBlobs = response.data.map(imgBytes => new Blob([imgBytes], { type: 'image/png' }));
//           const imgUrls = imageBlobs.map(blob => URL.createObjectURL(blob));
//           setImages(imgUrls);
//         })
//         .catch(error => {
//           console.log(error);
//         });
//     }, []);
  const [images, setImages] = useState([]);

  useEffect(() => {
    axios.get('/albums/1/photos/getPhotos')
      .then(response => setImages(response.data) )
      .catch(error => console.error(error));
  }, []);
  console.log(images);


  return (
    <>
  {init ? <AppRouter isLoggedIns={isLoggedIn} userObj={userObj} /> : " Initailizing" }
  <footer>&copy; spring frontend {new Date().getFullYear()}</footer>
  <div>
      <a>{message}</a>
  </div>
  <form onSubmit={(e) => onSubmit(e)}>
    <input
      type="file"
      name="profile_files"
      multiple="multiple"
    />

    <button type="submit">제출</button>
  </form>
    <div>
       {images.map((image, index) => (
         <img
           key={index}
           src={URL.createObjectURL(new Blob([image]))}
           alt={`Image ${index}`}
         />
       ))}
    </div>


  </>
  );
}
export default App;
//git config --global core.autocrlf true