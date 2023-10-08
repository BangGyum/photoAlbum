import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './components/App';
import initializeApp from "FirebaseInstance";
console.log(initializeApp);

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  // <React.StrictMode>
     <App />
  // </React.StrictMode>
  //strictmode는 side effect를 줄이기 위해 일부러 두번씩 실행시킴. 그래서 dev환경에서만 두번씩 호출되고 production에서는 무시
  //https://reactjs.org/docs/strict-mode.html#detecting-unexpected-side-effects
);


//https://console.firebase.google.com/u/0/project/bwitter-1dcf1/overview?hl=ko
//firebase SDK