import { useAuth } from "../components/AuthProvider";
import React from "react";
import { MsalInterface } from "../scripts/msal.js";



export function AuthPage(){
  const [auth, setAuth] = useAuth();

    return (
        <main>
          <h1>Phishing Net</h1>
          <button onClick={()=>{
            const msal_agent = new MsalInterface();
            msal_agent.login();
          }}>Authorize</button>
        </main>
      );
}