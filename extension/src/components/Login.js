import { useAuthentication } from "../auth/authenticationProvider";
import { deleteStoredMap, setStoredInfo } from "../utils/localstorage";
import { defaultFetch } from "../utils/fetch";
import { useAlertContext } from "./Layout";

export function Login(){
    const [userInfo, setUserInfo] = useAuthentication()
    const [alert, setAlert] = useAlertContext()

    return (
        <div>
            <div style={{height: "1rem"}}/>
            <div class= "form-group">
                <input placeholder="Username" type="text" name="username" id="username" style={{width: "12rem"}}/>
            </div>
            <div class= "form-group">
                <input placeholder="Password" type="password" name="password" id="password" style={{width: "12rem"}}/>
            </div>
            <div style={{height: "1rem"}}/>
            <div class= "form-group">
                <button type="submit" class="btn btn-primary" style={{width: "12rem"}} onClick={ async ()=>{
                    let username = document.getElementById('username').value;
                    let password = document.getElementById('password').value;
                    try{
                        const rsp = await defaultFetch(
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
                        const tokenRsp = await rsp.json()
                        tokenRsp.username = username
                        setStoredInfo(tokenRsp)
                        setUserInfo(tokenRsp)
                        deleteStoredMap()
                    }catch(error){
                        console.log(error)
                        if(!error.status){
                            setAlert({alert: "error", message: "Server timeout"})
                        }else{
                            setAlert({alert: "error", message: `${error.details}`})
                        }
                    }
                }}>Login</button>
            </div>
        </div>
    )
}