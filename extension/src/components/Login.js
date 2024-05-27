import { useAuthentication } from "../auth/authenticationProvider";
import { setStoredInfo } from "../utils/localstorage";
import { defaultFetch } from "../utils/fetch";
import { useAlertContext } from "./Layout";

export function Login(){
    const [userInfo, setUserInfo] = useAuthentication()
    const [alert, setAlert] = useAlertContext()

    return (
        <div>
            <input placeholder="Username" type="text" name="username" id="username"/>
            <input placeholder="Password" type="password" name="password" id="password"/>
            <button type="button" onClick={ async ()=>{
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
                }catch(error){
                    setAlert({alert: "error", message: `${error.details}`})
                }
            }}>Submit</button>
        </div>
    )
}