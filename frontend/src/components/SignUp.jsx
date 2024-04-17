import React from "react";
import { defaultFetch } from "../../../extension/src/utils/fetch";
import { MsalInterface } from "../scripts/msal";

export function SignUpPage(){
    const msalAgent = new MsalInterface()

    return (
        <div className="signUp-form">
            <h1><b>SignUp</b></h1><hr/>
            <h1>Phising Net</h1>
                Username: <input type="text" name="username" id="username"/>
                Password: <input type="text" name="password" id="password"/>
                <button type="button" onClick={ async ()=>{
                let username = document.getElementById('username').value;
                let password = document.getElementById('password').value;
                console.log(`Username: ${username} / Password: ${password}`);
                
                
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
                    //keep login persistent
                    /* 
                    window.open('https://login.microsoftonline.com/common/oauth2/v2.0/authorize?'+
                    'client_id=cb14d1d3-9a43-4b04-9c52-555211443e63'+
                    '&response_type=code'+
                    '&redirect_uri=http://localhost:3000/app'+
                    '&response_mode=query'+
                    '&scope=offline_access User.read Mail.read'+
                    '&state=12345')
                    */
                    msalAgent.login()

                }else{
                    console.log('Throw error here')
                }
            }}>Create User</button>
        </div>
    );
}