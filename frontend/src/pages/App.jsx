import {BrowserRouter, Link, Route, Routes} from 'react-router-dom'
import {Layout} from '../components/Layout'
import { HomePage } from '../components/Home';
import { SignUpPage } from '../components/SignUp';
import { LoginPage } from '../components/Login';
import React from 'react';




export default function App({msalAgent}){
    return (
        <BrowserRouter basename='/app' >
            <Layout/>
            <Routes>
                <Route path='/' element={<HomePage/>}/>
                <Route path='/login' element={<LoginPage/>}/>
                <Route path='/signUp' element={<SignUpPage msalAgent={msalAgent}/>}/>
                <Route path='/link' element={<Link/>}/>
            </Routes>
        </BrowserRouter>
    );
}

