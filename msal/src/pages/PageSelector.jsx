import React from "react";
import { useAuth } from "../components/AuthProvider";
import { HomePage } from './HomePage';
import { AuthPage } from './AuthPage';

export function PageSelector() {
    const auth = useAuth();
    console.log(auth);
    return (auth[0] == undefined) ? <AuthPage/> : <HomePage/>;
}