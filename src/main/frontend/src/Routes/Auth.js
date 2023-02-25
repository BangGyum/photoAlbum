import React,{useState} from "react";
import { getAuth,
     signInWithEmailAndPassword,
      createUserWithEmailAndPassword,
      GoogleAuthProvider,
      GithubAuthProvider,
      signInWithPopup} from "firebase/auth";
import { authService } from "FirebaseInstance";

import 'bootstrap/dist/css/bootstrap.min.css';
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Container from 'react-bootstrap/Container';

//https://firebase.google.com/docs/auth/web/password-auth?authuser=1#create_a_password-based_account

const Auth = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [newAccount, setNewAccount] = useState(true);
    const [error, setError] = useState("");
    const onChange = (event) =>{
        //console.log(event.target.name);
        const {target : {name, value}} =event;
        if(name ==="email"){
            setEmail(value);
        } else if (name ==="password"){
            setPassword(value);
        }
        //console.log(email);
        //console.log(password);

    };
    const onSubmit = async (event) => {
        event.preventDefault();
        try{
            const auth = getAuth();
            let authAccount;
            if(newAccount){
                authAccount = await createUserWithEmailAndPassword(auth, email, password);
            }else{
                authAccount = await signInWithEmailAndPassword(auth, email, password);
            }
            //console.log(authAccount);
        }catch (error){
            setError(error.message);
        }

    }
    const toggleAccount =() => {
        setNewAccount((prev) => !prev);
    };
    const onSocialLogin = async (event) => {
        //console.log(event.target.name);
        const {target:{name}} = event;
        let provider;
        if(name === "google"){
            provider = new GoogleAuthProvider();
        }else if (name === "github"){
            provider = new GithubAuthProvider();
        }
        const data = await signInWithPopup(authService, provider);
        console.log(data);
    }
    //    <form onSubmit={onSubmit}>
//              <input name="email" type="text" placeholder="Email" value={email} onChange={onChange} required/>
//              <input name="password" type="password" placeholder="Password" onChange={onChange} required value={password}/>
//              <input type="submit" value={newAccount ? "Create Account" : "Log In"} />
//              <a>{error}</a>
//          </form>
return (
<>
<div>
        <Container className="panel">
            <Form onSubmit={onSubmit}>
                <Form.Group as={Row} className="mb-3" controlId="formPlaintextPassword">
                    <Col sm>
                        <Form.Control type="password" placeholder="Email" value={email} onChange={onChange} required />
                    </Col>
                </Form.Group>

                <Form.Group as={Row} className="mb-3" controlId="formPlaintextPassword">
                    <Col sm>
                        <Form.Control type="password" placeholder="Password" onChange={onChange} value={password} required />
                    </Col>
                </Form.Group>
                <br/>

                <div className="d-grid gap-1">
                    <Button variant="secondary" type="submit" >
                        {newAccount ? "Create Account" : "Log In"}
                    </Button>
                </div>
            </Form>
        </Container>

    <span onClick={toggleAccount}>{newAccount? "Log in ":"Create Account"}</span>
    <div>
        <button name="google" onClick={onSocialLogin}>Continue with Google</button>
        <button name="github" onClick={onSocialLogin}>Continue with Github</button>
    </div>
</div>
</>
);
};
export default Auth;