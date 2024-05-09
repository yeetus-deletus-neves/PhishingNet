import { defaultFetch } from "../utils/fetch";
import {useNavigate} from "react-router-dom"

export function LoginPage(){
    const navigate = useNavigate()

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
                window.localStorage.setItem("userToken",JSON.stringify(tokenRsp))
                navigate("/")
            }}>Login</button>
    </div>);
}