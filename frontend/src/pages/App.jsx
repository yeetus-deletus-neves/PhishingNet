import {BrowserRouter, createBrowserRouter, Route, Routes} from 'react-router-dom'
import {Layout} from '../components/Layout'
import { ErrorPage } from '../components/ErrorHandling/ErrorPage';
import { HomePage } from '../components/Home';
import { SignUpPage } from '../components/SignUp';
import { LoginPage } from '../components/Login';
import React from 'react';


const router = createBrowserRouter([
    {
        element: <Layout/>,
        errorElement: <ErrorPage/>,
        children: [
            {
                path: "/",
                element: <HomePage/>
            },
            {
                path: "/signUp",
                element: <SignUpPage/>
            },
            {
                paht: "/login",
                element: <LoginPage/>
            }
        ]
    }
])


export default function App(){
    return (
        <BrowserRouter basename='/app' >
            <Layout/>
            <Routes element={<Layout/>}>
                <Route path='/' element={<HomePage/>}/>
                <Route path='/login' element={<LoginPage/>}/>
                <Route path='/signUp' element={<SignUpPage/>}/>
            </Routes>
        </BrowserRouter>
    );
}

