//const { createProxyMiddleware } = require('http-proxy-middleware');
//
//module.exports = function(app){
//  app.use(
//      createProxyMiddleware('/api', { //api 는 프록시를 사용할 경로 (path)
//          target: 'http://localhost:8081/', //target 은 내가 프록시로 이용할 서버의 주소
//          changeOrigin: true //changeOrigin은 대상 서버의 구성에 따라 호스트 헤더의 변경을 해주는 옵션
//      })
//  )
//};
// src/main/frontend/src/setProxy.js

const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
  app.use(
    '/api',
    createProxyMiddleware({
      target: 'http://localhost:8081',
      changeOrigin: true,
    })
  );
};