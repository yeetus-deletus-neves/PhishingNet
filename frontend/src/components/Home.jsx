import React from "react";

export function HomePage(){
    const tokenStored = JSON.parse(window.localStorage.getItem("userToken"))
    console.log(tokenStored)
    return (
    <div className="container">
        <div id="container" className="center">
            <h1 className="center">Phishing Net</h1>
            <h3 className="center">Logged in with {tokenStored.email}</h3>
        </div>
    </div>);
}