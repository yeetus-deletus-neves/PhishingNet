import { defaultFetch } from "../utils/fetch";
import {useNavigate} from "react-router-dom"
import { useAuthentication } from "./auth/AuthProvider";
import { setStoredInfo, deleteStoredInfo } from "../scripts/localstorage";
import { useAlertContext } from "./Layout";

export function LoginPage(){
    const navigate = useNavigate()
    const [userInfo,setUserInfo] = useAuthentication()
    const [alert, setAlert] = useAlertContext()


    if(userInfo){
        return(
            <div className="container">
                <div id="container" className="center" >
                    <h2 className="center">You're currently logged in {userInfo.username}!</h2>
                    <h3 className="center">If you wish to switch accounts, you have to logout first</h3>
                    <button className="btn btn-dark" onClick={ ()=>{
                        deleteStoredInfo()
                        setUserInfo(null)
                    }
                }>
                    Logout from current account
                    </button>
                </div>
            </div>
        )
    }
    else{
        return (
            <div className="container">
                <div id="container" className="login-from center">
                    <h1>Login</h1>
                        <input placeholder="Username" type="text" name="username" id="username"/>
                        <input placeholder="Password" type="password" name="password" id="password"/>
                        <button type="submit" className="btn btn-dark" onClick={ async ()=>{
                            let username = document.getElementById('username').value;
                            let password = document.getElementById('password').value;
                            try{
                                const tokenRsp = await defaultFetch(
                                    'http://localhost:8080/user/signIn',
                                    "POST",
                                    {
                                        'Content-Type': 'application/json'
                                    },
                                    {
                                        "username": username,
                                        "password": password
                                    }
                                )
                                tokenRsp.username = username
                                setStoredInfo(tokenRsp)
                                setUserInfo(tokenRsp)
                                navigate("/")
                            }catch(error){
                                setAlert({alert: "error", message: `${error.details}`})
                            }
                        }}>Login</button>
                </div>
            </div>
        );
    }
}