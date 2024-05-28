import React from "react";
import { useAuthentication } from "../auth/authenticationProvider";

import { Login } from "../components/Login";

  
const App = () => {
    const [userInfo,setUserInfo] = useAuthentication()
    
    if(!userInfo){
        return (
            <Login/>
        )
    }
}

export default App;