import React from "react";
import {HashRouter as Router, Route, Routes} from "react-router-dom";
import Auth from "Routes/Auth";
import Home from "Routes/Home";
import InAlbum from "Routes/InAlbum";
import InPhotoAlbum from "Routes/InPhotoAlbum";
import Navigation from "components/Navigation";

const AppRouter = ({ isLoggedIns, userObj, albumId }) => {
    //const [isLoggedIn, setIsLoggedIn] = useState(false);
    //isLoggedIns && <Navigation />} navigation이 존재하려면, isLoggedIns가 true여야 한다.
    console.log("Router albumId:",albumId);
    return (
    <Router>
        {<Navigation />}
        <Routes>
            {isLoggedIns ? (
                <>
                    <Route exact path="/"  element={<Home userObj={userObj}/>} />
                    <Route exact path="/album" element={<InAlbum/>} />
                    <Route exact path="/photoAlbum" element={<InPhotoAlbum propId={albumId}/>} />
                </>
                ) : (
                    <>
                    <Route exact path="/" element={<Auth/>} />
                    </>

                )

            }
        </Routes>
    </Router>
    );

}
export default AppRouter;
