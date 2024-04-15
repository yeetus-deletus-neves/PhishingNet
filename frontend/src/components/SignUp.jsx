import React from "react";
import { defaultFetch } from "../../../extension/src/utils/fetch";

export function SignUpPage(){


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
                    console.log(loginRsp)
                }
                console.log(tokenRsp);
            }}>Create User</button>
        </div>
    );
}