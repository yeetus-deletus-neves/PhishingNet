import {BrowserRouter, createBrowserRouter, Link, Route, Routes, RouterProvider} from 'react-router-dom'
import {Layout} from '../components/Layout'
import { HomePage } from '../components/Home';
import { SignUpPage } from '../components/SignUp';
import { LoginPage } from '../components/Login';
import React from 'react';
import { ErrorPage } from '../components/ErrorHandling/ErrorPage';
import { LinkPage } from '../components/Link';
import { AuthProvider } from '../components/auth/AuthProvider';
import { RequiresAuth } from '../components/auth/RequiresAuth';


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
                element:
                <RequiresAuth>
                    <LinkPage/>
                </RequiresAuth>
            }
        ]
    }
])


export default function App(){
    return (
        <AuthProvider>
            <RouterProvider router={router}/>
        </AuthProvider>
    );
}

