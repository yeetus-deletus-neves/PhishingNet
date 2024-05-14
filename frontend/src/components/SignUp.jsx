import React from "react";
import { defaultFetch } from "../utils/fetch";
import { MsalInterface } from "../scripts/msal";
import { setStoredInfo } from "../scripts/localstorage";

export function SignUpPage(){
    
    return (
        <div className="signUp-form">
            <h1><b>SignUp</b></h1>
            <h1>Phising Net</h1>
                Username: <input type="text" name="username" id="username"/>
                Password: <input type="text" name="password" id="password"/>
                <button type="button" onClick={ async ()=>{
                    let username = document.getElementById('username').value;
                    let password = document.getElementById('password').value;
                    
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
                    console.log('Throw error here')
                }
            }}>Create User</button>
        </div>
    );
}