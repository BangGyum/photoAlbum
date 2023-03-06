import React from "react";
import {HashRouter as Router, Route, Routes} from "react-router-dom";
import InAlbum from "Routes/InAlbum";
import InPhotoAlbum from "Routes/InPhotoAlbum";
import Navigation from "components/Navigation";

const AppRouterInPhotoAlbum = ({ albumId }) => {
    return (
    <Router>
        {<Navigation />}
        <Routes>
                <>
                    <Route exact path="/photoAlbum" element={<InPhotoAlbum/>} />
                </>
            }
        </Routes>
    </Router>
    );

}
export default AppRouterInPhotoAlbum;