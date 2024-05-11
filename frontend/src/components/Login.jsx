import { useState } from "react";
import { defaultFetch } from "../utils/fetch";
import {useNavigate} from "react-router-dom"

export function LoginPage(){
    const navigate = useNavigate()
    const [storedInfo,setInfo] = useState(window.localStorage.getItem("userToken"))
    if(storedInfo){
        const tokenStored = JSON.parse(storedInfo)
        return(
            <div className="center">
                <h2 className="center">You're currently logged in {tokenStored.username}!</h2>
                <h3 className="center">If you wish to switch accounts, you have to logout first</h3>
                <button type="button" onClick={ ()=>{
                    window.localStorage.removeItem("userToken")
                    setInfo(null)
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
            <h1><b>Login</b></h1>
            <h1>Phising Net</h1>
                Username: <input type="text" name="username" id="username"/>
                Password: <input type="text" name="password" id="password"/>
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
                    window.localStorage.setItem("userToken",JSON.stringify(tokenRsp))
                    navigate("/")
                }}>Login</button>
        </div>
        );
    }
}