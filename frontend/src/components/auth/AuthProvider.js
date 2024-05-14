import { createContext, useContext, useState } from "react";

const AuthContext = createContext([undefined,()=>{}])

export function AuthProvider({children}){
    const storedInfo = window.localStorage.getItem("userToken")
    let info = undefined
    if(storedInfo){
        info = JSON.parse(storedInfo)
    }
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
