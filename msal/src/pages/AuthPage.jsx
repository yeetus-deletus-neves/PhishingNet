import { useAuth } from "../components/AuthProvider";
import React from "react";


export function AuthPage({msal_agent}){
  const [auth, setAuth] = useAuth();

    return (
        <main>
          <h1>Phishing Net</h1>
          <button onClick={()=>{
            msal_agent.login();
          }}>Authorize</button>
        </main>
      );
}