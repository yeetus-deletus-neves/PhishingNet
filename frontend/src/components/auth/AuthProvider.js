import { createContext, useContext, useState } from "react";
import { getStoredInfo } from "../../scripts/localstorage";

const AuthContext = createContext([undefined,()=>{}])

export function AuthProvider({children}){
    const info = getStoredInfo()
    const [userInfo,setUserInfo] = useState(info)

    return(
        <AuthContext.Provider value={[userInfo,setUserInfo]}>
            {children}
        </AuthContext.Provider>
    )
}

export function useAuthentication(){
    return useContext(AuthContext)
}
