import { useState, useContext, createContext } from 'react';
import React from 'react';


const AuthContext = createContext();

export function AuthProvider({children}){
    const [auth, setAuth] = useState(undefined);

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