import React from "react";
import { useAuthentication } from "../auth/authenticationProvider";

import { Login } from "../components/Login";

  
const App = () => {
    const [userInfo,setUserInfo] = useAuthentication()
    
    if(!userInfo){
        return (
            <Login/>
        )
    }else{
        return(
            <div>
                <h4 class="card-title">{userInfo.username}</h4> 
            </div>
        )
    }
}

export default App;