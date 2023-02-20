
import { getAuth } from "firebase/auth";
import firebase,{ initializeApp } from 'firebase/app';
import 'firebase/compat/auth';
import { getFirestore } from "firebase/firestore";
//firebase의 db는 no sql로, 많은것들을 프로그램하지 않아도 되고, 유연함을 가지고 있다. 하지만 자유도가 높진 않음.
//특징 : collection과 document가 있음. col은 폴더, doc은 문서같은 텍스트 => col은 doc을 가지고있음. 

//https://firebase.google.com/docs/reference/js/auth

const firebaseConfig = {
    apiKey: process.env.REACT_APP_API_KEY,
    authDomain: process.env.REACT_APP_AUTH_DOMAIN,
    projectId: process.env.REACT_APP_PROJECT_ID,
    storageBucket: process.env.REACT_APP_STORAGE_BUCKET,
    messagingSenderId: process.env.REACT_APP_MESSAGIN_ID,
    appId: process.env.REACT_APP_APP_ID
  };
  //env 파일을 git에 안올라가기에 보안을 위해 이렇게 하지만.
  //값을 치환하여 빌드하기 때문에 결국 보임.

  const app = initializeApp(firebaseConfig);
  export const authService = getAuth(app);

  export default authService;
  export const dbService = getFirestore();
