import React from "react";
import { defaultFetch } from "../../utils/fetch";
import { MsalInterface } from "../../scripts/msal";
import { setStoredInfo } from "../../scripts/localstorage";
import { useAlertContext } from "./Layout";

export function SignUpPage(){
    const [alert, setAlert] = useAlertContext()

    return (
        <div className="container">
            <div id="container" className="signUp-form center">
                <h1>Cria conta</h1>
                    <input placeholder="Username" type="text" name="username" id="username"/>
                    <input placeholder="Password" type="password" name="password" id="password"/>
                    <button type="submit" class="btn btn-dark" onClick={ async ()=>{
                        let username = document.getElementById('username').value;
                        let password = document.getElementById('password').value;
                        try{
                            const tokenRsp = await defaultFetch(
                                'http://localhost:8080/user',
                                "POST",
                                {
                                    'Content-Type': 'application/json'
                                },
                                {
                                    "username": username,
                                    "password": password
                                }
                            )
                            if(tokenRsp.username){
                                const loginRsp = await defaultFetch(
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
                                loginRsp.username = username
                                setStoredInfo(loginRsp)
                                const msalAgent = new MsalInterface()
                                msalAgent.login()    
                            }else{
                                throw {
                                    details: "Conta não vinculada"
                                }
                            }
                        }catch(error){
                            if(error.details){
                                setAlert({alert: "error", message: `${error.details}`})
                            }else{
                                setAlert({alert: "error", message: "Erro interno de servidor"})
                            }
                        }
                }}>Submeter</button>
            </div>
        </div>
    );
}