import React from "react";
import { useAuth } from "../components/AuthProvider";
import { HomePage } from './HomePage';
import { AuthPage } from './AuthPage';

export function PageSelector({msal_agent}) {
    const auth = useAuth();
    console.log(auth);
    return (auth[0] == undefined) ? <AuthPage msal_agent={msal_agent}/> : <HomePage/>;
}