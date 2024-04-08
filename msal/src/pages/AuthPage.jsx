import { useAuth } from "../components/AuthProvider";
import React from "react";

function test(auth){
  

  console.log(auth)
}

export function AuthPage(){
  const [auth, setAuth] = useAuth();

    return (
        <main>
          <h1>Phishing Net</h1>
          <button onClick={()=>{setAuth('token')}}>Authorize</button>
        </main>
      );
}