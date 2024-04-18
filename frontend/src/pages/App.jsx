import {BrowserRouter, createBrowserRouter, Link, Route, Routes, RouterProvider} from 'react-router-dom'
import {Layout} from '../components/Layout'
import { HomePage } from '../components/Home';
import { SignUpPage } from '../components/SignUp';
import { LoginPage } from '../components/Login';
import React from 'react';
import { ErrorPage } from '../components/ErrorHandling/ErrorPage';
import { LinkPage } from '../components/Link';


const router = createBrowserRouter([
    {
        element: <Layout/>,
        errorElement: <ErrorPage/>,
        children: [
            {
                path:'/',
                element:<HomePage/>
            },
            {
                path:'/login',
                element:<LoginPage/>
            },
            {
                path:'/signUp',
                element:<SignUpPage/>
            },
            {
                path:'/link',
                element:<LinkPage/>
            }
        ]
    }
])


export default function App({msalAgent}){
    return (
        <RouterProvider router={router}/>
        /*
        <BrowserRouter basename='/app' >
            <Layout/>
            <Routes>
                <Route path='/' element={<HomePage/>}/>
                <Route path='/login' element={<LoginPage/>}/>
                <Route path='/signUp' element={<SignUpPage msalAgent={msalAgent}/>}/>
                <Route path='/link' element={<Link/>}/>
            </Routes>
        </BrowserRouter>
        */
    );
}

