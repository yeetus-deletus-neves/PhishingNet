import { useState, useContext, createContext } from 'react';
import React from 'react';


const AuthContext = createContext();
const CanGetCode = createContext();

export function AuthProvider({children}){
    const [auth, setAuth] = useState(undefined);
    const [canCode, setCanCode] = useState(false);

    //TODO: Ler se ja existe token na cache
    
    return (
        <AuthContext.Provider value={[auth, setAuth]}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth(){
    return useContext(AuthContext);
}

export function canCode(){
    return useContext(CanGetCode);
}
