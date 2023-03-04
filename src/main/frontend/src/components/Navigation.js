import React from "react";
import { Link } from "react-router-dom";

const Navigation = () => { //link를 사용해서 이동
    return(

        <ul>
            <li><Link to="/">Home</Link></li>
            <li><Link to="/album">InAlbum</Link></li>
            <li><Link to="/photoAlbum/1">InPhotoAlbum</Link></li>
        </ul>
);
}
export default Navigation;