import { useAuthentication } from "../utils/auth";
import { About } from "./About";
import { setStoredInfo } from "../utils/localstorage";
import { defaultFetch } from "../utils/fetch";

export function HomePage(){
    const [userInfo, setUserInfo] = useAuthentication()

    return (
        <main>
            <About/>
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
            }}>Submit</button>
            <br></br>
            <div>Not a member? <a style={{color: "#3d3df9", cursor: "pointer"}} onClick={()=>{
                window.open('http://localhost:3000/signUp','_blank')
            }}>Sign up here</a></div>
        </main>
    )
}