import React from "react";
import { MsalInterface } from "../scripts/msal";
import { useAuthentication } from "./auth/AuthProvider";

export function HomePage(){
    const [userInfo,setUserInfo] = useAuthentication()
    if(userInfo){

        if(userInfo.email){
            return (
                <div className="container">
                    <div id="container" className="center">
                        <h2 className="center">Hello {userInfo.username}!</h2>
                        <br></br>
                        <h3 className="center">Your account is currently linked with the email: {userInfo.email}</h3>
                        <br></br>
                        <h3 className="center">Make sure to enter in this account inbox for the extension to work</h3>
                    </div>
                </div>
            );
        }else{
            return (
                <div className="container">
                    <div id="container" className="center">
                        <h2 className="center">Hello {userInfo.username}!</h2>
                        <h3 className="center">Account not linked</h3>
                        <button type="button" onClick={()=>{
                            const msalAgent = new MsalInterface()
                            msalAgent.login()
                        }
                        }> Link account </button>
                    </div>
                </div>
            );
        }
    }else{
        return(
            <div className="container">
                <div id="container" className="center">
                    <h1 className="center">Phishing Net</h1>
                    <h3>You are currently not logged in</h3>
                </div>
            </div>
        )
    }
}