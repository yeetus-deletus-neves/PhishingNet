import React from "react";
import { MsalInterface } from "../scripts/msal";

export function HomePage(){
    const storedInfo = window.localStorage.getItem("userToken")
    if(storedInfo){
        const tokenStored = JSON.parse(storedInfo)
        if(tokenStored.email){
            return (
                <div className="container">
                    <div id="container" className="center">
                        <h2 className="center">Hello {tokenStored.username}!</h2>
                        <h3 className="center">Your account is currently linked with the email: {tokenStored.email}</h3>
                    </div>
                </div>
            );
        }else{
            const msalAgent = new MsalInterface()
            return (
                <div className="container">
                    <div id="container" className="center">
                        <h2 className="center">Hello {tokenStored.username}!</h2>
                        <h3 className="center">Account not linked</h3>
                        <h4 className="center"> To link the account click bellow</h4>
                        <button type="button" onClick={
                            msalAgent.login()
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