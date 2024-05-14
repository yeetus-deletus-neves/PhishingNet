import { defaultFetch } from "../utils/fetch";
import {useNavigate} from "react-router-dom"
import { useAuthentication } from "./auth/AuthProvider";
import { setStoredInfo, deleteStoredInfo } from "../scripts/localstorage";

export function LoginPage(){
    const navigate = useNavigate()
    const [userInfo,setUserInfo] = useAuthentication()
    if(userInfo){
        return(
            <div className="center">
                <h2 className="center">You're currently logged in {userInfo.username}!</h2>
                <h3 className="center">If you wish to switch accounts, you have to logout first</h3>
                <button type="button" onClick={ ()=>{
                    deleteStoredInfo()
                    setUserInfo(null)
                }
                }>
                Logout from current account
                </button>
            </div>
        )
    }
    else{
        return (
        <div className="login-from">
            <h1>Login</h1>
                <input placeholder="Username" type="text" name="username" id="username"/>
                <input placeholder="Password" type="password" name="password" id="password"/>
                <button type="button" onClick={ async ()=>{
                    let username = document.getElementById('username').value;
                    let password = document.getElementById('password').value;
                    
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
                }}>Login</button>
        </div>
        );
    }
}