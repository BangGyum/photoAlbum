import React from "react";
import { authService } from "FirebaseInstance"
import { useNavigate  } from "react-router-dom";

const Profile = () => {
    const navigate = useNavigate(); //양식이 제출 or 특정 event가 발생 시, url을 조작할 수 있는 interface
    const onClickLogOut = () => {
        authService.signOut();

        navigate(-1);  //뒤로
    }

    return (
        <>
            <button onClick={onClickLogOut}>Log out</button>
        </>
    );
}
export default Profile;